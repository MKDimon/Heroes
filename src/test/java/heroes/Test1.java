package heroes;

import static org.junit.jupiter.api.Assertions.*;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

public class Test1 {
    @Test
    public void test() throws UnitException {
        Unit[][] units = new Unit[1][1];

        Unit unit = new Unit(UnitTypes.SWORDSMAN);

        units[0][0] = unit;

        Unit[][] unitTest = {units[0].clone()};
        assertSame(units[0][0], unitTest[0][0]);
        unitTest[0][0] = new Unit(UnitTypes.MAGE);
        assertNotSame(units[0][0], unitTest[0][0]);
    }
}
