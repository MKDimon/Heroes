package heroes.gui.generaldrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Side;

import java.io.IOException;

public class PriestDrawer implements IGeneralDrawer {
    @Override
    public void draw(final TerminalWrapper tw, final Side s) throws IOException {
        int y_start = 1;
        int x_start = (s == Side.RHS) ? tw.getTerminal().getTerminalSize().getColumns() - 30 : 5;

        TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.putString(x_start, y_start + 1, "                  .---.");
        tg.putString(x_start, y_start + 2, "         /^\\     /.'\"'.\\");
        tg.putString(x_start, y_start + 3, "       .'_|_'.   \\\\   ||");
        tg.putString(x_start, y_start + 4, "      <   |   > ,_),-',' ");
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
