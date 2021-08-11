package heroes.gui.heroeslanterna.menudrawers.generalmenudrawers;

import com.googlecode.lanterna.TerminalPosition;
import heroes.gui.heroeslanterna.LanternaWrapper;

import java.io.IOException;

public interface IGeneralMenuDrawer {
    void draw(final LanternaWrapper tw, final TerminalPosition tp, final boolean isSelected) throws IOException;
}
