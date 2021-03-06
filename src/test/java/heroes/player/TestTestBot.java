package heroes.player;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gamelogic.validation.Validator;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTestBot {

    @Test
    public void testBotGetAction() throws GameLogicException, UnitException {
        final BaseBot player = new TestBot(Fields.PLAYER_ONE);
        final BaseBot player1 = new TestBot(Fields.PLAYER_TWO);

        GameLogic gl = new GameLogic();
        final Army firstPlayerArmy = player.getArmy(null);
        gl.gameStart(firstPlayerArmy, player1.getArmy(firstPlayerArmy));

        final Answer answer = player.getAnswer(gl.getBoard());

        assertAll(
                () -> assertDoesNotThrow(
                        () -> Validator.checkNullPointer(answer.getAttacker(),
                                                         answer.getDefender(),
                                                         answer.getActionType())),
                () -> assertEquals(answer.getActionType(),
                        gl.getBoard().getUnitByCoordinate(answer.getAttacker()).getActionType())
        );
    }
}
