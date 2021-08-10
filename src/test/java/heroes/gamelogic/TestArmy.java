package heroes.gamelogic;

import gamecore.gamelogic.Army;
import gamecore.units.General;
import gamecore.units.GeneralTypes;
import gamecore.units.Unit;
import gamecore.units.UnitTypes;
import gamecore.auxiliaryclasses.boardexception.BoardException;
import gamecore.auxiliaryclasses.unitexception.UnitException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestArmy {
    @Test
    public void testArmyCopy() throws UnitException, BoardException {
        final General firstGeneral = new General(GeneralTypes.COMMANDER);
        final Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.HEALER), firstGeneral, new Unit(UnitTypes.MAGE)}
        };

        final Army army = new Army(firstArmy, firstGeneral);
        assertEquals(army, new Army(army));
    }
}
