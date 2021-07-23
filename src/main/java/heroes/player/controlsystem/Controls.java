package heroes.player.controlsystem;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.clientserver.commands.GetRoomCommand;
import heroes.gui.TerminalWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controls {
    private final TerminalWrapper tw;

    private final Logger logger = LoggerFactory.getLogger(Controls.class);
    public Controls(final TerminalWrapper tw) {
        this.tw = tw;
    }

    public KeyType update() {
        KeyStroke ks = null;
        try {
            ks = tw.getScreen().pollInput();
        } catch (IOException e) {
            logger.error("Error in getting pressed button procedure in Controls.", e);
        }
        return (ks != null) ? ks.getKeyType() : null;
    }

}
