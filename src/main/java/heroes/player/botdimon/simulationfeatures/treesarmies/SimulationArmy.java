package heroes.player.botdimon.simulationfeatures.treesarmies;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;

public class SimulationArmy extends SimulationTreeArmy {

    @Override
    public Army getArmyByArmy(Army army) {
        try {
            return getArmyConst(0);
        } catch (UnitException | BoardException e) {
            e.printStackTrace();
        }
        return null;
    }
}
