package heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.graphics.TextGraphics;
import gamecore.units.Unit;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import gamecore.mathutils.Pair;

public class HealthDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        tg.setForegroundColor(Colors.RED.color());
        int currentHP = unit.getCurrentHP();
        if (currentHP <= 0) {
            currentHP = 0;
            DeathDrawer dd = new DeathDrawer();
            dd.draw(tw, topLeftCorner, unit);
        }
        tg.putString(topLeftCorner.getX() + 1, topLeftCorner.getY() + 8, "HP: " + currentHP);

        tg.setForegroundColor(Colors.WHITE.color());
    }
}
