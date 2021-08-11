package heroes.gui.heroeslanterna.unitdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;
import gamecore.mathutils.Pair;

public class SwordsmanDrawer implements IUnitDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        final TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));
        TextCharacter square_bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('[').withForegroundColor(Colors.SILVER.color());
        TextCharacter square_bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter(']').withForegroundColor(Colors.SILVER.color());
        TextCharacter figure_bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('{').withForegroundColor(Colors.STEELGRAY.color());
        TextCharacter figure_bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter('}').withForegroundColor(Colors.STEELGRAY.color());
        TextCharacter slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.SILVER.color());
        TextCharacter minus = TextCharacter.DEFAULT_CHARACTER.withCharacter('-').withForegroundColor(Colors.SILVER.color());
        TextCharacter o = TextCharacter.DEFAULT_CHARACTER.withCharacter('o').withForegroundColor(Colors.STEELGRAY.color());
        TextCharacter zero = TextCharacter.DEFAULT_CHARACTER.withCharacter('0').withForegroundColor(Colors.RED.color());
        TextCharacter apostrophe = TextCharacter.DEFAULT_CHARACTER.withCharacter('\'').withForegroundColor(Colors.BROWN.color());
        TextCharacter acute = TextCharacter.DEFAULT_CHARACTER.withCharacter('`').withForegroundColor(Colors.BROWN.color());
        TextCharacter pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(Colors.SILVER.color());


        ti.setCharacterAt(3, 0, slash);
        ti.setCharacterAt(4, 0, pipe);

        ti.setCharacterAt(2, 1, pipe);
        ti.setCharacterAt(3, 1, pipe);
        ti.setCharacterAt(4, 1, pipe);

        ti.setCharacterAt(2, 2, pipe);
        ti.setCharacterAt(3, 2, pipe);
        ti.setCharacterAt(4, 2, pipe);

        ti.setCharacterAt(2, 3, pipe);
        ti.setCharacterAt(3, 3, pipe);
        ti.setCharacterAt(4, 3, pipe);

        ti.setCharacterAt(2, 4, pipe);
        ti.setCharacterAt(3, 4, pipe);
        ti.setCharacterAt(4, 4, pipe);

        ti.setCharacterAt(0, 5, minus);
        ti.setCharacterAt(1, 5, square_bracket_left);
        ti.setCharacterAt(2, 5, figure_bracket_left);
        ti.setCharacterAt(3, 5, o);
        ti.setCharacterAt(4, 5, figure_bracket_right);
        ti.setCharacterAt(5, 5, square_bracket_right);
        ti.setCharacterAt(6, 5, minus);

        if (isGeneral) {
            pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(Colors.INDIGO.color());
        }

        ti.setCharacterAt(3, 1, pipe);
        ti.setCharacterAt(3, 2, pipe);
        ti.setCharacterAt(3, 3, pipe);
        ti.setCharacterAt(3, 4, pipe);
        if (isGeneral) {
            pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(Colors.GOLD.color());
            slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.GOLD.color());
            apostrophe = TextCharacter.DEFAULT_CHARACTER.withCharacter('\'').withForegroundColor(Colors.GOLD.color());
            acute = TextCharacter.DEFAULT_CHARACTER.withCharacter('`').withForegroundColor(Colors.GOLD.color());
            zero = TextCharacter.DEFAULT_CHARACTER.withCharacter('0').withForegroundColor(Colors.BROWN.color());
        } else {
            pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(Colors.BROWN.color());
            slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(Colors.BROWN.color());
        }


        ti.setCharacterAt(2, 6, pipe);
        ti.setCharacterAt(3, 6, slash);
        ti.setCharacterAt(4, 6, pipe);

        ti.setCharacterAt(2, 7, acute);
        ti.setCharacterAt(3, 7, zero);
        ti.setCharacterAt(4, 7, apostrophe);
        return ti;
    }

    @Override
    public void draw(final LanternaWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isGeneral) {
        tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()),
                formTextImage(isGeneral));
    }
}
