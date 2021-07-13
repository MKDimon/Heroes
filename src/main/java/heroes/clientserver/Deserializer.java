package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Deserializer {
    private static Logger logger = LoggerFactory.getLogger(Deserializer.class);

    public static Board deserializeBoard(String jsonBoard) throws IOException {
            return new ObjectMapper().readValue(jsonBoard, Board.class);
    }

    public static Answer deserializeAnswer(final String jsonAnswer) throws IOException {
        return new ObjectMapper().readValue(jsonAnswer, Answer.class);
    }

    public static Army deserializeArmy(String jsonArmy) throws IOException {
        if (jsonArmy == null) throw new NullPointerException("NULL");
        return new ObjectMapper().readValue(jsonArmy, Army.class);
    }
}
