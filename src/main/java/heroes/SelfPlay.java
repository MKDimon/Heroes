package heroes;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.clientserver.commands.CommonCommands;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gamelogic.GameStatus;
import heroes.gui.TerminalEndGame;
import heroes.gui.TerminalWrapper;
import heroes.player.*;
import heroes.player.botnikita.NikitaBot;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);

    public static void main(final String[] args) throws GameLogicException, IOException, InterruptedException, UnitException {
        TerminalWrapper tw = new TerminalWrapper();
        tw.start();

        List<BaseBot.BaseBotFactory> factories = Arrays.asList(new RandomBot.RandomBotFactory(),
                new TestBot.TestBotFactory(), new PlayerBot.PlayerBotFactory(), new PlayerGUIBot.PlayerGUIBotFactory(),
                new NikitaBot.NikitaBotFactory());
        BaseBot playerOne = factories.get(1).createBot(Fields.PLAYER_ONE);
        playerOne.setTerminal(tw);
        BaseBot playerTwo = factories.get(4).createBot(Fields.PLAYER_TWO);
        playerTwo.setTerminal(tw);
        Map<Fields, BaseBot> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        int counter = 0;
        final int gamesCount = 1;
        GameLogic gl = new GameLogic();
        tw.updateMenu();
        for (int i = 0; i < gamesCount; i ++) {
            gl = new GameLogic();
            final Army firstPlayerArmy = playerOne.getArmy(null);
            gl.gameStart(firstPlayerArmy, playerTwo.getArmy(firstPlayerArmy));

            tw.update(null, gl.getBoard());

            while (gl.isGameBegun()) {
                TimeUnit.MICROSECONDS.sleep(500000);
                Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
                gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
                tw.update(answer, gl.getBoard());
            }
            if (gl.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS ||
                    gl.getBoard().getStatus() == GameStatus.NO_WINNERS) {
                counter = counter + 1;
            }
            System.out.println("Game number " + i);
        }

        Data data = new Data(CommonCommands.DRAW ,gl.getBoard());
        TerminalEndGame.endGame(tw, data);
        System.out.println("Minmax bot won " + counter + " games of " + gamesCount);
    }
}
