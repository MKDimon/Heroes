package heroes;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.mathutils.Position;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestActionValidation {

    @Test
    public void test() throws UnitException {
        Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        assertEquals(6, Arrays.stream(fieldPlayerOne).mapToLong(x -> Arrays.stream(x).filter(Unit::isActive).count()).sum());
    }

    @Test
    public void testValid() throws UnitException {
        General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne;              fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = generalPlayerTwo;

        GameLogic gl = new GameLogic();
        gl.gameStart(fieldPlayerOne, fieldPlayerTwo, generalPlayerOne, generalPlayerTwo);
        assertAll(
                () -> assertTrue(gl.action(new Position(0,0, Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.DEFENSE)),

                () -> assertFalse(gl.action(new Position(0,0,Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.AREA_DAMAGE)),

                () -> assertFalse(gl.action(new Position(0,0,Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT)),

                () -> assertFalse(gl.action(new Position(1,0,Fields.PLAYER_TWO),
                        new Position(0,2, Fields.PLAYER_ONE), ActionTypes.HEALING)),

                () -> assertTrue(gl.action(new Position(0,0,Fields.PLAYER_ONE),
                        new Position(0,1, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT)),

                () -> assertTrue(gl.action(new Position(1,0,Fields.PLAYER_TWO),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.HEALING))
        );

        gl.getBoard().getFieldPlayerOne()[0][0].setCurrentHP(0);
        gl.getBoard().getFieldPlayerOne()[0][1].setCurrentHP(0);
        assertTrue(gl.action(new Position(0,2,Fields.PLAYER_ONE),
                new Position(0,0, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT));
    }
}
