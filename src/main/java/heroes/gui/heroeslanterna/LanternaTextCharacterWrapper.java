package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.TextCharacter;
import heroes.gui.heroeslanterna.utils.Colors;

/**
 * Обертка над элементами класса TextCharacter библиотеки Lanterna. Необходим для сокращения конструкций построения
 * TextCharacter.
 */
public class LanternaTextCharacterWrapper {
    public TextCharacter getTC(final char c, final Colors color) {
        return TextCharacter.DEFAULT_CHARACTER.withCharacter(c).withForegroundColor(color.color());
    }
}
