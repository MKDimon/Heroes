package heroes.gui.unitdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.TerminalWrapper;
import heroes.gui.utils.TextColorMap;
import heroes.mathutils.Pair;

public class BowmanDrawer implements IUnitDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));

        TextCharacter bracket_left = TextCharacter.DEFAULT_CHARACTER.withCharacter('(').withForegroundColor(TextColorMap.getColor("brown"));
        TextCharacter bracket_right = TextCharacter.DEFAULT_CHARACTER.withCharacter(')').withForegroundColor(TextColorMap.getColor("brown"));
        TextCharacter slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(TextColorMap.getColor("brown"));
        TextCharacter backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(TextColorMap.getColor("brown"));
        TextCharacter sharp = TextCharacter.DEFAULT_CHARACTER.withCharacter('#').withForegroundColor(TextColorMap.getColor("green"));
        TextCharacter underscore = TextCharacter.DEFAULT_CHARACTER.withCharacter('_').withForegroundColor(TextColorMap.getColor("brown"));

        if (isGeneral) {
            sharp = TextCharacter.DEFAULT_CHARACTER.withCharacter('#').withForegroundColor(TextColorMap.getColor("silver"));
        }


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
        if (isGeneral) {
            slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(TextColorMap.getColor("gold"));
            backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(TextColorMap.getColor("gold"));
        } else {
            slash = TextCharacter.DEFAULT_CHARACTER.withCharacter('/').withForegroundColor(TextColorMap.getColor("red"));
            backslash = TextCharacter.DEFAULT_CHARACTER.withCharacter('\\').withForegroundColor(TextColorMap.getColor("red"));
        }

        ti.setCharacterAt(7, 3, backslash);
        ti.setCharacterAt(7, 4, slash);
        return ti;
    }

    @Override
    public void draw(final TerminalWrapper tw, final Pair<Integer, Integer> topLeftCorner, final boolean isGeneral) {
        tw.getScreen().newTextGraphics().drawImage(new TerminalPosition(topLeftCorner.getX(), topLeftCorner.getY()),
                                                        formTextImage(isGeneral));
    }
}
