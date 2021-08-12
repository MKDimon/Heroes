package heroes.controller;

import com.googlecode.lanterna.input.KeyStroke;
import heroes.player.controlsystem.Selector;


public interface IController {
    int getFieldCommand();
    int getRoomCommand();
    int getOpponentCommand();
    String getBot(final Selector selector);
    KeyStroke pollInput();
}
