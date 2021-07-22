package heroes.clientserver;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationDeserializationTest {

    @Test
    public void testSDAnswer1() throws GameLogicException, IOException {
        final Answer anw1 = new Answer(new Position(1,1, Fields.PLAYER_ONE),
                new Position(1,1 , Fields.PLAYER_ONE), ActionTypes.HEALING);
        final String jsonAnw1 = Serializer.serializeData(new Data(anw1));
        assertEquals("{\"command\":null,\"board\":null,\"answer\":{\"attacker\":{\"x\":1,\"y\":1,\"f\":\"PLAYER_ONE\"},\"defender\":" +
                "{\"x\":1,\"y\":1,\"f\":\"PLAYER_ONE\"},\"actionType\":\"HEALING\"},\"army\":null}", jsonAnw1);
        final Answer anw2 = Deserializer.deserializeData(jsonAnw1).answer;
        assertEquals(anw1, anw2);
    }

    @Test
    public void testSDAnswer2() throws GameLogicException, IOException {
        final Answer anw1 = new Answer(new Position(0,1, Fields.PLAYER_ONE),
                new Position(0,1, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT);
        final String jsonAnw1 = Serializer.serializeData(new Data(anw1));
        assertEquals("{\"command\":null,\"board\":null,\"answer\":{\"attacker\":{\"x\":0,\"y\":1,\"f\":\"PLAYER_ONE\"},\"defender\":" +
                "{\"x\":0,\"y\":1,\"f\":\"PLAYER_TWO\"},\"actionType\":\"CLOSE_COMBAT\"},\"army\":null}", jsonAnw1);
        final Answer anw2 = Deserializer.deserializeData(jsonAnw1).answer;
        assertEquals(anw1, anw2);
    }

    @Test
    public void testSDBoard() throws BoardException, UnitException, IOException {
        final General firstGeneral = new General(GeneralTypes.ARCHMAGE);
        final General secondGeneral = new General(GeneralTypes.COMMANDER);
        final Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), firstGeneral, new Unit(UnitTypes.HEALER)}
        };
        final Unit[][] secondArmy = new Unit[][]{
                {new Unit(UnitTypes.MAGE), new Unit(UnitTypes.MAGE), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), secondGeneral, new Unit(UnitTypes.HEALER)}
        };
        final Board board = new Board(new Army(firstArmy, firstGeneral), new Army(secondArmy, secondGeneral));
        board.deinspireArmy(firstArmy);
        board.deinspireArmy(secondArmy);
        final Board board1 = Deserializer.deserializeData(Serializer.serializeData(
                new Data(null, null, board, null))
        ).board;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 3; j++){
                assertEquals(board.getArmy(Fields.PLAYER_ONE)[i][j], board1.getArmy(Fields.PLAYER_ONE)[i][j]);
                assertEquals(board.getArmy(Fields.PLAYER_TWO)[i][j], board1.getArmy(Fields.PLAYER_TWO)[i][j]);
            }
        }
    }

    @Test
    public void testSDArmy() throws UnitException, BoardException, IOException {
        final General general = new General(GeneralTypes.ARCHMAGE);
        final Unit[][] units = new Unit[][]{
                {new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), general, new Unit(UnitTypes.HEALER)}
        };
        final Army army = new Army(units, general);
        army.getPlayerUnits()[0][0].setArmor(55);
        army.getPlayerUnits()[0][1].setAccuracy(22);
        army.getPlayerUnits()[0][2].setCurrentHP(-4);
        army.getPlayerUnits()[1][0].setPower(11);
        army.getPlayerUnits()[1][1].setActive(false);
        army.getPlayerUnits()[1][2].setActive(false);
        final Army army1 = Deserializer.deserializeData(Serializer.serializeData(new Data(null, army))).army;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 3; j++){
                assertEquals(army.getPlayerUnits()[i][j], army.getPlayerUnits()[i][j]);
            }
        }
        assertAll(
                ()-> assertFalse(army1.getPlayerUnits()[0][2].isAlive()),
                ()-> assertFalse(army1.getPlayerUnits()[0][2].isActive()),
                ()-> assertFalse(army1.getPlayerUnits()[1][1].isActive()),
                ()-> assertEquals(army1.getPlayerUnits()[1][1], general)
        );
    }
}
