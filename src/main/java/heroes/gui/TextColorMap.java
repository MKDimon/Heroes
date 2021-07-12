package heroes.gui;

import com.googlecode.lanterna.TextColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextColorMap {
    private static final Map<String, TextColor> textColorMap;
    static {
        textColorMap = Map.of(
                "orange", new TextColor.RGB(253, 134, 18),
                "red", new TextColor.RGB(192, 0, 0)
        );
    }

    public static TextColor getColor(final String color) {
        return textColorMap.get(color);
    }



}
