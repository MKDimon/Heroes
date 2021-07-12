package heroes.clientserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Deserializer {
    private static Logger logger = LoggerFactory.getLogger(Deserializer.class);

    public static Board deserializeBoard(String jsonBoard) throws JsonProcessingException {
            return new ObjectMapper().readValue(jsonBoard, Board.class);
    }

    public static Answer deserializeAnswer(final String jsonAnswer) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonAnswer, Answer.class);
    }

    public static Army deserializeArmy(String jsonArmy) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonArmy, Army.class);
    }
}
