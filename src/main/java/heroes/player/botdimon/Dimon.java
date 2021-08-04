package heroes.player.botdimon;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.botdimon.simulationfeatures.functions.Functions;
import heroes.player.botdimon.simulationfeatures.functions.UtilityFuncMap;
import heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTree;
import heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTreeFactory;
import heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTrees;
import heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreeArmy;
import heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreesArmy;
import heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreesArmyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Умный бот Димон
 */
public class Dimon extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(Dimon.class);

    public Dimon(final Fields field) throws GameLogicException {
        super(field);
    }

    public static class DimonFactory extends BaseBot.BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new Dimon(fields);
        }
    }

    protected TerminalWrapper tw = null;

    @Override
    public void setTerminal(TerminalWrapper tw) {
        super.tw = tw;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            final SimulationTreeArmy tree = new SimulationTreesArmyMap().getTree(
                    SimulationTreesArmy.SIMPLE_SIMULATION
            );

            if (getField() == Fields.PLAYER_ONE) {
                return tree.getArmyConst();
            }
            else {
                return tree.getArmyByArmy(firstPlayerArmy);
            }

        } catch (BoardException | UnitException e) {
            logger.error("Error creating army in RandomBot", e);
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) {
        final long start = System.currentTimeMillis();
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                SimulationTrees.CUSTOM_STEP_SIMULATION,
                UtilityFuncMap.getFunc(Functions.EXPONENT_FUNCTION_V1),
                getField(), 3
        );
        final Answer answer = tree.getAnswer(board);
        final long finish = System.currentTimeMillis();
        System.out.println("TIME: " + (finish - start));
        return answer;
    }
}
