package heroes.gui.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.TerminalPosition;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.Side;

import java.io.IOException;

public interface IGeneralMenuDrawer {
    void draw(final TerminalWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException;
}
