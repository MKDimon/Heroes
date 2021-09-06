package heroes.gui.heroeslanterna.utils;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gui.heroeslanterna.generaldrawers.CommanderDrawer;
import heroes.gui.heroeslanterna.generaldrawers.IGeneralDrawer;
import heroes.gui.heroeslanterna.generaldrawers.PriestDrawer;
import heroes.gui.heroeslanterna.generaldrawers.SniperDrawer;

import java.util.Map;

/**
 * Статический класс хранит неизменяемый Map, который создает связь между типом генерала и соответствующим классом
 * Drawer. Эти классы рисуют портреты генералов.
 * По сути, вместе с IGeneralDrawer, это урезанная реализация паттерна Strategy.
 */
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
