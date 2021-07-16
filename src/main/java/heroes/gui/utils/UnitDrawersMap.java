package heroes.gui.utils;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.generaldrawers.CommanderDrawer;
import heroes.gui.generaldrawers.IGeneralDrawer;
import heroes.gui.generaldrawers.PriestDrawer;
import heroes.gui.generaldrawers.SniperDrawer;
import heroes.gui.unitdrawers.*;
import heroes.units.UnitTypes;

import java.util.Map;

public class UnitDrawersMap {
    private static final Map<ActionTypes, IUnitDrawer> unitDrawersMap;
    static {
        unitDrawersMap = Map.of(
                ActionTypes.CLOSE_COMBAT, new SwordsmanDrawer(),
                ActionTypes.HEALING, new HealerDrawer(),
                ActionTypes.RANGE_COMBAT, new BowmanDrawer(),
                ActionTypes.AREA_DAMAGE, new MageDrawer()
        );
    }

    public static IUnitDrawer getDrawer(final ActionTypes at) {
        return unitDrawersMap.get(at);
    }



}
