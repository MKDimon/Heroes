package heroes;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.SimpleTerminalResizeListener;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gui.TerminalBorderDrawer;
import heroes.gui.TerminalWrapper;
import heroes.gui.unitdrawers.HealerDrawer;
import heroes.gui.unitdrawers.MageDrawer;
import heroes.gui.unitdrawers.SwordsmanDrawer;
import heroes.gui.utils.TextColorMap;
import heroes.gui.generaldrawers.*;
import heroes.gui.unitdrawers.BowmanDrawer;
import heroes.gui.utils.Side;
import heroes.mathutils.Pair;
import heroes.player.Answer;
import heroes.player.IPlayer;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//TODO: Сам класс должен отвечать за изменение своих параметров. Просмотреть методы get и set.
//TODO: Java-doc.
public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);

    public static void main(final String[] args) throws GameLogicException, IOException, InterruptedException {
        TerminalWrapper tw = new TerminalWrapper();
        tw.start();

        IPlayer playerOne = new RandomBot(Fields.PLAYER_ONE);
        IPlayer playerTwo = new RandomBot(Fields.PLAYER_TWO);
        Map<Fields, IPlayer> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        GameLogic gl = new GameLogic();

        SimpleTerminalResizeListener strl = new SimpleTerminalResizeListener(tw.getTerminal().getTerminalSize());
        tw.getTerminal().addResizeListener(strl);
        KeyStroke ks = new KeyStroke(KeyType.Escape);

        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());


//        while(true) {
//            if (strl.isTerminalResized()) {
//
//            }
//        }

        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());
        tw.update(null, gl.getBoard());
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(65, tw.getTerminal().getTerminalSize().getRows() -
                        (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3), "PRESS ENTER TO BEGIN");
        tw.getScreen().refresh();
        tw.getScreen().readInput();
        //tw.stop();
        while (gl.isGameBegun()) {
            TimeUnit.SECONDS.sleep(1);
            Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            tw.update(answer, gl.getBoard());
        }
        tg.putString(55, tw.getTerminal().getTerminalSize().getRows() -
                (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3), "PRESS ENTER TO LEAVE THIS MASSACRE...");
        tw.getScreen().refresh();
        tw.getScreen().readInput();
        tw.stop();

    }
}
