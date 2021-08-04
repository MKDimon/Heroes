package heroes.player.botgleb;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Бот по стратегии "минимакс".
 **/

public class ExpectiMaxBot extends BaseBot implements Visualisable {

    private static final int maxRecLevel = 2;
    private static final UtilityFunction utilityFunction = UtilityFunctions.HPUtilityFunction;

    private static final Logger logger = LoggerFactory.getLogger(SimpleMinMaxBot.class);

    protected final TerminalWrapper tw = null;

    /**
     * Фабрика ботов.
     **/

    public static class ExpectiMaxBotFactory extends BaseBotFactory {
        @Override
        public ExpectiMaxBot createBot(final Fields fields) throws GameLogicException {
            return new ExpectiMaxBot(fields);
        }
    }

    private static final class AnswerAndWin {
        final Answer answer;
        final double win;

        private AnswerAndWin(final Answer answer, final double win) {
            this.answer = answer;
            this.win = win;
        }
    }

    public ExpectiMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.tw = tw;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new TestBot(getField()).getArmy(firstPlayerArmy);
        } catch (final GameLogicException e) {
            logger.error("Error creating army by ExpectiMaxBot", e);
            return null;
        }
    }

    /**
     * Получение ответа от бота.
     **/

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        try {
            final long startTime = System.currentTimeMillis();
            final List<Answer> actions = board.getPossibleMoves();
            final List<AnswerAndWin> awList = new ArrayList<>();
            for (final Answer answer : actions) {
                final Board implBoard = board.copy(answer);
                final double win = getWinByGameTree(implBoard, 1);
                awList.add(new AnswerAndWin(answer, win));
            }
            System.out.println("ExpectiMax bot time: " + (System.currentTimeMillis() - startTime));
            return getGreedyDecision(awList).answer;

        } catch (final GameLogicException | BoardException | UnitException e) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    /**
     * Метод рекурсивно строит дерево ходов.
     * Если узел терминальный, или достигнута максимальная глубина рекурсии,
     * возвращает либо максимальное значение функции оценки узла, либо "среднюю" оценку.
     * Таким образом, на верхний уровень пробрасывается нужная оценка состояний из нижних уровней.
     **/

    private double getWinByGameTree(final Board implBoard, final int recLevel)
            throws BoardException, UnitException, GameLogicException {
        final boolean isMax = implBoard.getCurrentPlayer() == getField();
        // Если состояние терминальное, и победил агент, то возвращает +большое число,
        // если победил соперник, возвращает -большое число.
        if (implBoard.getStatus() != GameStatus.GAME_PROCESS) {
            return getTerminalStateValue(implBoard);
        }
        if (recLevel >= maxRecLevel) {
            // функция полезности вычисляется для агента.
            // Показывает, насколько поелзно будет ему это действие
            return utilityFunction.compute(implBoard, getField());
        }
        // Если состояние не терминальное, и не достигнут максимлаьынй уровень рекурсии,
        // то начинаем строить дерево из текущего состояния.
        final List<Answer> actions = implBoard.getPossibleMoves();
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Answer answer : actions) {
            final Board simBoard = implBoard.copy(answer);
            final double win = getWinByGameTree(simBoard, recLevel + 1);
            awList.add(new AnswerAndWin(answer, win));
        }

        // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому
        // корневому ответу сопоставляется значение из нижнего состяния.
        if (isMax) {
            return getGreedyDecision(awList).win;
        } else {
            return getChance(awList, aw -> aw.win / awList.size());
        }

    }

    /**
     * Метод находит в списке awList элемент с максимальным полем win.
     **/

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = bestAW.win;
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = currentAW.win;
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }

    /**
     * Метод оценивает вероятностные узлы с помощью функции probabilityFunction.
     **/

    private double getChance(final List<AnswerAndWin> awList,
                             final ToDoubleFunction<AnswerAndWin> probabilityFunction) {
        double win = 0;
        for (final AnswerAndWin aw : awList) {
            win += probabilityFunction.applyAsDouble(aw);
        }
        return win;
    }

    /**
     * Метод вычисляет тип терминального состояния и выдает в соответствии с ним значение функции полезности
     * (+- условная бесконечность, либо 0, если ничья).
     **/

    private double getTerminalStateValue(final Board board) throws GameLogicException {
        if (board.getStatus() == GameStatus.GAME_PROCESS) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        if (board.getStatus() == GameStatus.NO_WINNERS) {
            return -100000d;
        }
        if ((board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE) ||
                (board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO)) {
            System.out.println("CAN WIN");
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }

}
