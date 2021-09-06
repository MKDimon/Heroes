package heroes.player.botdimon.simulationfeatures.treesarmies;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationArmy extends SimulationTreeArmy {
    private static final Logger logger = LoggerFactory.getLogger(SimulationArmy.class);

    @Override
    public Army getArmyByArmy(final Army army) {
        try {
            return getArmyConst(0);
        } catch (final UnitException | BoardException e) {
            logger.error("Simulation army error", e);
        }
        return null;
    }
}
