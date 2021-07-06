package heroes;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
import heroes.auxiliaryclasses.ActionTypes;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBoardValidation {

    @Test
    public void testValid() throws UnitException {
        Board board = new Board();
        assertAll(
                () -> assertTrue(board.action(new Position(0,0, Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.DEFENSE)),

                () -> assertFalse(board.action(new Position(0,0,Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.AREA_DAMAGE)),

                () -> assertFalse(board.action(new Position(0,0,Fields.PLAYER_ONE),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT)),

                () -> assertFalse(board.action(new Position(1,0,Fields.PLAYER_TWO),
                        new Position(0,2, Fields.PLAYER_ONE), ActionTypes.HEALING)),

                () -> assertTrue(board.action(new Position(0,0,Fields.PLAYER_ONE),
                        new Position(0,1, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT)),

                () -> assertTrue(board.action(new Position(1,0,Fields.PLAYER_TWO),
                        new Position(0,2, Fields.PLAYER_TWO), ActionTypes.HEALING))
        );

        board.getFieldPlayerOne()[0][0].setCurrentHP(0);
        board.getFieldPlayerOne()[0][1].setCurrentHP(0);
        assertTrue(board.action(new Position(0,2,Fields.PLAYER_ONE),
                new Position(0,0, Fields.PLAYER_TWO), ActionTypes.CLOSE_COMBAT));
    }
}
