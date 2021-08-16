package heroes.gui.heroeslanterna.menudrawers;

import com.googlecode.lanterna.TerminalPosition;
import heroes.gui.heroeslanterna.LanternaLineDrawer;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.utils.Colors;

public class MenuBoardDrawer {
    public static void drawBorders(final LanternaWrapper tw, final TerminalPosition topLeft,
                                   final TerminalPosition botRight) {
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn(), topLeft.getRow(),
                botRight.getRow(), '|', Colors.WHITE);
        LanternaLineDrawer.drawVerticalLine(tw, botRight.getColumn(), topLeft.getRow(),
                botRight.getRow(), '|', Colors.WHITE);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn(), botRight.getColumn(),
                topLeft.getRow(), '=', Colors.WHITE);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn(), botRight.getColumn(),
                botRight.getRow(), '=', Colors.WHITE);
    }

    public static void drawUnitBorders(final LanternaWrapper tw, final TerminalPosition topLeft,
                                       final TerminalPosition botRight, final int selectedPosition) {
        Colors color;
        if (selectedPosition == 1)
            color = Colors.GOLD;
        else
            color = Colors.WHITE;
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 2, topLeft.getRow() + 2,
                topLeft.getRow() + 12, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 12, topLeft.getRow() + 2,
                topLeft.getRow() + 12, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 3, topLeft.getColumn() + 11,
                topLeft.getRow() + 2, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 3, topLeft.getColumn() + 11,
                topLeft.getRow() + 12, '=', color);

        if (selectedPosition == 2)
            color = Colors.GOLD;
        else
            color = Colors.WHITE;
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 15, topLeft.getRow() + 2,
                topLeft.getRow() + 12, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 25, topLeft.getRow() + 2,
                topLeft.getRow() + 12, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 16, topLeft.getColumn() + 24,
                topLeft.getRow() + 2, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 16, topLeft.getColumn() + 24,
                topLeft.getRow() + 12, '=', color);

        if (selectedPosition == 3)
            color = Colors.GOLD;
        else
            color = Colors.WHITE;
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 2, topLeft.getRow() + 13,
                topLeft.getRow() + 23, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 12, topLeft.getRow() + 13,
                topLeft.getRow() + 23, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 3, topLeft.getColumn() + 11,
                topLeft.getRow() + 13, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 3, topLeft.getColumn() + 11,
                topLeft.getRow() + 23, '=', color);

        if (selectedPosition == 4)
            color = Colors.GOLD;
        else
            color = Colors.WHITE;
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 15, topLeft.getRow() + 13,
                topLeft.getRow() + 23, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 25, topLeft.getRow() + 13,
                topLeft.getRow() + 23, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 16, topLeft.getColumn() + 24,
                topLeft.getRow() + 13, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 16, topLeft.getColumn() + 24,
                topLeft.getRow() + 23, '=', color);

        if (selectedPosition == 5)
            color = Colors.GOLD;
        else
            color = Colors.WHITE;
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 2, topLeft.getRow() + 24,
                topLeft.getRow() + 34, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 12, topLeft.getRow() + 24,
                topLeft.getRow() + 34, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 3, topLeft.getColumn() + 11,
                topLeft.getRow() + 24, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 3, topLeft.getColumn() + 11,
                topLeft.getRow() + 34, '=', color);

        if (selectedPosition == 6)
            color = Colors.GOLD;
        else
            color = Colors.WHITE;
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 15, topLeft.getRow() + 24,
                topLeft.getRow() + 34, '|', color);
        LanternaLineDrawer.drawVerticalLine(tw, topLeft.getColumn() + 25, topLeft.getRow() + 24,
                topLeft.getRow() + 34, '|', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 16, topLeft.getColumn() + 24,
                topLeft.getRow() + 24, '=', color);
        LanternaLineDrawer.drawHorizontalLine(tw, topLeft.getColumn() + 16, topLeft.getColumn() + 24,
                topLeft.getRow() + 34, '=', color);
    }

}
