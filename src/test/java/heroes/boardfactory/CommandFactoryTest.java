package heroes.boardfactory;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandFactoryTest {
    @Test
    public void test() {
        assertAll(
                ()-> assertEquals(1,1),
                ()-> assertTrue(true)
        );
    }
}
