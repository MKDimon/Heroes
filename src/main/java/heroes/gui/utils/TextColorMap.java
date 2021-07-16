package heroes.gui.utils;

import com.googlecode.lanterna.TextColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextColorMap {
    private static final Map<String, TextColor> textColorMap;
    static {
        Map<String, TextColor> temp = new HashMap<>();
        temp.put("orange", new TextColor.RGB(253, 134, 18));
        temp.put("red", new TextColor.RGB(192, 0, 0));
        temp.put("darkred", new TextColor.RGB(139,0,0));
        temp.put("darkestred", new TextColor.RGB(90,0,0));
        temp.put("black", new TextColor.RGB(0, 0, 0));
        temp.put("blue", new TextColor.RGB(0,0,240));
        temp.put("lightblue", new TextColor.RGB(65,105,225));
        temp.put("indigo", new TextColor.RGB(75,0,130));
        temp.put("silver", new TextColor.RGB(192,192,192));
        temp.put("steelgray", new TextColor.RGB(119,136,153));
        temp.put("lightgray", new TextColor.RGB(211,211,211));
        temp.put("green", new TextColor.RGB(34,139,34));
        temp.put("lightgreen", new TextColor.RGB(124,252,0));
        temp.put("brown", new TextColor.RGB(139,69,19));
        temp.put("gold", new TextColor.RGB(255,215,0));
        textColorMap = temp;
    }

    public static TextColor getColor(final String color) {
        return textColorMap.getOrDefault(color, new TextColor.RGB(255, 255, 255));
    }



}
