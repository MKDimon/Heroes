package heroes.player;

import gamecore.auxiliaryclasses.gamelogicexception.GameLogicException;
import gamecore.gamelogic.Army;
import gamecore.gamelogic.Fields;
import gamecore.gamelogic.GameLogic;
import gamecore.gamelogic.validation.Validator;
import gamecore.player.Answer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestTestBot {

    @Test
    public void testBotGetAction() throws GameLogicException {
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
