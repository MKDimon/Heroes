package heroes;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class Test1 {
    @Test
    public void test() throws UnitException, GameLogicException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        final StringWriter writer = new StringWriter();
        mapper.writeValue(writer, new Answer(new Position(1,2, Fields.PLAYER_ONE), new Position(1,2, Fields.PLAYER_ONE), ActionTypes.HEALING));
        System.out.println((writer));
    }
}
