package heroes.gamelogic;

import gamecore.gamelogic.Army;
import gamecore.gamelogic.Fields;
import gamecore.gamelogic.GameLogic;
import gamecore.units.General;
import gamecore.units.GeneralTypes;
import gamecore.units.Unit;
import gamecore.units.UnitTypes;
import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.auxiliaryclasses.boardexception.BoardException;
import gamecore.auxiliaryclasses.unitexception.UnitException;
import gamecore.mathutils.Position;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class TestGameLogic {

    @Test
    public void testNewBoard() throws UnitException, BoardException {
        final General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        final General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        final Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne; fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        final Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = generalPlayerTwo;              fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = new Unit(UnitTypes.MAGE);

        final GameLogic gl = new GameLogic();
        gl.gameStart(new Army(fieldPlayerOne, generalPlayerOne), new Army(fieldPlayerTwo, generalPlayerTwo));

        final GameLogic newGL = new GameLogic(gl.getBoard());

        assertNotSame(newGL.getBoard(), gl.getBoard());
    }

    @Test
    public void testIncorrectParams() throws UnitException, BoardException {
        final General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        final General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        final Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne; fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        final Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = generalPlayerTwo;              fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = new Unit(UnitTypes.MAGE);

        final GameLogic gl = new GameLogic();

        final Army one = new Army(fieldPlayerOne, generalPlayerOne);
        final Army two = new Army(fieldPlayerTwo, generalPlayerTwo);

        assertFalse(gl.gameStart(one, one));

        fieldPlayerOne[0][2] = null;

        assertFalse(gl.gameStart(one, two));
    }

    @Test
    public void testActionValidation() throws UnitException, BoardException {
        /*
         *   Следить за очередью ходов игроков
         */
        final General generalPlayerOne = new General(GeneralTypes.COMMANDER);
        final General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);

        final Unit[][] fieldPlayerOne = new Unit[2][3];
        fieldPlayerOne[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerOne[0][1] = generalPlayerOne;              fieldPlayerOne[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerOne[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerOne[1][2] = new Unit(UnitTypes.MAGE);

        final Unit[][] fieldPlayerTwo = new Unit[2][3];
        fieldPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][0] = new Unit(UnitTypes.HEALER);
        fieldPlayerTwo[0][1] = generalPlayerTwo;              fieldPlayerTwo[1][1] = new Unit(UnitTypes.BOWMAN);
        fieldPlayerTwo[0][2] = new Unit(UnitTypes.SWORDSMAN); fieldPlayerTwo[1][2] = new Unit(UnitTypes.MAGE);

        final GameLogic gl = new GameLogic();
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
