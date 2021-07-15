package heroes.gui.utils;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.generaldrawers.CommanderDrawer;
import heroes.gui.generaldrawers.IGeneralDrawer;
import heroes.gui.generaldrawers.PriestDrawer;
import heroes.gui.generaldrawers.SniperDrawer;
import heroes.units.GeneralTypes;

import java.util.Map;

public class GeneralDrawersMap {
    private static final Map<ActionTypes, IGeneralDrawer> generalDrawersMap;
    static {
        generalDrawersMap = Map.of(
                ActionTypes.CLOSE_COMBAT, new CommanderDrawer(),
                ActionTypes.RANGE_COMBAT, new SniperDrawer(),
                ActionTypes.AREA_DAMAGE, new PriestDrawer()
        );
    }

    public static IGeneralDrawer getDrawer(final ActionTypes at) {
        return generalDrawersMap.get(at);
    }



}
