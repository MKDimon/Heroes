package heroes.gui.utils;

import com.googlecode.lanterna.TextColor;

/**
 * Класс хранит неизменяемый Map RGB цветов. Так как Lanterna не предоставляет удобных инструментов для работы с
 * цветами, то была создана эта обертка. Названию цвета соответствует статическая переменная Лантерны, которую можно
 * использовать в некоторых её методах.
 */
public enum Colors {
    ORANGE(new TextColor.RGB(253, 134, 18)),
    PINK(new TextColor.RGB(255,105,180)),
    RED(new TextColor.RGB(192, 0, 0)),
    DARKRED(new TextColor.RGB(139,0,0)),
    DARKESTRED(new TextColor.RGB(90,0,0)),
    BLACK(new TextColor.RGB(0, 0, 0)),
    BLUE(new TextColor.RGB(0,0,240)),
    LIGHTBLUE(new TextColor.RGB(65,105,225)),
    INDIGO(new TextColor.RGB(75,0,130)),
    SILVER(new TextColor.RGB(192,192,192)),
    STEELGRAY(new TextColor.RGB(119,136,153)),
    LIGHTGRAY(new TextColor.RGB(211,211,211)),
    GREEN(new TextColor.RGB(34,139,34)),
    LIGHTGREEN(new TextColor.RGB(124,252,0)),
    BROWN(new TextColor.RGB(139,69,19)),
    GOLD(new TextColor.RGB(255,215,0)),
    WHITE(new TextColor.RGB(255,255,255)),
    ;

    private final TextColor rgb;

    Colors(TextColor rgb) {
        this.rgb = rgb;
    }

    public TextColor color() {
        return rgb;
    }
}
