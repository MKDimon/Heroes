package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.player.Answer;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Дерево ну типа работает
 */
public class SimulationCustomStepsWithThread extends SimulationTree {
    public SimulationCustomStepsWithThread(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        super(func, field, maxHeight, clustering);
    }

    /**
     * Поточный узел для ForkJoinPool
     */
    private class ThreadNode extends RecursiveTask<Double> {
        public final Node node;
        private final int curHeight;

        public ThreadNode(final Node node, final int curHeight) {
            this.node = node;
            this.curHeight = curHeight;
        }

        @Override
        protected Double compute() {
            final Node root = node;

            if (root.board.getStatus() != GameStatus.GAME_PROCESS || curHeight >= maxHeight) {
                return func.getValue(root.board, field);
            }

            try {
                root.list.addAll(getAllSteps(root, root.board.getCurrentPlayer()));
                List<ForkJoinTask<Double>> list = new LinkedList<>();
                for (final Node item : root.list) {
                    ForkJoinTask<Double> node = new ThreadNode(item, curHeight + 1);
                    list.add(node.fork());
                }
                for (int i = 0; i < list.size(); i++) {
                    root.list.get(i).value = list.get(i).join();
                }
            } catch (UnitException | BoardException | GameLogicException e) {
                e.printStackTrace();
            }
            return getGreedyDecision(root.list, root.board.getCurrentPlayer()).value;

        }
    }

    @Override
    public Answer getAnswer(final Board board) {
        final Node root = new Node(board, null, 0);
        super.setUnitAccuracy(board);

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new ThreadNode(root, 0));

        final Node node = getGreedyDecision(root.list, field);
        final Answer answer = node.answer;
        logger.info("Value: " + node.value);
        logger.info("Attacker position = {}, defender position = {}, action type = {}",
                answer.getAttacker(), answer.getDefender(), answer.getActionType());
        return answer;
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
