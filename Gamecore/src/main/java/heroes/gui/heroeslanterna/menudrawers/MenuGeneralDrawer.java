package heroes.gui.heroeslanterna.menudrawers;

import com.googlecode.lanterna.TerminalPosition;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.menudrawers.generalmenudrawers.CommanderMenuDrawer;
import heroes.gui.heroeslanterna.menudrawers.generalmenudrawers.PriestMenuDrawer;
import heroes.gui.heroeslanterna.menudrawers.generalmenudrawers.SniperMenuDrawer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MenuGeneralDrawer {
    private static final Logger logger = LoggerFactory.getLogger(MenuGeneralDrawer.class);
    public static void drawGenerals(final LanternaWrapper tw, final int selectedGeneral) {
        boolean commanderSelected = false;
        boolean archmageSelected = false;
        boolean sniperSelected = false;

        if (selectedGeneral == 1) {
            commanderSelected = true;
        }

        if (selectedGeneral == 2) {
            sniperSelected = true;
        }

        if (selectedGeneral == 3) {
            archmageSelected = true;
        }
        final int y = 10;
        final int x_start = tw.getScreen().getTerminalSize().getColumns() / 2;

        final CommanderMenuDrawer commanderMenuDrawer = new CommanderMenuDrawer();
        final SniperMenuDrawer sniperMenuDrawer = new SniperMenuDrawer();
        final PriestMenuDrawer priestMenuDrawer = new PriestMenuDrawer();

        try {
            commanderMenuDrawer.draw(tw, new TerminalPosition(x_start - 68, y), commanderSelected);
            sniperMenuDrawer.draw(tw, new TerminalPosition(x_start - 18, y), sniperSelected);
            priestMenuDrawer.draw(tw, new TerminalPosition(x_start + 38, y), archmageSelected);
        } catch (IOException e) {
            logger.error("Error in drawing menu generals.", e);
        }


    }
}
