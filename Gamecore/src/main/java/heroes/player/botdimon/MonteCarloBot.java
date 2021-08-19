package heroes.player.botdimon;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.ClientsConfigs;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
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
public class MonteCarloBot extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(MonteCarloBot.class);

    public MonteCarloBot(final Fields field) throws GameLogicException {
        super(field);
    }

    public static class MonteCarloFactory extends BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new MonteCarloBot(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new MonteCarloBot(fields);
        }
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            final SimulationTreeArmy tree = new SimulationTreesArmyMap().getTree(
                    SimulationTreesArmy.SIMPLE_SIMULATION
            );

            if (getField() == Fields.PLAYER_ONE) {
                return tree.getArmyConst(0);
            }
            else {
                return tree.getArmyConst(0);
            }

        } catch (BoardException | UnitException e) {
            logger.error("Error creating army in bot", e);
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) {
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                SimulationTrees.EXPECTI_SIMULATION,
                UtilityFuncMap.getFunc(Functions.EXPONENT_FUNCTION_V2),
                getField(), 1, false
        );
        final Answer answer = tree.getAnswer(board);
        final long finish = System.currentTimeMillis();
        return answer;
    }
}
