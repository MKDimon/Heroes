package heroes.gui.menudrawers;

import com.googlecode.lanterna.TerminalPosition;
import heroes.gui.TerminalWrapper;
import heroes.gui.menudrawers.generalmenudrawers.CommanderMenuDrawer;
import heroes.gui.menudrawers.generalmenudrawers.PriestMenuDrawer;
import heroes.gui.menudrawers.generalmenudrawers.SniperMenuDrawer;
import heroes.gui.menudrawers.unitmenudrawers.BowmanMenuDrawer;
import heroes.gui.menudrawers.unitmenudrawers.HealerMenuDrawer;
import heroes.gui.menudrawers.unitmenudrawers.MageMenuDrawer;
import heroes.gui.menudrawers.unitmenudrawers.SwordsmanMenuDrawer;
import heroes.mathutils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MenuUnitDrawer {
    public static void drawUnits(final TerminalWrapper tw, final int selectedUnit) {
        boolean swordsmanSelected = false;
        boolean bowmanSelected = false;
        boolean mageSelected = false;
        boolean healerSelected = false;

        if (selectedUnit == 1) {
            swordsmanSelected = true;
        }

        if (selectedUnit == 2) {
            bowmanSelected = true;
        }

        if (selectedUnit == 3) {
            mageSelected = true;
        }

        if (selectedUnit == 4) {
            healerSelected = true;
        }
        int y = 5;
        int x_start = tw.getScreen().getTerminalSize().getColumns() / 2 - 4;

        SwordsmanMenuDrawer swordsmanMenuDrawer = new SwordsmanMenuDrawer();
        BowmanMenuDrawer bowmanMenuDrawer = new BowmanMenuDrawer();
        MageMenuDrawer mageMenuDrawer = new MageMenuDrawer();
        HealerMenuDrawer healerMenuDrawer = new HealerMenuDrawer();


        swordsmanMenuDrawer.draw(tw, new Pair<Integer, Integer>(x_start , y ), swordsmanSelected);
        bowmanMenuDrawer.draw(tw, new Pair<Integer, Integer>(x_start, y + 10), bowmanSelected);
        mageMenuDrawer.draw(tw, new Pair<Integer, Integer>(x_start, y + 20), mageSelected);
        healerMenuDrawer.draw(tw, new Pair<Integer, Integer>(x_start, y + 30), healerSelected);

    }
}
