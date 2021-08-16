package heroes.player.botgleb;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
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
 * Бот по стратегии "ExpectiMax" с использованием ForkJoinPull.
 **/

public class MultithreadedExpectiMaxBot extends ExpectiMaxBot {
    private static final Logger logger = LoggerFactory.getLogger(MultithreadedExpectiMaxBot.class);

    /**
     * Фабрика ботов.
     **/

    public static class MultithreadedExpectiMaxBotFactory extends AIBotFactory {
        @Override
        public MultithreadedExpectiMaxBot createBot(final Fields fields) throws GameLogicException {
            return new MultithreadedExpectiMaxBot(fields);
        }

        @Override
        public MultithreadedExpectiMaxBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                                      final int maxRecLevel) throws GameLogicException {
            return new MultithreadedExpectiMaxBot(fields, utilityFunction, maxRecLevel);
        }
    }

    /**
     * Класс "распараллеливвтель" задач. Метод compute() строит дерево игры.
     **/

    private final class ExpectiMaxWinCounter extends RecursiveTask<AnswerAndWin> {

        private final Board implBoard;
        private final int recLevel;
        private final Answer rootAnswer;

        private ExpectiMaxWinCounter(final Board implBoard, final int recLevel, final Answer rootAnswer) {
            this.implBoard = implBoard;

            this.recLevel = recLevel;
            this.rootAnswer = rootAnswer;
        }

        @Override
        protected AnswerAndWin compute() {
            try {
                final boolean isMax = implBoard.getCurrentPlayer() == getField();
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
                final List<ExpectiMaxWinCounter> subTasks = new LinkedList<>();
                for (final Answer answer : actions) {
                    final ExpectiMaxWinCounter task = new ExpectiMaxWinCounter(implBoard.copy(answer), recLevel + 1, answer);
                    task.fork();
                    subTasks.add(task);
                }
                for (final ExpectiMaxWinCounter subTask : subTasks) {
                    final AnswerAndWin aw = subTask.join();
                    awList.add(aw);
                }

                // Пробрасывает на верхний уровень, вплоть до метода getAnswer, где каждому
                // корневому ответу сопоставляется значение из нижнего состяния.
                if (rootAnswer != null) {
                    return new AnswerAndWin(rootAnswer, getGreedyDecision(awList, isMax,
                            aw -> aw.win() / awList.size()).win());
                } else {
                    return getMaxAW(awList);
                }
            } catch (GameLogicException e) {
                logger.error("Error computing tree by MultithreadedExpectiMaxBot", e);
                return null;
            }
        }
    }

    public MultithreadedExpectiMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public MultithreadedExpectiMaxBot(final Fields fields, final UtilityFunction utilityFunction,
                                      final int maxRecLevel) throws GameLogicException {
        super(fields, utilityFunction, maxRecLevel);
    }

    /**
     * Получение ответа от бота.
     **/

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        final long startTime = System.currentTimeMillis();
        final ExpectiMaxWinCounter startWinCounter = new ExpectiMaxWinCounter(board, 0, null);
        final ForkJoinPool resultForkJoinPool = new ForkJoinPool();
        final Answer result = resultForkJoinPool.invoke(startWinCounter).answer();
        System.out.println("MultiThreadedExpectiMax time: " + (System.currentTimeMillis() - startTime));
        return result;
    }

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList, final boolean isMax,
                                           final ToDoubleFunction<AnswerAndWin> probabilityFunction) {
        if (isMax) {
            return getMaxAW(awList);
        } else {
            return new AnswerAndWin(null, getChance(awList, probabilityFunction));
        }
    }

}
