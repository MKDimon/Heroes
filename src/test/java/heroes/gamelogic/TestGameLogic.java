package heroes.gamelogic;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameLogic {

    @Test
    public void testNewBoard() throws UnitException, BoardException {
        General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne; fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = generalPlayerTwo;              fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = new Unit(UnitTypes.MAGE);

        GameLogic gl = new GameLogic();
        gl.gameStart(new Army(fieldPlayerOne, generalPlayerOne), new Army(fieldPlayerTwo, generalPlayerTwo));

        GameLogic newGL = new GameLogic(gl.getBoard());

        assertNotSame(newGL.getBoard(), gl.getBoard());
    }

    @Test
    public void testIncorrectParams() throws UnitException, BoardException {
        General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne; fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = generalPlayerTwo;              fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = new Unit(UnitTypes.MAGE);

        GameLogic gl = new GameLogic();

        assertFalse(
                gl.gameStart(new Army(fieldPlayerOne, generalPlayerOne), new Army(fieldPlayerOne, generalPlayerOne)));

        fieldPlayerOne[0][2] = null;

        assertFalse(
                gl.gameStart(new Army(fieldPlayerOne, generalPlayerOne), new Army(fieldPlayerTwo, generalPlayerTwo)));
    }

    @Test
    public void testActionValidation() throws UnitException, BoardException {
        /*
         *   Следить за очередью ходов игроков
         */
        General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne;              fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = generalPlayerTwo;              fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = new Unit(UnitTypes.MAGE);

        GameLogic gl = new GameLogic();
        gl.gameStart(new Army(fieldPlayerOne, generalPlayerOne), new Army(fieldPlayerTwo, generalPlayerTwo));


        assertAll(
                () -> assertTrue(gl.action(new Position(0,0, Fields.PLAYER_ONE), // Defending...
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.DEFENSE)),

                () -> assertFalse(gl.action(new Position(0,0,Fields.PLAYER_TWO), // INCORRECT
                        new Position(0,2, Fields.PLAYER_ONE), ActionTypes.AREA_DAMAGE)),

                () -> assertFalse(gl.action(new Position(0,0,Fields.PLAYER_TWO), //
                        new Position(0,2, Fields.PLAYER_ONE), ActionTypes.CLOSE_COMBAT)),

                () -> assertFalse(gl.action(new Position(1,0,Fields.PLAYER_TWO),
                        new Position(0,2, Fields.PLAYER_ONE), ActionTypes.HEALING)),

                () -> assertFalse(gl.action(new Position(10, 25, Fields.PLAYER_TWO),
                        new Position(0, 2, Fields.PLAYER_ONE), ActionTypes.CLOSE_COMBAT)),

                () -> assertTrue(gl.action(new Position(0,0,Fields.PLAYER_TWO),
                        new Position(0,1, Fields.PLAYER_ONE), ActionTypes.CLOSE_COMBAT)),

                () -> assertTrue(gl.action(new Position(1,0,Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_ONE), ActionTypes.HEALING))
        );

        gl.getBoard().getFieldPlayerTwo()[0][0].setCurrentHP(0);
        gl.getBoard().getFieldPlayerTwo()[0][1].setCurrentHP(0);
        assertTrue(gl.action(new Position(0,2,Fields.PLAYER_TWO),
                new Position(0,0, Fields.PLAYER_ONE), ActionTypes.CLOSE_COMBAT));
        assertFalse(gl.action(new Position(0,2,Fields.PLAYER_TWO),
                new Position(0,0, Fields.PLAYER_ONE), ActionTypes.CLOSE_COMBAT));
    }
}
