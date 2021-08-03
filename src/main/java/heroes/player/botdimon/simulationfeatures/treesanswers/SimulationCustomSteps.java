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
 * Minimax алгоритм
 * Альфа-бета отсечения
 */
public class SimulationCustomSteps extends SimulationTree {
    public SimulationCustomSteps(final IUtilityFunc func, final Fields field, final int maxHeight) {
        super(func, field, maxHeight);
    }

    @Override
    public Answer getAnswer(final Board board) {
        final Node root = new Node(board, null, Double.MIN_VALUE);
        super.setUnitAccuracy(board);
        try {
            root.list.addAll(super.getAllSteps(root, board.getCurrentPlayer()));
            for (final Node item : root.list) {
                item.value = getWinByGameTree(item.board, Double.MIN_VALUE, Double.MAX_VALUE, 1);
            }
        } catch (final UnitException | GameLogicException | BoardException e) {
            logger.error("Error change branch", e);
        }
        final Node node = getGreedyDecision(root.list, field);
        final Answer answer = node.answer;
        logger.info("Value: " + node.value);
        logger.info("Attacker position = {}, defender position = {}, action type = {}",
                answer.getAttacker(), answer.getDefender(), answer.getActionType());
        return answer;
    }

    /**
     * Выдает лучшее значение из узлов и их потомков
     *
     * @param board статус игры узла
     * @param alpha альфа/бета отсечения
     * @param beta  альфа/бета отсечения
     * @param curHeight текущая глубина дерева
     * @return лучшее значение в узле и потомков
     * @throws GameLogicException ошибка
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     */
    private double getWinByGameTree(final Board board, double alpha, double beta, final int curHeight)
            throws GameLogicException, UnitException, BoardException {
        final Node root = new Node(board, null, 0);

        if (board.getStatus() != GameStatus.GAME_PROCESS || curHeight >= maxHeight) {
            return func.getValue(board, field);
        }

        root.list.addAll(getAllSteps(root, board.getCurrentPlayer()));

        for (final Node item : root.list) {
            item.value = getWinByGameTree(item.board, alpha, beta, curHeight + 1);
            if (board.getCurrentPlayer() == field) {
                if (item.value >= beta) { return item.value; }
                alpha = Math.max(item.value, alpha);
            }
            else {
                if (item.value <= alpha) { return item.value; }
                beta = Math.min(item.value, beta);
            }
        }
        return getGreedyDecision(root.list, board.getCurrentPlayer()).value;
    }


    /**
     * @param list список вершин
     * @param field поле игрока
     * @return максимальное/минимальное значение по алгоритму Minimax
     */
    private Node getGreedyDecision(final List<Node> list, final Fields field) {
        Node maxValue = list.get(0);
        for (final Node item : list) {
            if (field == super.field)
                maxValue = (Double.compare(maxValue.value,item.value) < 0) ? item : maxValue;
            else
                maxValue = (Double.compare(maxValue.value,item.value) > 0) ? item : maxValue;
        }
        return maxValue;
    }
}
