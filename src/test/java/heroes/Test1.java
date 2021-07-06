package heroes;

import static org.junit.jupiter.api.Assertions.*;

import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import heroes.units.auxiliaryclasses.UnitException;
import org.junit.Test;

import java.util.Arrays;

public class Test1 {
    @Test
    public void test() throws UnitException {
        assertAll(
                ()-> assertEquals(1,1),
                ()-> assertTrue(true)
        );
    }
}
