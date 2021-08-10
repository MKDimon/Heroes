package heroes.gui.heroeslanterna.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;

import java.io.IOException;
import java.util.EnumSet;

public class PriestMenuDrawer implements IGeneralMenuDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException {
        int y_start = tp.getRow();
        final int x_start = tp.getColumn();

        final TextGraphics tg = tw.getScreen().newTextGraphics();
        if (isSelected) {
            tg.setForegroundColor(Colors.GOLD.color());
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 4, y_start + 25, "ARCHMAGE");
            tg.setForegroundColor(Colors.LIGHTBLUE.color());
            tg.setModifiers(EnumSet.of(SGR.ITALIC));
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 40, y_start + 27,
                    "His Majesty's Court Magician. Wise scholar and leader of Kingdom's Mage Circle.");
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 44, y_start + 28,
                    "Archmage's magic instills terror in enemy. His troops' weapon is powered by great spells.");
            tg.clearModifiers();
            tg.setForegroundColor(Colors.BLUE.color());
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 20, y_start + 30,
                    "Inspiration: ");
            tg.putString(tw.getTerminal().getTerminalSize().getColumns() / 2 - 5, y_start + 30,
                    "Power +10% for all gamecore.units.");
            tg.setForegroundColor(Colors.GOLD.color());
        }
        tg.putString(x_start, y_start++, "                  .---.");
        tg.putString(x_start, y_start++, "         /^\\     /.'\"'.\\");
        tg.putString(x_start, y_start++, "       .'   '.   \\\\   ||");
        tg.putString(x_start, y_start++, "      <       > ,_),-',' ");
        tg.putString(x_start, y_start++, "       \\_____/     ()");
        tg.putString(x_start, y_start++, "       {/a a\\}     ||");
        tg.putString(x_start, y_start++, "      {/-.^.-\\}   (_|");
        tg.putString(x_start, y_start++, "     .'{  `  }'-._/|;\\");
        tg.putString(x_start, y_start++, "    /  {     }  /; || |");
        tg.putString(x_start, y_start++, "    /`'-{     }-';  || |");
        tg.putString(x_start, y_start++, "  ; `'=|{   }|=' _/|| |");
        tg.putString(x_start, y_start++, "  |   \\| |~| |  |/ || |");
        tg.putString(x_start, y_start++, "  |\\   \\ | | |  ;  || |");
        tg.putString(x_start, y_start++, "  | \\   ||=| |=<\\  || |");
        tg.putString(x_start, y_start++, "  | /\\_/\\| | |  \\`-||_/");
        tg.putString(x_start, y_start++, "  '-| `;'| | |  |  ||");
        tg.putString(x_start, y_start++, "    |  |+| |+|  |  ||");
        tg.putString(x_start, y_start++, "    |  | | | |  |  ||");
        tg.putString(x_start, y_start++, "    |  \"\"\" \"\"\"  |  ||");
        tg.putString(x_start, y_start++, "    |_ _ _ _ _ _|  ||");
        tg.putString(x_start, y_start++, "     |,;,;,;,;,;,|  ||");
        tg.putString(x_start, y_start++, "    `|||||||||||`  ||");
        tg.putString(x_start, y_start++, "      |||||||||||   ||");
        tg.putString(x_start, y_start, "     `\"\"\"\"\"\"\"\"\"`   \"\"");
    }
}
