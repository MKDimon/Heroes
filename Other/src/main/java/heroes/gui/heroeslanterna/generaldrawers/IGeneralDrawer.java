package heroes.gui.heroeslanterna.generaldrawers;

import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Side;

import java.io.IOException;

public interface IGeneralDrawer {
    void draw(final LanternaWrapper tw, final Side s) throws IOException;
}
