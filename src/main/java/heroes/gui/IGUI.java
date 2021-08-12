package heroes.gui;

import heroes.clientserver.Data;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.player.Answer;
import heroes.player.controlsystem.Selector;

public interface IGUI {
    void refresh();
    void start();
    void printPlayer(final Fields field);
    void drawBots(final Selector selector);
    void drawWait();
    void update(final Answer answer, final Board board);
    void stop();
    void clear();
    void pollInput();
    void endGame(final Data data);
    void continueGame(final Data data);
}
