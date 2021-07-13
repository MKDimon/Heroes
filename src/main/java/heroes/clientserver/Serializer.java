package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;

import java.io.IOException;
import java.io.StringWriter;

public class Serializer {
    public static String serializeBoard(Board board) throws IOException {
        StringWriter writer = new StringWriter();
        new ObjectMapper().writeValue(writer, board);
        return writer.toString();
    }

    public static String serializeAnswer(final Answer answer) throws IOException {
        StringWriter writer = new StringWriter();
        new ObjectMapper().writeValue(writer, answer);
        return writer.toString();
    }

    public static String serializeArmy(Army army) throws IOException {
        StringWriter writer = new StringWriter();
        new ObjectMapper().writeValue(writer, army);
        return writer.toString();
    }
}