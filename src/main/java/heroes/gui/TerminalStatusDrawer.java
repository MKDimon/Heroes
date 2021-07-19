package heroes.gui;

import heroes.gui.statusdrawers.ActiveDrawer;
import heroes.gui.statusdrawers.HealthDrawer;
import heroes.mathutils.Pair;
import heroes.units.Unit;

/**
 * Статический класс-обертка над группой классов рисования статусов юнита.
 */
public class TerminalStatusDrawer {
    /**
     * Вызывает все классы, рисующие статус юнита.
     * @param tw экземпляр класса TerminalWrapper для обращения непосредственно к Screen
     * @param topLeftCorner координаты верхнего левого угла, от которого ведется счет координат рисования юнита.
     * @param unit юнит, статус которого будет отрисован методом. Поле необходимо для получения самих статусов.
     */
    public static void invokeAllStatusDrawers(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner,
                                    final Unit unit){
        HealthDrawer hd = new HealthDrawer();
        hd.draw(tw, topLeftCorner, unit);
        ActiveDrawer ad = new ActiveDrawer();
        ad.draw(tw, topLeftCorner, unit);

    }
}
