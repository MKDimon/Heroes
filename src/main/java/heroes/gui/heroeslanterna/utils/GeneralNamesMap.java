package heroes.gui.heroeslanterna.utils;

import gamecore.auxiliaryclasses.ActionTypes;

import java.util.Map;

public class GeneralNamesMap {
    private static final Map<ActionTypes, String> generalDrawersMap;
    static {
        generalDrawersMap = Map.of(
                ActionTypes.CLOSE_COMBAT, "COMMANDER",
                ActionTypes.RANGE_COMBAT, "SNIPER",
                ActionTypes.AREA_DAMAGE, "ARCHMAGE"
        );
    }

    public static String getName(final ActionTypes at) {
        return generalDrawersMap.get(at);
    }



}
