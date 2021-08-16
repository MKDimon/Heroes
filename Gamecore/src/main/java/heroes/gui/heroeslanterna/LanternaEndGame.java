package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.commands.CommonCommands;
import heroes.gamelogic.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LanternaEndGame {
    private static final Logger logger = LoggerFactory.getLogger(LanternaEndGame.class);
    public static void endGame(final LanternaWrapper tw, final Data data) {

        final TextGraphics tg = tw.newTG();
        int x = 0, y = 0;
        try {
            tw.update(data.answer, data.board);
            tw.refresh();
            x = tw.getTerminalSize().getColumns() / 2;
            y = tw.getTerminalSize().getRows() ;
        } catch (IOException | UnitException e) {
            logger.error("Cannot call TerminalSize from LanternaEndGame", e);
        }

        tg.putString(x - 8, y - 8, "The game is over.");
        tg.putString(x - 10, y - 7, "Press ENTER to leave.");


        String msg = "";
        if (data.command == CommonCommands.END_GAME) {
            GameStatus gs = data.board.getStatus();
            if (gs == GameStatus.NO_WINNERS) {
                msg = "No one is victorious today";
            } else if (gs == GameStatus.PLAYER_ONE_WINS) {
                tg.putString(x - 14, y - 6, "The game is over on round " + data.board.getCurNumRound());
                msg = "Left player has won... But he's lost many good people today";
            } else if (gs == GameStatus.PLAYER_TWO_WINS) {
                tg.putString(x - 14, y - 6, "The game is over on round " + data.board.getCurNumRound());
                msg = "Right player has won... But he's lost many good people today";
            }
            tg.putString(x - msg.length() / 2, y - 5, msg);
        }



        boolean isPressedEnter = false;
        while (!isPressedEnter) {
            KeyStroke ks = new KeyStroke(KeyType.F19);
            try {
                tw.getScreen().refresh();
                ks = tw.getScreen().readInput();
            } catch (IOException e) {
                logger.error("Cannot read input in LanternaEndGame", e);
            }

            if (ks.getKeyType() == KeyType.Enter) {
                isPressedEnter = true;
                try {
                    tw.getScreen().refresh();
                    tw.stop();
                } catch (IOException e) {
                    logger.error("Cannot stop terminal in LanternaEndGame", e);
                }
            }
        }
    }
}
