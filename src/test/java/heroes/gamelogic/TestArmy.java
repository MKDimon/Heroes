package heroes.gamelogic;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
