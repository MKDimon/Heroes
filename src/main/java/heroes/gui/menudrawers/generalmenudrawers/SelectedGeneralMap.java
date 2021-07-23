package heroes.gui.menudrawers.generalmenudrawers;

import java.util.Map;

/**
 * Статический класс хранит неизменяемый Map, который создает связь между типом генерала и соответствующим классом
 * Drawer. Эти классы рисуют портреты генералов.
 * По сути, вместе с IGeneralDrawer, это урезанная реализация паттерна Strategy.
 */
public class SelectedGeneralMap {
    private static final Map<Integer, String> selectedGeneralMap;
    static {
        selectedGeneralMap = Map.of(
               1 , "COMMANDER",
               2 , "SNIPER",
               3 , "ARCHMAGE"
        );
    }

    public static String getDrawer(final Integer i) {
        return selectedGeneralMap.get(i);
    }



}
