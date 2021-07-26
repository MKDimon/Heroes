package heroes.gui;

import com.googlecode.lanterna.TextCharacter;
import heroes.gui.utils.TextColorMap;

/**
 * Обертка над элементами класса TextCharacter библиотеки Lanterna. Необходим для сокращения конструкций построения
 * TextCharacter.
 */
public class TextCharacterWrapper {
    public TextCharacter getTC(final char c, final String color) {
        return TextCharacter.DEFAULT_CHARACTER.withCharacter(c).withForegroundColor(TextColorMap.getColor(color));
    }
}
