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
import heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTree;
import heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTreeFactory;
import heroes.player.botdimon.simulationfeatures.treesarmies.SimulationTrees;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Умный бот для проверки Димона
 */
public class AntiDimon extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(AntiDimon.class);

    public AntiDimon(final Fields field) throws GameLogicException {
        super(field);
    }

    public static class AntiDimonFactory extends BaseBotFactory {
        @Override
        public BaseBot createBot(final Fields fields) throws GameLogicException {
            return new AntiDimon(fields);
        }
    }

    protected TerminalWrapper tw = null;

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.tw = tw;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            General general = new General(GeneralTypes.ARCHMAGE);
            Unit[][] army = new Unit[2][3];
            army[0][0] = new Unit(UnitTypes.SWORDSMAN);
            army[1][0] = new Unit(UnitTypes.MAGE);
            army[0][1] = new Unit(UnitTypes.BOWMAN);
            army[1][1] = general;
            army[0][2] = new Unit(UnitTypes.SWORDSMAN);
            army[1][2] = new Unit(UnitTypes.HEALER);

            return new Army(army, general);
        } catch (BoardException | UnitException e) {
            logger.error("Error creating army in RandomBot", e);
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) {
        final SimulationTree tree = new SimulationTreeFactory().createSimulation(
                SimulationTrees.CUSTOM_STEP_SIMULATION,
                UtilityFuncMap.getFunc(Functions.EXPONENT_FUNCTION),
                getField(), 3
        );
        return tree.getAnswer(board);
    }
}
