package heroes.gui.utils;

import com.googlecode.lanterna.TerminalPosition;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.gui.menudrawers.unitmenudrawers.UnitMenuTerminalGrid;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerminalArmyDrawer {
    private static final Logger logger = LoggerFactory.getLogger(TerminalArmyDrawer.class);

    public static void drawArmy(final TerminalWrapper tw, final TerminalPosition tp,
                         final Army army, final boolean withBorders, final Fields field) {
        drawArmy(tw, tp, army.getPlayerUnits(), army.getGeneral(), withBorders, field);
    }
    public static void drawArmy(final TerminalWrapper tw, final TerminalPosition tp,
                                final Unit[][] units, final General general,
                                final boolean withBorders, final Fields field) {
        UnitMenuTerminalGrid utg = new UnitMenuTerminalGrid(tw);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Unit unit = units[i][j];
                if (unit != null) {
                    UnitDrawersMap.getDrawer(unit.getActionType())
                            .draw(tw, utg.getPair(new Position(i, j, field)),
                                    unit == general);
                }
            }
        }

    }
}
