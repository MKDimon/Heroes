package heroes.clientserver;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
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
        Answer anw1 = new Answer(new Position(1,1, Fields.PLAYER_ONE),
                new Position(1,1 , Fields.PLAYER_ONE), ActionTypes.HEALING);
        String jsonAnw1 = Serializer.serializeAnswer(anw1);
        assertEquals("{\"attacker\":{\"x\":1,\"y\":1,\"f\":\"PLAYER_ONE\"},\"defender\":" +
                "{\"x\":1,\"y\":1,\"f\":\"PLAYER_ONE\"},\"actionType\":\"HEALING\"}", jsonAnw1);
        Answer anw2 = Deserializer.deserializeAnswer(jsonAnw1);
        assertEquals(anw1, anw2);
    }

    @Test
    public void testSDAnswer2() throws GameLogicException, IOException {
        Answer anw1 = new Answer(new Position(0,1, Fields.PLAYER_ONE),
                new Position(0,1, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT);
        String jsonAnw1 = Serializer.serializeAnswer(anw1);
        assertEquals("{\"attacker\":{\"x\":0,\"y\":1,\"f\":\"PLAYER_ONE\"},\"defender\":" +
                "{\"x\":0,\"y\":1,\"f\":\"PLAYER_TWO\"},\"actionType\":\"CLOSE_COMBAT\"}", jsonAnw1);
        Answer anw2 = Deserializer.deserializeAnswer(jsonAnw1);
        assertEquals(anw1, anw2);
    }

    @Test
    public void testSDBoard() throws BoardException, UnitException, IOException {
        General firstGeneral = new General(GeneralTypes.ARCHMAGE);
        General secondGeneral = new General(GeneralTypes.COMMANDER);
        Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), firstGeneral, new Unit(UnitTypes.HEALER)}
        };
        Unit[][] secondArmy = new Unit[][]{
                {new Unit(UnitTypes.MAGE), new Unit(UnitTypes.MAGE), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), secondGeneral, new Unit(UnitTypes.HEALER)}
        };
        Board board = new Board(new Army(firstArmy, firstGeneral), new Army(secondArmy, secondGeneral));
        board.deinspireArmy(firstArmy, firstGeneral);
        board.deinspireArmy(secondArmy, secondGeneral);
        Board board1 = Deserializer.deserializeBoard(Serializer.serializeBoard(board));
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 3; j++){
                assertEquals(board.getArmy(Fields.PLAYER_ONE)[i][j], board1.getArmy(Fields.PLAYER_ONE)[i][j]);
                assertEquals(board.getArmy(Fields.PLAYER_TWO)[i][j], board1.getArmy(Fields.PLAYER_TWO)[i][j]);
            }
        }
    }

    @Test
    public void testSDArmy() throws UnitException, BoardException, IOException {
        General general = new General(GeneralTypes.ARCHMAGE);
        Unit[][] units = new Unit[][]{
                {new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), general, new Unit(UnitTypes.HEALER)}
        };
        Army army = new Army(units, general);
        army.getPlayerUnits()[0][0].setArmor(55);
        army.getPlayerUnits()[0][1].setAccuracy(22);
        army.getPlayerUnits()[0][2].setCurrentHP(-4);
        army.getPlayerUnits()[1][0].setPower(11);
        army.getPlayerUnits()[1][1].setActive(false);
        army.getPlayerUnits()[1][2].setActive(false);
        Army army1 = Deserializer.deserializeArmy(Serializer.serializeArmy(army));
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
