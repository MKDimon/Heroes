package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;

import java.io.IOException;
import java.io.StringWriter;

public class Serializer {
    public static String serializeData(Data data) throws IOException {
        StringWriter writer = new StringWriter();
        new ObjectMapper().writeValue(writer, data);
        return writer.toString();
    }
}