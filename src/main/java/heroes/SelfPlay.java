package heroes;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.clientserver.commands.CommonCommands;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gui.TerminalEndGame;
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

public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);

    public static void main(final String[] args) throws GameLogicException, IOException, InterruptedException, UnitException {
        TerminalWrapper tw = new TerminalWrapper();
        tw.start();

        List<BaseBot.BaseBotFactory> factories = Arrays.asList(new RandomBot.RandomBotFactory(),
                new TestBot.TestBotFactory(), new PlayerBot.PlayerBotFactory());
        BaseBot playerOne = factories.get(1).createBot(Fields.PLAYER_ONE);
        BaseBot playerTwo = factories.get(1).createBot(Fields.PLAYER_TWO);
        Map<Fields, BaseBot> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        GameLogic gl = new GameLogic();

        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());

        tw.update(null, gl.getBoard());

        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(65, tw.getTerminal().getTerminalSize().getRows() -
                (int) ((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3), "PRESS ENTER TO BEGIN");
        tw.getScreen().refresh();
        tw.getScreen().readInput();

        while (gl.isGameBegun()) {
            TimeUnit.MICROSECONDS.sleep(5000);
            Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            tw.update(answer, gl.getBoard());

        }
        Data data = new Data(CommonCommands.DRAW ,gl.getBoard());
        TerminalEndGame.endGame(tw, data);
    }
}
