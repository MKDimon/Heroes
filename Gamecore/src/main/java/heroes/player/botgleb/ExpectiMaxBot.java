package heroes.player.botgleb;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.ClientsConfigs;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Бот по стратегии "ExpectiMax".
 **/

public class ExpectiMaxBot extends AIBot implements Visualisable {

    /**
     * Фабрика ботов.
     **/

    public static class ExpectiMaxBotFactory extends AIBotFactory {
        @Override
        public ExpectiMaxBot createBot(final Fields fields) throws GameLogicException {
            return new ExpectiMaxBot(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new ExpectiMaxBot(fields);
        }

        @Override
        public AIBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                 final int maxRecLevel) throws GameLogicException {
            return new ExpectiMaxBot(fields, utilityFunction, maxRecLevel);
        }
    }

    public ExpectiMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public ExpectiMaxBot(final Fields fields, final UtilityFunction utilityFunction, final int maxRecLevel)
            throws GameLogicException {
        super(fields, utilityFunction, maxRecLevel);
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
            return getMaxAW(awList).answer();

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
        if (recLevel >= getMaxRecLevel()) {
            // функция полезности вычисляется для агента.
            // Показывает, насколько поелзно будет ему это действие
            return getUtilityFunction().compute(implBoard, getField());
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
            return getMaxAW(awList).win();
        } else {
            return getChance(awList, aw -> aw.win() / awList.size());
        }

    }

    /**
     * Метод находит в списке awList элемент с максимальным полем win.
     **/

    protected AnswerAndWin getMaxAW(final List<AnswerAndWin> awList) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = bestAW.win();
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = currentAW.win();
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

    protected double getChance(final List<AnswerAndWin> awList,
                               final ToDoubleFunction<AnswerAndWin> probabilityFunction) {
        double win = 0;
        for (final AnswerAndWin aw : awList) {
            win += probabilityFunction.applyAsDouble(aw);
        }
        return win;
    }

}
