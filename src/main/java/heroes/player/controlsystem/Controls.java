package heroes.player.controlsystem;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.gui.IGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Controls {
    private final IGUI gui;

    private final Logger logger = LoggerFactory.getLogger(Controls.class);
    public Controls(final IGUI gui) {
        this.gui = gui;
    }

    public KeyType update() {
        KeyStroke ks = null;
        try {
            ks = gui.getScreen().pollInput();
        } catch (IOException e) {
            logger.error("Error in getting pressed button procedure in Controls.", e);
        }
        return (ks != null) ? ks.getKeyType() : null;
    }

}
