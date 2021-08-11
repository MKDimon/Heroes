package heroes.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.player.Answer;

public interface IGUI {
    void refresh();
    void start();
    TextGraphics newTG();
    void printPlayer(final Fields field);
    int updateMenu();
    void update(final Answer answer, final Board board);
    void stop();
    Screen getScreen();
    Terminal getTerminal();
    TerminalSize getTerminalSize();

}
