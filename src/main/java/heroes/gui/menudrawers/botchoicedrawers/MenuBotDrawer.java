package heroes.gui.menudrawers.botchoicedrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.TextColorMap;

public class MenuBotDrawer {
    public static void drawBots(final TerminalWrapper tw, final int selectedGeneral) {
        int y = 20;
        int x_start = tw.getScreen().getTerminalSize().getColumns() / 2;

        TextGraphics tg = tw.getScreen().newTextGraphics();

        tg.putString(x_start - 20, y, "TestBot");
        tg.putString(x_start - 5, y, "RandomBot");
        tg.putString(x_start + 5, y, "PlayerBot");
        tg.putString(x_start + 20, y, "PlayerGUIBot");

        if (selectedGeneral == 1) {
            tg.setForegroundColor(TextColorMap.getColor("gold"));
            tg.putString(x_start - 20, y, "TestBot");
            tg.setForegroundColor(TextColorMap.getColor("white"));
        }

        if (selectedGeneral == 2) {
            tg.setForegroundColor(TextColorMap.getColor("gold"));
            tg.putString(x_start - 5, y, "RandomBot");
            tg.setForegroundColor(TextColorMap.getColor("white"));
        }

        if (selectedGeneral == 3) {
            tg.setForegroundColor(TextColorMap.getColor("gold"));
            tg.putString(x_start + 5, y, "PlayerBot");
            tg.setForegroundColor(TextColorMap.getColor("white"));
        }

        if (selectedGeneral == 4) {
            tg.setForegroundColor(TextColorMap.getColor("gold"));
            tg.putString(x_start + 20, y, "PlayerGUIBot");
            tg.setForegroundColor(TextColorMap.getColor("white"));
        }
    }

    public static void drawWait(final TerminalWrapper tw) {
        int y = 20;
        int x_start = tw.getScreen().getTerminalSize().getColumns() / 2;
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start - 24, y, "The choice is made. Waiting for your opponent...");

    }
}
