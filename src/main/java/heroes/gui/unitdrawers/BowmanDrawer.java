package heroes.gui.unitdrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import heroes.gui.TerminalWrapper;
import heroes.gui.TextCharacterWrapper;
import heroes.gui.utils.TextColorMap;
import heroes.mathutils.Pair;

public class BowmanDrawer implements IUnitDrawer {
    @Override
    public TextImage formTextImage(final boolean isGeneral) {
        TextImage ti = new BasicTextImage(new TerminalSize(8, 8), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));

        final TextCharacterWrapper tcw = new TextCharacterWrapper();

        ti.setCharacterAt(2, 0, tcw.getTC('(', "brown"));
        ti.setCharacterAt(2, 7, tcw.getTC('(', "brown"));
        ti.setCharacterAt(4, 2, tcw.getTC(')', "brown"));
        ti.setCharacterAt(4, 5, tcw.getTC(')', "brown"));
        ti.setCharacterAt(3, 1, tcw.getTC('\\', "brown"));
        ti.setCharacterAt(3, 6, tcw.getTC('/', "brown"));

        TextCharacter sharp;
        if (isGeneral) {
            sharp = tcw.getTC('#', "silver");
        } else {
            sharp = tcw.getTC('#', "green");
        }
        ti.setCharacterAt(0, 3, sharp);
        ti.setCharacterAt(0, 4, sharp);
        ti.setCharacterAt(1, 3, sharp);
        ti.setCharacterAt(1, 4, sharp);
        ti.setCharacterAt(2, 3, tcw.getTC('_', "brown"));
        ti.setCharacterAt(3, 3, tcw.getTC('_', "brown"));
        ti.setCharacterAt(4, 3, tcw.getTC('_', "brown"));
        ti.setCharacterAt(5, 3, tcw.getTC('_', "brown"));
        ti.setCharacterAt(6, 3, tcw.getTC('_', "brown"));

        TextCharacter slash, backslash;
        if (isGeneral) {
            slash = tcw.getTC('/', "gold");
            backslash = tcw.getTC('\\', "gold");
        } else {
            slash = tcw.getTC('/', "red");
            backslash = tcw.getTC('\\', "red");
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
