package heroes.player.botdimon.simulationfeatures.treesarmies;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;

import java.util.ArrayList;
import java.util.List;

public abstract class SimulationTreeArmy {
    private final List<Army> armies = new ArrayList<>();

    /**
     * Убирает невостребованные армии из списка
     */
    protected void validateArmy() {

    }

    public Army getArmyConst() throws UnitException, BoardException {
        General general = new General(GeneralTypes.ARCHMAGE);
        Unit[][] army = new Unit[2][3];
        army[0][0] = new Unit(UnitTypes.SWORDSMAN);
        army[0][1] = new Unit(UnitTypes.BOWMAN);
        army[0][2] = new Unit(UnitTypes.SWORDSMAN);
        army[1][0] = new Unit(UnitTypes.MAGE);
        army[1][1] = general;
        army[1][2] = new Unit(UnitTypes.HEALER);/*
        General general = new General(GeneralTypes.SNIPER);
        Unit[][] army = new Unit[2][3];

        army[0][0] = new Unit(UnitTypes.BOWMAN);
        army[0][1] = new Unit(UnitTypes.SWORDSMAN);
        army[0][2] = new Unit(UnitTypes.SWORDSMAN);
        army[1][0] = new Unit(UnitTypes.HEALER);
        army[1][1] = new Unit(UnitTypes.BOWMAN);
        army[1][2] = general;*/

        return new Army(army, general);
    }

    public abstract Army getArmyByArmy(final Army army);
}
