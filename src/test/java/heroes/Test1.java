package heroes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

public class Test1 {
    @Test
    public void test(){
        assertAll(
                ()-> assertEquals(1,1),
                ()-> assertTrue(true)
        );
    }
}
