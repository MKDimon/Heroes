package heroes.gui.heroeslanterna.menudrawers.unitmenudrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.heroeslanterna.LanternaLineDrawer;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.mathutils.Pair;

public class BowmanMenuDrawer implements IUnitMenuDrawer {
    @Override
    public TextImage formTextImage(final boolean isSelected) {
        final TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));

        TextCharacter bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('(').withForegroundColor(Colors.BROWN.color());
        TextCharacter bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter(')').withForegroundColor(Colors.BROWN.color());
        TextCharacter slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.BROWN.color());
        TextCharacter backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(Colors.BROWN.color());
        TextCharacter sharp = TextCharacter.DEFAULT_CHARACTER.withCharacter('#').withForegroundColor(Colors.GREEN.color());
        TextCharacter underscore = TextCharacter.DEFAULT_CHARACTER.withCharacter('_').withForegroundColor(Colors.BROWN.color());

        ti.setCharacterAt(2, 0, bracket_left);
        ti.setCharacterAt(2, 7, bracket_left);
        ti.setCharacterAt(4, 2, bracket_right);
        ti.setCharacterAt(4, 5, bracket_right);
        ti.setCharacterAt(3, 1, backslash);
        ti.setCharacterAt(3, 6, slash);
        ti.setCharacterAt(0, 3, sharp);
        ti.setCharacterAt(0, 4, sharp);
        ti.setCharacterAt(1, 3, sharp);
        ti.setCharacterAt(1, 4, sharp);
        ti.setCharacterAt(2, 3, underscore);
        ti.setCharacterAt(3, 3, underscore);
        ti.setCharacterAt(4, 3, underscore);
        ti.setCharacterAt(5, 3, underscore);
        ti.setCharacterAt(6, 3, underscore);
        slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.RED.color());
        backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(Colors.RED.color());
        ti.setCharacterAt(7, 3, backslash);
        ti.setCharacterAt(7, 4, slash);

        return ti;
    }

    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isSelected) {
        tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()),
                                                        formTextImage(isSelected));

        if (isSelected) {
            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() - 1, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.GOLD);

            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() + 8, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.GOLD);
        } else {
            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() - 1, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.WHITE);

            LanternaLineDrawer.drawVerticalLine(tw, topLeftCorner.getX() + 8, topLeftCorner.getY() - 1,
                    topLeftCorner.getY() + 8, '|', Colors.WHITE);
        }
    }
}
