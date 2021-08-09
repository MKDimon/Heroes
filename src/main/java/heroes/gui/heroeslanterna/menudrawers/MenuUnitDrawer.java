package heroes.gui.heroeslanterna.menudrawers;

import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.menudrawers.unitmenudrawers.BowmanMenuDrawer;
import heroes.gui.heroeslanterna.menudrawers.unitmenudrawers.HealerMenuDrawer;
import heroes.gui.heroeslanterna.menudrawers.unitmenudrawers.MageMenuDrawer;
import heroes.gui.heroeslanterna.menudrawers.unitmenudrawers.SwordsmanMenuDrawer;
import heroes.mathutils.Pair;

public class MenuUnitDrawer {
    public static void drawUnits(final LanternaWrapper tw, final int selectedUnit) {
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


        swordsmanMenuDrawer.draw(tw, new Pair<>(x_start , y ), swordsmanSelected);
        bowmanMenuDrawer.draw(tw, new Pair<>(x_start, y + 10), bowmanSelected);
        mageMenuDrawer.draw(tw, new Pair<>(x_start, y + 20), mageSelected);
        healerMenuDrawer.draw(tw, new Pair<>(x_start, y + 30), healerSelected);

    }
}
