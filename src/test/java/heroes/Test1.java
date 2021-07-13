package heroes;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class Test1 {
    @Test
    public void test() throws UnitException, GameLogicException, IOException, BoardException {
        ObjectMapper mapper = new ObjectMapper();

        StringWriter writer = new StringWriter();
        General firstGeneral = new General(GeneralTypes.COMMANDER);
        Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.HEALER), firstGeneral, new Unit(UnitTypes.MAGE)}
        };
        mapper.writeValue(writer, new Army(firstArmy, firstGeneral));
        System.out.println(writer.toString());

        Army army = mapper.readValue(writer.toString(), Army.class);
    }
}
