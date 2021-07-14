package heroes.gamelogic;

import static org.junit.jupiter.api.Assertions.*;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

public class TestArmy {
    @Test
    public void testArmyCopy() throws UnitException, BoardException {
        General firstGeneral = new General(GeneralTypes.COMMANDER);
        Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.HEALER), firstGeneral, new Unit(UnitTypes.MAGE)}
        };

        Army army = new Army(firstArmy, firstGeneral);
        assertEquals(army, new Army(army));
    }
}
