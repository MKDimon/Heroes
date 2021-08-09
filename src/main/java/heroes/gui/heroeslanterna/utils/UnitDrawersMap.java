package heroes.gui.heroeslanterna.utils;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.heroeslanterna.unitdrawers.*;

import java.util.Map;

/**
 * Статический класс хранит неизменяемый Map, который создает связь между типом юнита и соответствующим классом Drawer.
 * По сути, вместе с IUnitDrawer, это урезанная реализация паттерна Strategy.
 */
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
