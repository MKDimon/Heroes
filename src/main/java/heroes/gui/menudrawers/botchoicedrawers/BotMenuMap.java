package heroes.gui.menudrawers.botchoicedrawers;

import heroes.gui.menudrawers.generalmenudrawers.CommanderMenuDrawer;
import heroes.gui.menudrawers.generalmenudrawers.IGeneralMenuDrawer;
import heroes.gui.menudrawers.generalmenudrawers.PriestMenuDrawer;
import heroes.gui.menudrawers.generalmenudrawers.SniperMenuDrawer;

import java.util.Map;

/**
 * Статический класс хранит неизменяемый Map, который создает связь между типом генерала и соответствующим классом
 * Drawer. Эти классы рисуют портреты генералов.
 * По сути, вместе с IGeneralDrawer, это урезанная реализация паттерна Strategy.
 */
public class BotMenuMap {
    private static final Map<Integer, String> botMap;
    static {
        botMap = Map.of(
                1, "Test",
                2, "Random",
                3, "Player",
                4, "PlayerGUI"
        );
    }

    public static String getDrawer(final Integer s) {
        return botMap.get(s);
    }



}
