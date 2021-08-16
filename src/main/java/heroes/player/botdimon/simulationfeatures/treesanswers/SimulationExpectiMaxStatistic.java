package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.player.Answer;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;

import java.util.List;

/**
 * Дерево симуляции заданной высоты
 */
public class SimulationExpectiMaxStatistic extends SimulationTree {
    public SimulationExpectiMaxStatistic(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        super(func, field, maxHeight, clustering);
    }

    @Override
    public Answer getAnswer(final Board board) {
        final Node root = new Node(board, null, Double.MIN_VALUE);
        super.setUnitAccuracy(board);
        try {
            root.list.addAll(super.getAllSteps(root, board.getCurrentPlayer()));
            for (final Node item : root.list) {
                item.value = getWinByGameTree(item.board, 1);
            }
        } catch (final Exception e) {
            logger.error("Error change branch", e);
        }
        final Node node = getGreedyDecision(root.list, field);
        UtilityFuncStatistic.record(node.board, node.value, field);
        final Answer answer = node.answer;
        logger.info("Attacker position = {}, defender position = {}, action type = {}",
                answer.getAttacker(), answer.getDefender(), answer.getActionType());
        return answer;
    }

    /**
     * Выдает лучшее значение из узлов и их потомков
     *
     * @param board статус игры узла
     * @param curHeight текущая глубина дерева
     * @return лучшее значение в узле и потомков
     * @throws GameLogicException ошибка
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     */
    private double getWinByGameTree(final Board board, final int curHeight)
            throws GameLogicException, UnitException, BoardException {
        final Node root = new Node(board, null, 0);

        if (board.getStatus() != GameStatus.GAME_PROCESS || curHeight >= maxHeight) {
            return func.getValue(board, field);
        }

        root.list.addAll(getAllSteps(root, board.getCurrentPlayer()));

        for (final Node item : root.list) {
            item.value = getWinByGameTree(item.board, curHeight + 1);
        }
        return getGreedyDecision(root.list, board.getCurrentPlayer()).value;
    }


    /**
     * @param list список вершин
     * @param field поле игрока
     * @return узел с ответом и максимальным значением, если оценивался ход своего поля
     * узел с усредненным значением, если оценивался ход противника
     */
    private Node getGreedyDecision(final List<Node> list, final Fields field) {
        Node maxValue = list.get(0);
        if (field == super.field) {
            for (final Node item : list) {
                maxValue = (Double.compare(maxValue.value, item.value) < 0) ? item : maxValue;
            }
        }
        else {
            maxValue = new Node(null, null, 0);
            for (final Node item : list) {
                maxValue.value += item.value;
            }
            maxValue.value /= list.size();
        }
        return maxValue;
    }
}
