package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;

import java.io.IOException;

public class Deserializer {

    public static Board deserializeBoard(final String jsonBoard) throws IOException {
            return new ObjectMapper().readValue(jsonBoard, Board.class);
    }

    public static Answer deserializeAnswer(final String jsonAnswer) throws IOException {
        return new ObjectMapper().readValue(jsonAnswer, Answer.class);
    }

    public static Army deserializeArmy(final String jsonArmy) throws IOException {
        return new ObjectMapper().readValue(jsonArmy, Army.class);
    }
}
