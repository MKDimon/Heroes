package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.player.Answer;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Дерево симуляции на один шаг вперед
 * Ничего не может, может только дать лучший шаг
 */
public class SimulationOneStep extends SimulationTree {
    private final Logger logger = LoggerFactory.getLogger(SimulationOneStep.class);

    public SimulationOneStep(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        super(func, field, maxHeight, clustering);
    }

    @Override
    public Answer getAnswer(final Board board) {
        final Node root = new Node(board, null, Double.MIN_VALUE);
        super.setUnitAccuracy(board);
        Node maxNode = root;
        try {
            root.list.addAll(getAllSteps(root, field));
            maxNode = root.list.get(0);
            for (final Node item : root.list) {
                maxNode = (Double.compare(maxNode.value, func.getValue(item.board, field)) < 0) ? item : maxNode;
            }
        } catch (final UnitException | GameLogicException | BoardException e) {
            logger.error("Error get answer simulation", e);
        }
        return maxNode.answer;
    }

}
