package heroes;

import com.fasterxml.jackson.databind.ObjectMapper;
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
