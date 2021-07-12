package heroes;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.Answer;
import heroes.player.IPlayer;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//TODO: Сам класс должен отвечать за изменение своих параметров. Просмотреть методы get и set.
//TODO: Java-doc.
public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);
    public static void main(final String[] args) throws GameLogicException, IOException {

        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal = defaultTerminalFactory.createTerminal();
        TerminalSize ts = new TerminalSize(20, 50);
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();



        IPlayer playerOne = new TestBot(Fields.PLAYER_ONE);
        IPlayer playerTwo = new RandomBot(Fields.PLAYER_TWO);
        Map<Fields, IPlayer> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);
        tg.putString(10, 10, "VSEM PRIFFKI V ETOM CHATIKE");
        GameLogic gl = new GameLogic();
        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());
        int counter = 0;
        while (gl.isGameBegun()) {
            Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            screen.refresh();
            counter++;

            screen.readInput();
            screen.stopScreen();
        }
    }
}
