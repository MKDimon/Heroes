package heroes.player.botgleb;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.*;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class SimpleMinMaxBot extends BaseBot implements Visualisable {

    private static final int maxRecLevel = 2;
    private static final UtilityFunction utilityFunction = UtilityFunctions.simpleUtilityFunction;

    private static final Logger logger = LoggerFactory.getLogger(SimpleMinMaxBot.class);

    protected final TerminalWrapper tw = null;

    /**
     * Фабрика ботов.
     **/

    public static class SimpleMinMaxBotFactory extends BaseBotFactory {
        @Override
        public SimpleMinMaxBot createBot(final Fields fields) throws GameLogicException {
            return new SimpleMinMaxBot(fields);
        }
    }

    private static final class AnswerAndWin implements Comparable<AnswerAndWin> {
        final Answer answer;
        final double win;

        private AnswerAndWin(final Answer answer, final double win){
            this.answer = answer;
            this.win = win;
        }

        @Override
        public int compareTo(final AnswerAndWin o) {
            return Double.compare(win, o.win);
        }
    }

    public SimpleMinMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.tw = tw;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new TestBot(getField()).getArmy(firstPlayerArmy); //RandomBot(getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            logger.error("Error creating army by SimpleMinMaxbot", e);
            return null;
        }
    }

    /**
     * Получение ответа от бота. В методе строится дерево возсожных ходов и
     **/

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        try {
            final List<Answer> actions = board.getPossibleMoves();
            final List<AnswerAndWin> awList = new ArrayList<>();
            for(final Answer answer : actions){
                final Board implBoard = board.copy(answer);
                final double win = getWinByGameTree(implBoard, 1, UtilityFunctions.MAX_VALUE,
                        UtilityFunctions.MIN_VALUE);
                awList.add(new AnswerAndWin(answer, win));
            }
            return getGreedyDecision(awList, aw -> aw.win).answer;

        } catch (UnitException | BoardException | GameLogicException e) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    /**
     * Метод рекурсивно строит дерево ходов.
     * Если узел терминальный, или достигнута максимальная глубина рекурсии,
     * возвращает максимальное (или минимальное) значение функции оценки узла.
     * Таким образом, на верхний уровень пробрасывается нужная оценка состояний из нижних уровней.
     **/

    private double getWinByGameTree(final Board implBoard, final int recLevel,
                                     double alpha, double beta) throws BoardException, UnitException, GameLogicException {
        final ToDoubleFunction<AnswerAndWin> winCalculator;
        //Если сейчас ход агента, то функция полезности будет исходной,
        //если ход противника - домножим функцию на -1.
        final boolean isMax = implBoard.getCurrentPlayer() == getField();
        if (isMax) {
            winCalculator = aw -> aw.win;
        } else {
            winCalculator = aw -> -aw.win;
        }
        // Если состояние терминальное, и победил агент, то возвращает +большое число,
        // если победил соперник, возвращает -большое число.
        if(implBoard.getStatus() != GameStatus.GAME_PROCESS){
            return getTerminalStateValue(implBoard);
        }
        if(recLevel >= maxRecLevel){
            // функция полезности вычисляется для агента.
            // Показывает, насколько поелзно будет ему это действие
            return utilityFunction.compute(implBoard, getField());
        }
        // Если состояние не терминальное, и не достигнут максимлаьынй уровень рекурсии,
        // то начинаем строить дерево из текущего состояния.
        final List<Answer> actions = implBoard.getPossibleMoves();
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Answer answer : actions){
            final Board simBoard = implBoard.copy(answer);
            final double win = getWinByGameTree(simBoard, recLevel+1, alpha, beta);
            if (isMax && win > beta || !isMax && win < alpha){
                return win;
            }
            alpha = Math.max(alpha, win);
            beta = Math.min(beta, win);
            awList.add(new AnswerAndWin(answer, win));
        }
        // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому, так сказать,
        // корневому ответу сопоставляется значение из нижнего состяния.
        return getGreedyDecision(awList, winCalculator).win;
    }

    /**
     * Метод находит в списке awList элемент с максимальным полем win.
     * Если поиск происходит среди ответов соперника, то на поле win вешается минус. Таким образом,
     * метод находит ответ с максимальной ценностью, если ходит агент, и ответ с минимальной (для агента)
     * ценностью, если ходит соперник.
     **/

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList,
                                                     final ToDoubleFunction<AnswerAndWin> winCalculator){
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = winCalculator.applyAsDouble(bestAW);
        for(int i = 1; i < awList.size(); i++){
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            if(currentWin > bestWin){
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
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
            return 0d;
        }
        if (board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }

}
