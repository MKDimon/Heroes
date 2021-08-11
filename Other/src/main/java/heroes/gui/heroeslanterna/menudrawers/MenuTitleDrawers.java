package heroes.gui.heroeslanterna.menudrawers;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.gui.heroeslanterna.LanternaWrapper;

public class MenuTitleDrawers {
    public static void drawChooseGeneral(final LanternaWrapper tw, final TerminalPosition tp) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;
        tg.putString(tp.getColumn(), tp.getRow(), " ________________________________________________________________________________________________________________________ ");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|      __     _     _     __       __       __     _____          __     _____    __   __   _____    ____     __     _   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|    /    )   /    /    /    )   /    )   /    )   /    '       /    )   /    '   /|   /    /    '   /    )   / |    /   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|   /        /___ /    /    /   /    /    \\       /___         /        /__      / |  /    /___     /___ /   /__|   /    |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  /        /    /    /    /   /    /      \\     /            /  --,   /        /  | /    /        /    |   /   |  /     |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "| (____/  _/   _/_   (____/   (____/   (____/   /____        (____/   /____   _/_  |/    /____    /     |  /    | /____/ |");
        tg.putString(tp.getColumn(), tp.getRow() + i, "|________________________________________________________________________________________________________________________|");

    }

    public static void drawGeneralPosition(final LanternaWrapper tw, final TerminalPosition tp) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;
        tg.putString(tp.getColumn(), tp.getRow(), " _________________________________________________________________________________________________________________________________");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|     ____    _____    __   __   _____    ____     __     __        ____       __       __      __  ______  __     __     __   __ |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|    /    )   /    '   /|   /    /    '   /    )   / |    /         /    )   /    )   /    )    /     /     /    /    )   /|   /  |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|   /        /___     / |  /    /___     /___ /   /__|   /         /____/   /    /    \\        /     /     /    /    /   / |  /   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  /  --,   /        /  | /    /        /    |   /   |  /         /        /    /      \\      /     /     /    /    /   /  | /    |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "| (____/   /____   _/_  |/    /____    /     |  /    | /____/    /        (____/  (____/    _/_    /    _/_   (____/  _/_  |/     |");
        tg.putString(tp.getColumn(), tp.getRow() + i, "|_________________________________________________________________________________________________________________________________|");
    }

    public static void drawChoose(final LanternaWrapper tw, final TerminalPosition tp) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;

        tg.putString(tp.getColumn(), tp.getRow(), " __________________________________________________________");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|      __     _     _     __       __       __     _____   |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|    /    )   /    /    /    )   /    )   /    )   /    '  |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|   /        /___ /    /    /   /    /    \\       /___     |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  /        /    /    /    /   /    /      \\     /         |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "| (____/  _/   _/_   (____/   (____/   (____/   /____      |");
        tg.putString(tp.getColumn(), tp.getRow() + i, "|__________________________________________________________|");
    }

    public static void drawTroops(final LanternaWrapper tw, final TerminalPosition tp) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;

        tg.putString(tp.getColumn(), tp.getRow(), " _____________________________________________________");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  ______   ____       __       __     ____       __  |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|    /      /    )   /    )   /    )   /    )   /    )|");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|   /      /___ /   /    /   /    /   /____/    \\     |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "|  /      /    |   /    /   /    /   /           \\    |");
        tg.putString(tp.getColumn(), tp.getRow() + i++, "| /      /     |  (____/   (____/   /       (____/    |");
        tg.putString(tp.getColumn(), tp.getRow() + i, "|_____________________________________________________|");
    }

    public static void drawArmyHereDisclaimer(final LanternaWrapper tw, final TerminalPosition tp) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;

        tg.putString(tp.getColumn(), tp.getRow(), "General! Our scouts report that great enemy host");
        tg.putString(tp.getColumn() + 9, tp.getRow() + i++, "is already on the battlefield. ");
        tg.putString(tp.getColumn() + 12, tp.getRow() + i, "Brace yourself, fellows!");

    }

    public static void drawArmyNotHereDisclaimer(final LanternaWrapper tw, final TerminalPosition tp) {
        final TextGraphics tg = tw.getScreen().newTextGraphics();
        int i = 1;

        tg.putString(tp.getColumn(), tp.getRow(), "General, enemy army is nearby. We have");
        tg.putString(tp.getColumn() + 4, tp.getRow() + i++, "some time before they arrive.");
        tg.putString(tp.getColumn() + 12, tp.getRow() + i, "Form your army!");

    }
}
