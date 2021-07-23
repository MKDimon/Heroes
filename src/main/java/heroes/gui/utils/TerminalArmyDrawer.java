package heroes.gui.utils;

import com.googlecode.lanterna.TerminalPosition;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.mathutils.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerminalArmyDrawer {
    private static final Logger logger = LoggerFactory.getLogger(TerminalArmyDrawer.class);
    public static void drawArmy(final TerminalWrapper tw, final TerminalPosition tp,
                                final Army army, final boolean withBorders) {
        UnitTerminalGrid utg = new UnitTerminalGrid(tw);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    UnitDrawersMap.getDrawer(army.getPlayerUnits()[i][j].getActionType())
                            .draw(tw, utg.getPair(new Position(i, j, Fields.PLAYER_TWO)),
                                    army.getPlayerUnits()[i][j] == army.getGeneral());
                } catch (UnitException e) {
                    logger.error("Cannot get general in TerminalArmyDrawer", e);
                }
            }
        }

    }
}
