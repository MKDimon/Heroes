package heroes.gui.heroeslanterna.unitdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.mathutils.Pair;

public class HealerDrawer implements IUnitDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));
        TextCharacter bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('(').withForegroundColor(Colors.SILVER.color());
        TextCharacter bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter(')').withForegroundColor(Colors.SILVER.color());
        TextCharacter slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.SILVER.color());
        TextCharacter backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(Colors.SILVER.color());
        TextCharacter i = TextCharacter.DEFAULT_CHARACTER.withCharacter('I').withForegroundColor(Colors.BROWN.color());
        TextCharacter eight = TextCharacter.DEFAULT_CHARACTER.withCharacter('8').withForegroundColor(Colors.BROWN.color());

        ti.setCharacterAt(3, 0, slash);
        ti.setCharacterAt(4, 0, backslash);

        ti.setCharacterAt(3, 1, i);
        ti.setCharacterAt(4, 1, i);

        ti.setCharacterAt(2, 2, backslash);
        ti.setCharacterAt(3, 2, i);
        ti.setCharacterAt(4, 2, i);
        ti.setCharacterAt(5, 2, slash);

        ti.setCharacterAt(0, 3, bracket_left);
        ti.setCharacterAt(1, 3, eight);
        ti.setCharacterAt(2, 3, eight);
        ti.setCharacterAt(3, 3, i);
        ti.setCharacterAt(4, 3, i);
        ti.setCharacterAt(5, 3, eight);
        ti.setCharacterAt(6, 3, eight);
        ti.setCharacterAt(7, 3, bracket_right);

        ti.setCharacterAt(2, 4, slash);
        ti.setCharacterAt(3, 4, i);
        ti.setCharacterAt(4, 4, i);
        ti.setCharacterAt(5, 4, backslash);

        ti.setCharacterAt(3, 5, i);
        ti.setCharacterAt(4, 5, i);

        ti.setCharacterAt(3, 6, i);
        ti.setCharacterAt(4, 6, i);

        ti.setCharacterAt(3, 7, backslash);
        ti.setCharacterAt(4, 7, slash);

        return ti;
    }

    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isGeneral) {
        tw.newTG().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()), formTextImage(isGeneral));
    }
}
