package heroes.player.botgleb;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.ToDoubleFunction;

/**
 * Бот по стратегии "минимакс" с использованием ForkJoinPull.
 **/

public class MultithreadedMinMaxBot extends BaseBot implements Visualisable {

    private static final int maxRecLevel = 3;
    private static final UtilityFunction utilityFunction = UtilityFunctions.HPUtilityFunction;

    private static final Logger logger = LoggerFactory.getLogger(SimpleMinMaxBot.class);

    protected final TerminalWrapper tw = null;

    /**
     * Фабрика ботов.
     **/

    public static class MultithreadedMinMaxBotFactory extends BaseBotFactory {
        @Override
        public MultithreadedMinMaxBot createBot(final Fields fields) throws GameLogicException {
            return new MultithreadedMinMaxBot(fields);
        }
    }

    /**
     * Вспомогательный класс. Хранит ответ и оценку состояния доски, к которому приведет этот ответ.
     **/

    private static final class AnswerAndWin {
        final Answer answer;
        final double win;

        private AnswerAndWin(final Answer answer, final double win) {
            this.answer = answer;
            this.win = win;
        }
    }

    /**
     * Класс "распараллеливвтель" задач. Метод compute() строит дерево игры.
     **/

    private final class WinCounter extends RecursiveTask<AnswerAndWin> {

        private final Board implBoard;
        private final int recLevel;
        private final Answer rootAnswer;

        private WinCounter(final Board implBoard, final int recLevel, final Answer rootAnswer) {
            this.implBoard = implBoard;

            this.recLevel = recLevel;
            this.rootAnswer = rootAnswer;
        }

        @Override
        protected AnswerAndWin compute() {
            try {
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
                if (implBoard.getStatus() != GameStatus.GAME_PROCESS) {
                    return new AnswerAndWin(rootAnswer, getTerminalStateValue(implBoard));
                }
                if (recLevel >= maxRecLevel) {
                    // функция полезности вычисляется для агента.
                    // Показывает, насколько поелзно будет ему это действие
                    return new AnswerAndWin(rootAnswer, utilityFunction.compute(implBoard, getField()));
                }
                // Если состояние не терминальное, и не достигнут максимлаьынй уровень рекурсии,
                // то начинаем строить дерево из текущего состояния.
                final List<Answer> actions = implBoard.getPossibleMoves();
                final List<AnswerAndWin> awList = new ArrayList<>();
                final List<WinCounter> subTasks = new LinkedList<>();
                for (final Answer answer : actions) {
                    final WinCounter task = new WinCounter(implBoard.copy(answer), recLevel + 1, answer);
                    task.fork();
                    subTasks.add(task);
                }
                for (final WinCounter subTask : subTasks) {
                    final AnswerAndWin aw = subTask.join();
                    awList.add(aw);
                }

                // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому
                // корневому ответу сопоставляется значение из нижнего состяния.
                if (rootAnswer != null) {
                    return new AnswerAndWin(rootAnswer, getGreedyDecision(awList, winCalculator).win);
                } else {
                    return getGreedyDecision(awList, winCalculator);
                }
            } catch (GameLogicException e) {
                return null;
            }
        }
    }

    public MultithreadedMinMaxBot(final Fields field) throws GameLogicException {
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
            logger.error("Error creating army by SimpleMinMaxBot", e);
            return null;
        }
    }

    /**
     * Получение ответа от бота.
     **/

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        final long startTime = System.currentTimeMillis();
        final WinCounter startWinCounter = new WinCounter(board, 0, null);
        final ForkJoinPool resultForkJoinPool = new ForkJoinPool();
        final Answer result = resultForkJoinPool.invoke(startWinCounter).answer;
        System.out.println("MultithreadedMinMax time: " + (System.currentTimeMillis() - startTime));
        return result;

    }

    /**
     * Метод находит в списке awList элемент с максимальным полем win.
     * Если поиск происходит среди ответов соперника, то на поле win вешается минус. Таким образом,
     * метод находит ответ с максимальной ценностью, если ходит агент, и ответ с минимальной (для агента)
     * ценностью, если ходит соперник.
     **/

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList,
                                           final ToDoubleFunction<AnswerAndWin> winCalculator) {
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = winCalculator.applyAsDouble(bestAW);
        for (int i = 1; i < awList.size(); i++) {
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            if (currentWin > bestWin) {
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
            return -100000d;
        }
        if (board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }

}
