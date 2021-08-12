package heroes.player.controlsystem;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.controller.IController;
import heroes.gui.IGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Controls {
    private final IController controller;

    private final Logger logger = LoggerFactory.getLogger(Controls.class);
    public Controls(final IController controller) {
        this.controller = controller;
    }

    public KeyType update() {
        final KeyStroke ks = controller.pollInput();
        return (ks != null) ? ks.getKeyType() : null;
    }

}
