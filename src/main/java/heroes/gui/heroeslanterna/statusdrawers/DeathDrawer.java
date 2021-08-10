package heroes.gui.heroeslanterna.statusdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import gamecore.units.Unit;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import gamecore.mathutils.Pair;

public class DeathDrawer implements IStatusDrawer {
    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final Unit unit) {
        final TextImage ti = new BasicTextImage(new TerminalSize(1, 1), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));

        for (int i = 0; i < 8; i++) {
            TextCharacter def = tw.getScreen().getFrontCharacter(topLeftCorner.getX() + i, topLeftCorner.getY() + i);
            TextCharacter temp = def.withBackgroundColor(Colors.DARKESTRED.color());
            ti.setCharacterAt(0, 0, temp);
            tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX() + i, topLeftCorner.getY()+ i), ti);
        }
    }
}
