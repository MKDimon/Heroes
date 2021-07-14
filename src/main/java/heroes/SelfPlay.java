package heroes;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfPlay {


    public static void main(final String[] args) throws GameLogicException, UnitException {
        Logger logger = LoggerFactory.getLogger(SelfPlay.class);
        /*
            Инициализация ботов
         */
        List<BaseBot.BaseBotFactory> factories = Arrays.asList(new RandomBot.RandomBotFactory(),
                new TestBot.TestBotFactory(), new PlayerBot.PlayerBotFactory());
        BaseBot playerOne = factories.get(0).createBot(Fields.PLAYER_ONE);
        BaseBot playerTwo = factories.get(2).createBot(Fields.PLAYER_TWO);
        Map<Fields, BaseBot> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        GameLogic gl = new GameLogic();
        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());
        while (gl.isGameBegun()) {
            Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
        }
    }
}
