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
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Умный бот для проверки Димона
 */
public class AntiDimon extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(AntiDimon.class);

    private final int maxHeight = 3;

    public AntiDimon(final Fields field) throws GameLogicException {
        super(field);
    }

    public static class AntiDimonFactory extends BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new AntiDimon(fields);
        }

        @Override
        public BaseBot createBotWithConfigs(Fields fields, ClientsConfigs clientsConfigs) throws GameLogicException {
            return new AntiDimon(fields);
        }
    }

    private int getMaxHeight(final Unit[][] army) {
        return ((int) Board.activeCount(army) <= 3)? 2 : 0;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        return null;
    }

    @Override
    public Answer getAnswer(final Board board) {
        final long start = System.currentTimeMillis();
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                SimulationTrees.EXPECTI_SIMULATION,
                UtilityFuncMap.getFunc(Functions.MONTE_CARLO),
                getField(), 1, true
        );
        final Answer answer = tree.getAnswer(board);
        final long finish = System.currentTimeMillis();
        System.out.println("ANTI TIME: " + (finish - start));
        return answer;
    }
}
