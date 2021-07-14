package heroes;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
public class Test1 {


    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        FileOutputStream fileOutputStream = new FileOutputStream("src/test/java/heroes/config.json");

    }
}
