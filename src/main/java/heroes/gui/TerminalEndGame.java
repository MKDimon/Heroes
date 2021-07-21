package heroes.gui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.clientserver.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TerminalEndGame {
    private static final Logger logger = LoggerFactory.getLogger(TerminalEndGame.class);
    public static void endGame(final TerminalWrapper tw, final Data data) {

        tw.getScreen().clear();
        TextGraphics tg = tw.getScreen().newTextGraphics();
        int x = 0, y = 0;
        try {
            tw.getScreen().refresh();
            x = tw.getTerminal().getTerminalSize().getColumns() / 2;
            y = tw.getTerminal().getTerminalSize().getRows() / 2;
        } catch (IOException e) {
            logger.error("Cannot call TerminalSize from TerminalEndGame", e);
        }
        tg.putString(x - 8, y, "The game is over.");
        tg.putString(x - 10, y + 1, "Press ENTER to leave.");
        boolean isPressedEnter = false;
        while (!isPressedEnter) {
            KeyStroke ks = new KeyStroke(KeyType.F19);
            try {
                tw.getScreen().refresh();
                ks = tw.getScreen().readInput();
            } catch (IOException e) {
                logger.error("Cannot read input in TerminalEndGame", e);
            }

            if (ks.getKeyType() == KeyType.Enter) {
                isPressedEnter = true;
                try {
                    tw.getScreen().refresh();
                    tw.stop();
                } catch (IOException e) {
                    logger.error("Cannot stop terminal in TerminalEndGame", e);
                }
            }
        }
    }
}
