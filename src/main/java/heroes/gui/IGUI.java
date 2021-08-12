package heroes.gui;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.player.Answer;

public interface IGUI {
    void refresh();
    void start();
    void printPlayer(final Fields field);
    int updateMenu();
    void update(final Answer answer, final Board board);
    void stop();
    void clear();
    void pollInput();
}
