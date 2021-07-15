package heroes.gui.generaldrawers;

import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Side;

import java.io.IOException;

public interface IGeneralDrawer {
    void draw(final TerminalWrapper tw, final Side s) throws IOException;
}
