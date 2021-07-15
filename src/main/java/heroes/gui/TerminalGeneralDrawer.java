package heroes.gui;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.utils.GeneralDrawersMap;
import heroes.gui.generaldrawers.IGeneralDrawer;
import heroes.gui.utils.Side;

import java.io.IOException;

public class TerminalGeneralDrawer {
    public static void drawGenerals(final TerminalWrapper tw,
                                    final ActionTypes generalOne, final ActionTypes generalTwo) throws IOException {
        IGeneralDrawer genOne = GeneralDrawersMap.getDrawer(generalOne);
        IGeneralDrawer genTwo = GeneralDrawersMap.getDrawer(generalTwo);
        genOne.draw(tw, Side.LHS);
        genTwo.draw(tw, Side.RHS);
    }
}
