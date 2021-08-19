package heroes.player.botgleb;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.ClientsConfigs;
import heroes.gamelogic.*;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Бот по стратегии "Монте-Карло".
 **/

public class MonteCarloBot extends BaseBot implements Visualisable {
    private static final int iterations = 50;
    private static final int maxTreeHeight = 1;

    private static final Logger logger = LoggerFactory.getLogger(SimpleMinMaxBot.class);

    /**
     * Фабрика ботов.
     **/

    public static class MonteCarloBotFactory extends BaseBotFactory {
        @Override
        public MonteCarloBot createBot(final Fields fields) throws GameLogicException {
            return new MonteCarloBot(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new MonteCarloBot(fields);
        }
    }

    /**
     * Вспомогательный класс. Хранит ответ и количество побед/всех игр,
     * проведенных в симуляциях в данном или нижних узлах.
     **/

    private static final class WinsAndAllGames {
        private int wins;
        private int allGames;
        private final Answer answer;

        private WinsAndAllGames(final int wins, final int allGames, final Answer answer) {
            this.wins = wins;
            this.allGames = allGames;
            this.answer = answer;
        }

        private WinsAndAllGames uniteStats(final WinsAndAllGames winsAndAllGames) {
            wins += winsAndAllGames.wins;
            allGames += winsAndAllGames.allGames;
            return this;
        }

        private double getWin() {
            return (double) wins / allGames;
        }
    }

    /**
     * Класс "распараллеливатель" задач. Рекурсивно строит дерево игры и проводит симуляции в нижних узлах.
     **/

    private final class Node extends RecursiveTask<WinsAndAllGames> {
        private final Answer rootAnswer;
        private final WinsAndAllGames win;
        private final Board board;
        private final int treeHeight;

        private Node(final Answer rootAnswer, final Board board, final int treeHeight) {
            this.rootAnswer = rootAnswer;
            win = new WinsAndAllGames(0, 0, rootAnswer);
            this.board = board;
            this.treeHeight = treeHeight;
        }

        @Override
        protected WinsAndAllGames compute() {
            try {
                // Если достигнута максимальаня глубина дерева, проводим симуляцию для оценки узла.
                if (treeHeight >= maxTreeHeight) {
                    return new WinsAndAllGames(simulateGame(board), iterations, rootAnswer);
                }
                // Если наткнулись на терминальный узел, то проводим его оценку
                // (0, если узел проигрышный и 1000, если узел выйгрышный для агента).
                if (board.getStatus() != GameStatus.GAME_PROCESS) {
                    return new WinsAndAllGames(getTerminalStateValue(board), 1, rootAnswer);
                }
                // Если узел нетерминальный, продолжаем строить дерево в глубину и в статистику родительского узла
                // добавляем статистику из нижних узлов. (back propagation)
                final List<Node> subtasks = new LinkedList<>();
                final List<Answer> actions = board.getPossibleMoves();
                final List<WinsAndAllGames> wgList = new ArrayList<>();
                for (final Answer answer : actions) {
                    final Node task = new Node(answer, board.copy(answer), treeHeight + 1);
                    task.fork();
                    subtasks.add(task);
                }
                for (final Node task : subtasks) {
                    final WinsAndAllGames wg = task.join();
                    wgList.add(wg);
                    this.win.uniteStats(wg);
                }
                if (rootAnswer != null) {
                    return this.win;
                } else {
                    return getGreedyDecision(wgList);
                }

            } catch (final UnitException | BoardException | GameLogicException e) {
                logger.error("Error Monte-Carlo bot tree searching", e);
                return null;
            }
        }
    }

    public MonteCarloBot(final Fields field) throws GameLogicException {
        super(field);
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
        final Node root = new Node(null, board, 0);
        final ForkJoinPool task = new ForkJoinPool();
        final Answer result = task.invoke(root).answer;
        System.out.println("Monte-Carlo time: " + (System.currentTimeMillis() - startTime));
        return result;
    }

    /**
     * Метод находит ответ с наилучшей статистикой победы/все игры.
     **/

    private WinsAndAllGames getGreedyDecision(final List<WinsAndAllGames> wgList) {
        WinsAndAllGames bestWG = wgList.get(0);
        double bestWin = bestWG.getWin();
        for (int i = 1; i < wgList.size(); i++) {
            final WinsAndAllGames curWG = wgList.get(i);
            final double curWin = curWG.getWin();
            if (curWin > bestWin) {
                bestWG = curWG;
                bestWin = curWin;
            }
        }
        return bestWG;
    }


    /**
     * Симуляция игры из состояния board`ы до конца. Игра проводится iterations раз.
     * Возвращает количество побед.
     **/

    private int simulateGame(final Board board) throws BoardException, UnitException, GameLogicException {
        if (board.getStatus() != GameStatus.GAME_PROCESS) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < iterations; i++) {
            final Fields enemyField = getField() == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;
            final BaseBot botPlayer = new RandomBot(getField());
            final BaseBot botEnemy = new RandomBot(enemyField);
            final Map<Fields, BaseBot> getPlayer = Map.of(
                    getField(), botPlayer,
                    enemyField, botEnemy
            );
            final GameLogic simulationGL = new GameLogic(board);
            while (simulationGL.isGameBegun()) {
                final Answer answer = getPlayer.get(simulationGL.getBoard().getCurrentPlayer()).
                        getAnswer(simulationGL.getBoard());
                simulationGL.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            }
            result += getTerminalStateValue(simulationGL.getBoard());
        }
        return result;
    }


    /**
     * Метод вычисляет тип терминального состояния. Возвращает 1000, если победил игрок,
     * 0 - если ничья, или игрок проиграл.
     **/

    private int getTerminalStateValue(final Board board) throws GameLogicException {
        if (board.getStatus() == GameStatus.GAME_PROCESS) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        if (board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return 1000;
        } else {
            return 0;
        }
    }

}