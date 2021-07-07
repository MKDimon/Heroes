package heroes;

import static org.junit.jupiter.api.Assertions.*;

import heroes.auxiliaryclasses.unitexception.UnitException;
import org.junit.Test;

public class Test1 {
    @Test
    public void test() throws UnitException {
        assertAll(
                ()-> assertEquals(1,1),
                ()-> assertTrue(true)
        );
    }
}
