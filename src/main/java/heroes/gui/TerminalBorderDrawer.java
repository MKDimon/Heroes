package heroes.gui;

import java.io.IOException;

public class TerminalBorderDrawer {
    public static void drawBorders(final TerminalWrapper tw) throws IOException {
        drawInnerBorders(tw, "white");
        drawLogLine(tw, "orange");
        drawGeneralZone(tw, "red");
        drawBattlefield(tw, "grey");
    }

    private static void drawInnerBorders(final TerminalWrapper tw, final String color) throws IOException {
        TerminalLineDrawer.drawHorizontalLine(tw,1, tw.getTerminal().getTerminalSize().getColumns() - 2, 0, '=', color);
        TerminalLineDrawer.drawHorizontalLine(tw, 1, tw.getTerminal().getTerminalSize().getColumns() - 2,
                tw.getTerminal().getTerminalSize().getRows() - 1, '=', color);
        TerminalLineDrawer.drawVerticalLine(tw, 0, 0, tw.getTerminal().getTerminalSize().getRows() - 1, '|', color);
        TerminalLineDrawer.drawVerticalLine(tw, tw.getTerminal().getTerminalSize().getColumns() - 1, 0,
                tw.getTerminal().getTerminalSize().getRows() - 1, '|', color);
    }

    private static void drawLogLine(final TerminalWrapper tw, final String color) throws IOException {
        TerminalLineDrawer.drawHorizontalLine(tw, 1, tw.getTerminal().getTerminalSize().getColumns() - 2,
                tw.getTerminal().getTerminalSize().getRows() -
                        (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3), '=', color);
    }

    private static void drawGeneralZone(final TerminalWrapper tw, final String color) throws  IOException {
        TerminalLineDrawer.drawVerticalLine(tw, 35, 1, tw.getTerminal().getTerminalSize().getRows() -
                (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3) - 1, '|', color);
        TerminalLineDrawer.drawHorizontalLine(tw, 1, 34, 25, '=', color);

        TerminalLineDrawer.drawVerticalLine(tw, tw.getTerminal().getTerminalSize().getColumns() - 35, 1, tw.getTerminal().getTerminalSize().getRows() -
                (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3) - 1, '|', color);
        TerminalLineDrawer.drawHorizontalLine(tw, tw.getTerminal().getTerminalSize().getColumns() - 34,
                tw.getTerminal().getTerminalSize().getColumns() - 2, 25, '=', color);
    }

    private static void drawBattlefield(final TerminalWrapper tw, final String color) throws IOException {
        int center = tw.getTerminal().getTerminalSize().getColumns() / 2;
        TerminalLineDrawer.drawHorizontalLine(tw,  center - 35,center + 35, 3, '=', color);
        TerminalLineDrawer.drawHorizontalLine(tw,  center - 35,center + 35, 34, '=', color);
        TerminalLineDrawer.drawVerticalLine(tw, center - 35, 3, 34, '|', color);
        TerminalLineDrawer.drawVerticalLine(tw, center + 35, 3, 34, '|', color);

    }
}
