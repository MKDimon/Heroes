package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;

public class Deserializer {
    private static Logger logger = LoggerFactory.getLogger(Deserializer.class);

    public static Board deserializeBoard(final String jsonBoard) throws IOException {
            return new ObjectMapper().readValue(new StringReader(jsonBoard), Board.class);
    }

    public static Answer deserializeAnswer(final String jsonAnswer) throws IOException {
        return new ObjectMapper().readValue(new StringReader(jsonAnswer), Answer.class);
    }

    public static Army deserializeArmy(final String jsonArmy) throws IOException {
        return new ObjectMapper().readValue(new StringReader(jsonArmy), Army.class);
    }
}
