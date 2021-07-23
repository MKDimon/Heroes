package heroes;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.clientserver.commands.CommonCommands;
import heroes.gamelogic.Army;
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
                new TestBot.TestBotFactory(), new PlayerBot.PlayerBotFactory(), new PlayerGUIBot.PlayerGUIBotFactory());
        BaseBot playerOne = factories.get(1).createBot(Fields.PLAYER_ONE);
        playerOne.setTerminal(tw);
        BaseBot playerTwo = factories.get(1).createBot(Fields.PLAYER_TWO);
        playerTwo.setTerminal(tw);
        Map<Fields, BaseBot> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);
        tw.updateMenu();
        GameLogic gl = new GameLogic();
        final Army firstPlayerArmy = playerOne.getArmy(null);
        gl.gameStart(firstPlayerArmy, playerTwo.getArmy(firstPlayerArmy));

        tw.update(null, gl.getBoard());



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
