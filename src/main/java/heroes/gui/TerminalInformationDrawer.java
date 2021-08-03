package heroes.gui;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.utils.TextColorMap;

import java.io.IOException;

public class TerminalInformationDrawer {
    public static void drawInfo(final TerminalWrapper tw, final Board board) throws IOException {
        drawRound(tw, board);
        drawTurn(tw, board);
    }

    private static void drawRound(final TerminalWrapper tw, final Board board) throws IOException {
        int center = tw.getTerminal().getTerminalSize().getColumns() / 2;
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(TextColorMap.getColor("orange"));
        tg.putString(center, 1, "| Current round: " + board.getCurNumRound());
        tg.setForegroundColor(TextColorMap.getColor("white"));
    }

    private static void drawTurn(final TerminalWrapper tw, final Board board) throws IOException {
        int center = tw.getTerminal().getTerminalSize().getColumns() / 2;
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(TextColorMap.getColor("orange"));
        if (board.getCurrentPlayer() == Fields.PLAYER_ONE) {
            tg.putString(center - 21, 1, "Turn of RIGHT player");
        } else {
            tg.putString(center - 20, 1, "Turn of LEFT player");
        }
        tg.setForegroundColor(TextColorMap.getColor("white"));

    }
}
