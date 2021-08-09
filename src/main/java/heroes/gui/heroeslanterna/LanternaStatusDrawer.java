package heroes.gui.heroeslanterna;

import heroes.gui.heroeslanterna.statusdrawers.ActiveDrawer;
import heroes.gui.heroeslanterna.statusdrawers.DefenseDrawer;
import heroes.gui.heroeslanterna.statusdrawers.HealthDrawer;
import heroes.mathutils.Pair;
import heroes.units.Unit;

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
        HealthDrawer hd = new HealthDrawer();
        hd.draw(tw, topLeftCorner, unit);
        ActiveDrawer ad = new ActiveDrawer();
        ad.draw(tw, topLeftCorner, unit);
        DefenseDrawer dd = new DefenseDrawer();
        dd.draw(tw, topLeftCorner, unit);
    }
}
