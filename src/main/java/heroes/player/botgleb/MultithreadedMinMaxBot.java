package heroes.player.botgleb;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.gui.Visualisable;
import heroes.player.Answer;
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

public class MultithreadedMinMaxBot extends SimpleMinMaxBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(MultithreadedMinMaxBot.class);

    /**
     * Фабрика ботов.
     **/

    public static class MultithreadedMinMaxBotFactory extends AIBotFactory {
        @Override
        public MultithreadedMinMaxBot createBot(final Fields fields) throws GameLogicException {
            return new MultithreadedMinMaxBot(fields);
        }

        @Override
        public MultithreadedMinMaxBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                                  final int maxRecLevel) throws GameLogicException {
            return new MultithreadedMinMaxBot(fields, utilityFunction, maxRecLevel);
        }
    }

    /**
     * Класс "распараллеливвтель" задач. Метод compute() строит дерево игры.
     **/

    private final class MinMaxWinCounter extends RecursiveTask<AnswerAndWin> {
        protected final Board implBoard;
        protected final int recLevel;
        protected final Answer rootAnswer;

        protected MinMaxWinCounter(final Board implBoard, final int recLevel, final Answer rootAnswer) {
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
                    winCalculator = AnswerAndWin::win;
                } else {
                    winCalculator = aw -> -aw.win();
                }
                // Если состояние терминальное, и победил агент, то возвращает +большое число,
                // если победил соперник, возвращает -большое число.
                if (implBoard.getStatus() != GameStatus.GAME_PROCESS) {
                    return new AnswerAndWin(rootAnswer, getTerminalStateValue(implBoard));
                }
                if (recLevel >= getMaxRecLevel()) {
                    // функция полезности вычисляется для агента.
                    // Показывает, насколько поелзно будет ему это действие
                    return new AnswerAndWin(rootAnswer, getUtilityFunction().compute(implBoard, getField()));
                }
                // Если состояние не терминальное, и не достигнут максимлаьынй уровень рекурсии,
                // то начинаем строить дерево из текущего состояния.
                final List<Answer> actions = implBoard.getPossibleMoves();
                final List<AnswerAndWin> awList = new ArrayList<>();
                final List<MinMaxWinCounter> subTasks = new LinkedList<>();
                for (final Answer answer : actions) {
                    final MinMaxWinCounter task = new MinMaxWinCounter(implBoard.copy(answer), recLevel + 1, answer);
                    task.fork();
                    subTasks.add(task);
                }
                for (final MinMaxWinCounter subTask : subTasks) {
                    final AnswerAndWin aw = subTask.join();
                    awList.add(aw);
                }

                // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому
                // корневому ответу сопоставляется значение из нижнего состяния.
                if (rootAnswer != null) {
                    return new AnswerAndWin(rootAnswer, getGreedyDecision(awList, winCalculator).win());
                } else {
                    return getGreedyDecision(awList, winCalculator);
                }
            } catch (GameLogicException e) {
                logger.error("Error computing tree by MultithreadedMinMaxBot", e);
                return null;
            }
        }
    }

    public MultithreadedMinMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public MultithreadedMinMaxBot(final Fields fields, final UtilityFunction utilityFunction,
                                  final int maxRecLevel) throws GameLogicException {
        super(fields, utilityFunction, maxRecLevel);
    }

    /**
     * Получение ответа от бота.
     **/

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        final long startTime = System.currentTimeMillis();
        final MinMaxWinCounter startWinCounter = new MinMaxWinCounter(board, 0, null);
        final ForkJoinPool resultForkJoinPool = new ForkJoinPool();
        final Answer result = resultForkJoinPool.invoke(startWinCounter).answer();
        System.out.println("MultithreadedMinMax time: " + (System.currentTimeMillis() - startTime));
        return result;

    }

}
