package heroes.gui.menudrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.TerminalWrapper;

public class MenuTitleDrawers {
    public static void drawChooseGeneral(final TerminalWrapper tw, final TerminalPosition tp) {
        TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;
        tg.putString(tp.getColumn(), tp.getRow(), " ________________________________________________________________________________________________________________________ ");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|      __     _     _     __       __       __     _____          __     _____    __   __   _____    ____     __     _   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|    /    )   /    /    /    )   /    )   /    )   /    '       /    )   /    '   /|   /    /    '   /    )   / |    /   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|   /        /___ /    /    /   /    /    \\       /___         /        /__      / |  /    /___     /___ /   /__|   /    |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  /        /    /    /    /   /    /      \\     /            /  --,   /        /  | /    /        /    |   /   |  /     |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "| (____/  _/   _/_   (____/   (____/   (____/   /____        (____/   /____   _/_  |/    /____    /     |  /    | /____/ |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|________________________________________________________________________________________________________________________|");

    }

    public static void drawGeneralPosition(final TerminalWrapper tw, final TerminalPosition tp) {
        TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;
        tg.putString(tp.getColumn(), tp.getRow(), " _________________________________________________________________________________________________________________________________");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|     ____    _____    __   __   _____    ____     __     __        ____       __       __      __  ______  __     __     __   __ |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|    /    )   /    '   /|   /    /    '   /    )   / |    /         /    )   /    )   /    )    /     /     /    /    )   /|   /  |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|   /        /___     / |  /    /___     /___ /   /__|   /         /____/   /    /    \\        /     /     /    /    /   / |  /   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  /  --,   /        /  | /    /        /    |   /   |  /         /        /    /      \\      /     /     /    /    /   /  | /    |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "| (____/   /____   _/_  |/    /____    /     |  /    | /____/    /        (____/  (____/    _/_    /    _/_   (____/  _/_  |/     |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|_________________________________________________________________________________________________________________________________|");



    }
}
