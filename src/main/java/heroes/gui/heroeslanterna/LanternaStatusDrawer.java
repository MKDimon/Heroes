package heroes.gui.heroeslanterna;

import gamecore.units.Unit;
import heroes.gui.heroeslanterna.statusdrawers.ActiveDrawer;
import heroes.gui.heroeslanterna.statusdrawers.DefenseDrawer;
import heroes.gui.heroeslanterna.statusdrawers.HealthDrawer;
import gamecore.mathutils.Pair;

/**
 * Статический класс-обертка над группой классов рисования статусов юнита.
 */
public class LanternaStatusDrawer {
    /**
     * Вызывает все классы, рисующие статус юнита.
     * @param tw экземпляр класса LanternaWrapper для обращения непосредственно к Screen
     * @param topLeftCorner координаты верхнего левого угла, от которого ведется счет координат рисования юнита.
     * @param unit юнит, статус которого будет отрисован методом. Поле необходимо для получения самих статусов.
     */
    public static void invokeAllStatusDrawers(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner,
                                              final Unit unit){
        final HealthDrawer hd = new HealthDrawer();
        hd.draw(tw, topLeftCorner, unit);
        final ActiveDrawer ad = new ActiveDrawer();
        ad.draw(tw, topLeftCorner, unit);
        final DefenseDrawer dd = new DefenseDrawer();
        dd.draw(tw, topLeftCorner, unit);
    }
}
