package heroes.player.botnikita;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gamelogic.GameStatus;
import heroes.gui.TerminalWrapper;
import heroes.player.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SelfPlayNikita {
    public static void main(final String[] args) throws GameLogicException, UnitException {
        final List<BaseBot.BaseBotFactory> factories = Arrays.asList(new RandomBot.RandomBotFactory(),
                new TestBot.TestBotFactory(), new PlayerBot.PlayerBotFactory(), new PlayerGUIBot.PlayerGUIBotFactory(),
                new NikitaBot.NikitaBotFactory());
        final BaseBot playerOne = factories.get(1).createBot(Fields.PLAYER_ONE);
        final BaseBot playerTwo = factories.get(4).createBot(Fields.PLAYER_TWO);
        final Map<Fields, BaseBot> getPlayer = new HashMap<>();

        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        int counter = 0;
        final int gamesCount = 100;

        for (int i = 0; i < gamesCount; i ++) {

            final GameLogic gl = new GameLogic();
            final Army firstPlayerArmy = playerOne.getArmy(null);
            gl.gameStart(firstPlayerArmy, playerTwo.getArmy(firstPlayerArmy));


            while (gl.isGameBegun()) {
                final Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
                gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            }
            if (gl.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS ||
                    gl.getBoard().getStatus() == GameStatus.NO_WINNERS) {
                counter = counter + 1;
            }
            System.out.println("Game number " + i);
        }

        System.out.println("Minmax bot won " + counter + " games of " + gamesCount);
    }
}
