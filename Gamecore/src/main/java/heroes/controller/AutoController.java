package heroes.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.clientserver.ClientsConfigs;
import heroes.player.controlsystem.Selector;

public class AutoController implements IController{
    private final ClientsConfigs cc;

    public AutoController(final ClientsConfigs cc) {
        this.cc = cc;
    }

    @Override
    public int getFieldCommand() {
        return cc.FIELD;
    }

    @Override
    public int getRoomCommand() {
        return cc.ROOM;
    }

    @Override
    public int getOpponentCommand() {
        return cc.WITH_BOT? 2 : 1;
    }

    @Override
    public String getBot(Selector selector) {
        return cc.TYPE_BOT;
    }

    @Override
    public KeyStroke pollInput() {
        return new KeyStroke(KeyType.Enter);
    }
}
