package heroes.gui.heroeslanterna.menudrawers.botchoicedrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;

public class MenuBotDrawer {
    public static void drawBots(final LanternaWrapper tw, final int selectedGeneral) {
        int y = 20;
        int x_start = tw.getScreen().getTerminalSize().getColumns() / 2;

        TextGraphics tg = tw.getScreen().newTextGraphics();

        tg.putString(x_start - 20, y, "TestBot");
        tg.putString(x_start - 5, y, "RandomBot");
        tg.putString(x_start + 5, y, "PlayerBot");
        tg.putString(x_start + 20, y, "PlayerGUIBot");

        if (selectedGeneral == 1) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(x_start - 20, y, "TestBot");
            tg.setForegroundColor(Colors.WHITE.color());
        }

        if (selectedGeneral == 2) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(x_start - 5, y, "RandomBot");
            tg.setForegroundColor(Colors.WHITE.color());
        }

        if (selectedGeneral == 3) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(x_start + 5, y, "PlayerBot");
            tg.setForegroundColor(Colors.WHITE.color());
        }

        if (selectedGeneral == 4) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(x_start + 20, y, "PlayerGUIBot");
            tg.setForegroundColor(Colors.WHITE.color());
        }
    }

    public static void drawWait(final LanternaWrapper tw) {
        int y = 20;
        int x_start = tw.getScreen().getTerminalSize().getColumns() / 2;
        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start - 24, y, "The choice is made. Waiting for your opponent...");

    }
}
