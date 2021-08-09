package heroes.gui.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Colors;

import java.io.IOException;
import java.util.EnumSet;

public class PriestMenuDrawer implements IGeneralMenuDrawer {
    @Override
    public void draw(final TerminalWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException {
        int y_start = tp.getRow();
        int x_start = tp.getColumn();

        TextGraphics tg = tw.getScreen().newTextGraphics();
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
                    "Power +10% for all units.");
            tg.setForegroundColor(Colors.GOLD.color());
        }
        tg.putString(x_start, y_start + 1, "                  .---.");
        tg.putString(x_start, y_start + 2, "         /^\\     /.'\"'.\\");
        tg.putString(x_start, y_start + 3, "       .'   '.   \\\\   ||");
        tg.putString(x_start, y_start + 4, "      <       > ,_),-',' ");
        tg.putString(x_start, y_start + 5, "       \\_____/     ()");
        tg.putString(x_start, y_start + 6, "       {/a a\\}     ||");
        tg.putString(x_start, y_start + 7, "      {/-.^.-\\}   (_|");
        tg.putString(x_start, y_start + 8, "     .'{  `  }'-._/|;\\");
        tg.putString(x_start, y_start + 9, "    /  {     }  /; || |");
        tg.putString(x_start, y_start + 10, "    /`'-{     }-';  || |");
        tg.putString(x_start, y_start + 11, "  ; `'=|{   }|=' _/|| |");
        tg.putString(x_start, y_start + 12, "  |   \\| |~| |  |/ || |");
        tg.putString(x_start, y_start + 13, "  |\\   \\ | | |  ;  || |");
        tg.putString(x_start, y_start + 14, "  | \\   ||=| |=<\\  || |");
        tg.putString(x_start, y_start + 15, "  | /\\_/\\| | |  \\`-||_/");
        tg.putString(x_start, y_start + 16, "  '-| `;'| | |  |  ||");
        tg.putString(x_start, y_start + 17, "    |  |+| |+|  |  ||");
        tg.putString(x_start, y_start + 18, "    |  | | | |  |  ||");
        tg.putString(x_start, y_start + 19, "    |  \"\"\" \"\"\"  |  ||");
        tg.putString(x_start, y_start + 20, "    |_ _ _ _ _ _|  ||");
        tg.putString(x_start, y_start + 20, "     |,;,;,;,;,;,|  ||");
        tg.putString(x_start, y_start + 20, "    `|||||||||||`  ||");
        tg.putString(x_start, y_start + 20, "      |||||||||||   ||");
        tg.putString(x_start, y_start + 20, "     `\"\"\"\"\"\"\"\"\"`   \"\"");
    }
}
