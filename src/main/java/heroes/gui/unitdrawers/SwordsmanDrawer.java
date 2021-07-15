package heroes.gui.unitdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.TextColorMap;
import heroes.mathutils.Pair;

public class SwordsmanDrawer implements IUnitDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));
        TextCharacter square_bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('[').withForegroundColor(TextColorMap.getColor("silver"));
        TextCharacter square_bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter(']').withForegroundColor(TextColorMap.getColor("silver"));
        TextCharacter figure_bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('{').withForegroundColor(TextColorMap.getColor("steelgray"));
        TextCharacter figure_bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter('}').withForegroundColor(TextColorMap.getColor("steelgray"));
        TextCharacter slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(TextColorMap.getColor("silver"));
        TextCharacter minus = TextCharacter.DEFAULT_CHARACTER.withCharacter('-').withForegroundColor(TextColorMap.getColor("silver"));
        TextCharacter o = TextCharacter.DEFAULT_CHARACTER.withCharacter('o').withForegroundColor(TextColorMap.getColor("steelgray"));
        TextCharacter zero = TextCharacter.DEFAULT_CHARACTER.withCharacter('0').withForegroundColor(TextColorMap.getColor("red"));
        TextCharacter apostrophe = TextCharacter.DEFAULT_CHARACTER.withCharacter('\'').withForegroundColor(TextColorMap.getColor("brown"));
        TextCharacter acute = TextCharacter.DEFAULT_CHARACTER.withCharacter('`').withForegroundColor(TextColorMap.getColor("brown"));
        TextCharacter pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(TextColorMap.getColor("silver"));


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
            pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(TextColorMap.getColor("indigo"));
        }

        ti.setCharacterAt(3, 1, pipe);
        ti.setCharacterAt(3, 2, pipe);
        ti.setCharacterAt(3, 3, pipe);
        ti.setCharacterAt(3, 4, pipe);
        if (isGeneral) {
            pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(TextColorMap.getColor("gold"));
            slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(TextColorMap.getColor("gold"));
            apostrophe = TextCharacter.DEFAULT_CHARACTER.withCharacter('\'').withForegroundColor(TextColorMap.getColor("gold"));
            acute = TextCharacter.DEFAULT_CHARACTER.withCharacter('`').withForegroundColor(TextColorMap.getColor("gold"));
            zero = TextCharacter.DEFAULT_CHARACTER.withCharacter('0').withForegroundColor(TextColorMap.getColor("blue"));
        } else {
            pipe = TextCharacter.DEFAULT_CHARACTER.withCharacter('|').withForegroundColor(TextColorMap.getColor("brown"));
            slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(TextColorMap.getColor("brown"));
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
    public void draw(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isGeneral) {
        tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()),
                formTextImage(isGeneral));
    }
}
