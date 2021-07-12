package heroes;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gui.TerminalWrapper;
import heroes.gui.TextColorMap;
import heroes.player.Answer;
import heroes.player.IPlayer;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Map;

//TODO: Сам класс должен отвечать за изменение своих параметров. Просмотреть методы get и set.
//TODO: Java-doc.
public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);
    public static void main(final String[] args) throws GameLogicException, IOException {
        TerminalWrapper tw = new TerminalWrapper();
        Screen screen = tw.start();

        IPlayer playerOne = new TestBot(Fields.PLAYER_ONE);
        IPlayer playerTwo = new RandomBot(Fields.PLAYER_TWO);
        Map<Fields, IPlayer> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        Terminal terminal = tw.getTerminal();

        terminal.putCharacter('H');
        terminal.putCharacter('e');
        terminal.putCharacter('l');
        terminal.putCharacter('l');
        terminal.putCharacter('o');
        terminal.putCharacter('!');

        tw.getTg().setForegroundColor(TextColorMap.getColor("red"));
        tw.getTg().putString(12, 12, "KEK");

        screen.refresh();

        screen.readInput();
        screen.stopScreen();

        GameLogic gl = new GameLogic();
        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());
        while (gl.isGameBegun()) {
            Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
        }
    }
}
