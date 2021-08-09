package heroes.gui.menudrawers.generalmenudrawers;

import java.util.Map;

/**
 * Статический класс хранит неизменяемый Map, который создает связь между типом генерала и соответствующим классом
 * Drawer. Эти классы рисуют портреты генералов.
 * По сути, вместе с IGeneralDrawer, это урезанная реализация паттерна Strategy.
 */
public class GeneralMenuDrawersMap {
    private static final Map<String, IGeneralMenuDrawer> generalDrawersMap;
    static {
        generalDrawersMap = Map.of(
                "COMMANDER", new CommanderMenuDrawer(),
                "SNIPER", new SniperMenuDrawer(),
                "ARCHMAGE", new PriestMenuDrawer()
        );
    }

    public static IGeneralMenuDrawer getDrawer(final String s) {
        return generalDrawersMap.get(s);
    }



}
