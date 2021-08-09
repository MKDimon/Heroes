package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;

/**
 * Статический класс, формирующий главное меню приложения.
 */

public class LanternaMenuPictureDrawer {
    public static TextImage drawPicture(final LanternaWrapper tw) {
        int y_start = 0;
        final int x_start = tw.getTerminalSize().getColumns() / 2 - 35;

        TextImage ti = new BasicTextImage(tw.getTerminalSize(), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));
        TextGraphics tg = ti.newTextGraphics();
        tg.putString(x_start, y_start++, "           ________________________________________________________");
        tg.putString(x_start, y_start++, "          |    _     _   _____    ____       __     _____      __  |");
        tg.putString(x_start, y_start++, "          |    /    /    /    '   /    )   /    )   /    '   /    )|");
        tg.putString(x_start, y_start++, "          |   /___ /    /___     /___ /   /    /   /___      \\     |");
        tg.putString(x_start, y_start++, "          |  /    /    /        /    |   /    /   /           \\    |");
        tg.putString(x_start, y_start++, "          |_/_  _/_   /____    /     |  (____/   /____   (____/    |");
        tg.putString(x_start, y_start++, "          |________________________________________________________|");
        tg.putString(x_start, y_start++, "                                 ____                               ");
        tg.putString(x_start, y_start++, "                              .-\"    `-.      ,                     ");
        tg.putString(x_start, y_start++, "                            .'          '.   /j\\                    ");
        tg.putString(x_start, y_start++, "                           /              \\,/:/#\\                /\\ ");
        tg.putString(x_start, y_start++, "                          ;              ,//' '/#\\              //#\\ ");
        tg.putString(x_start, y_start++, "                          |             /' :   '/#\\            /  /#\\ ");
        tg.putString(x_start, y_start++, "                          :         ,  /' /'    '/#\\__..--\"\"\"\"/    /#\\__      ");
        tg.putString(x_start, y_start++, "                           \\       /'\\'-._:__    '/#\\        ;      /#, \"\"\"---");
        tg.putString(x_start, y_start++, "                            `-.   / ;#\\']\" ; \"\"\"--./#J       ':____...!       ");
        tg.putString(x_start, y_start++, "                               `-/   /#\\  J  [;[;[;Y]         |      ;     ");
        tg.putString(x_start, y_start++, "\"\"\"\"\"\"---....             __.--\"/    '/#\\ ;   \" \"  |     !    |   #! |   ");
        tg.putString(x_start, y_start++, "             \"\"--.. _.--\"\"     /      ,/#\\'-..____.;_,   |    |   '  |   ");
        tg.putString(x_start, y_start++, "                   \"-.        :_....___,/#} \"####\" | '_.-\",   | #['  |   ");
        tg.putString(x_start, y_start++, "                      '-._      |[;[;[;[;|         |.;'  /;\\  |      |  ");
        tg.putString(x_start, y_start++, "                      ,   `-.   |        :     _   .;'    /;\\ |   #\" |  ");
        tg.putString(x_start, y_start++, "                      !      `._:      _  ;   ##' .;'      /;\\|  _,  |   ");
        tg.putString(x_start, y_start++, "                     .#\\\"\"\"---..._    ';, |      .;{___     /;\\  ]#' |__....--");
        tg.putString(x_start, y_start++, "          .--.      ;'/#\\         \\    ]! |       \"| , \"\"\"--./_J    /         ");
        tg.putString(x_start, y_start++, "         /  '%;    /  '/#\\         \\   !' :        |!# #! #! #|    :`.__      ");
        tg.putString(x_start, y_start++, "        i__..'%] _:_   ;##J         \\      :\"#...._!   '  \"  \"|__  |    `--.._");
        tg.putString(x_start, y_start++, "         | .--\"\"\" !|\"\"\"\"  |\"\"\"----...J     | '##\"\" `-._       |  \"\"\"---.._    ");
        tg.putString(x_start, y_start++, "     ____: |      #|      |         #|     |          \"]      ;   ___...-\"T,  ");
        tg.putString(x_start, y_start++, "    /   :  :      !|      |   _______!_    |           |__..--;\"\"\"     ,;MM;");
        tg.putString(x_start, y_start++, "   :____| :    .-.#|      |  /\\      /#\\   |          /'               ''MM;");
        tg.putString(x_start, y_start++, "    |\"\"\": |   /   \\|   .----+  ;      /#\\  :___..--\"\";                  ,'MM;");
        tg.putString(x_start, y_start++, "   _Y--:  |  ;     ;.-'      ;  \\______/#: /         ;                  ''MM; ");
        tg.putString(x_start, y_start++, "  /    |  | ;_______;     ____!  |\"##\"\"\"MM!         ;                    ,'MM;");
        tg.putString(x_start, y_start++, " !_____|  |  |\"#\"#\"|____.'\"\"##\"  |       :         ;                     ''MM ");
        tg.putString(x_start, y_start++, "  | \"\"\"\"--!._|     |##\"\"         !       !         :____.....-------\"\"\"\"\"\" |'");
        tg.putString(x_start, y_start++, "  |          :     |______                        ___!_ \"#\"\"#\"\"#\"\"\"#\"\"\"#\"\"\"| ");
        tg.putString(x_start, y_start++, "__|          ;     |MM\"MM\"\"\"\"\"---..._______...--\"\"MM\"MM]                   | ");
        tg.putString(x_start, y_start++, "  \"\\-.      :      |#                                  :                   |  ");
        tg.putString(x_start, y_start++, "    /#'.    |      /##,                                !                   |  ");
        tg.putString(x_start, y_start++, "   .',/'\\   |       #:#,                                ;       .==.       |  ");
        tg.putString(x_start, y_start++, "  /\"\\'#\"\\',.|       ##;#,                               !     ,'||||',     |  ");
        tg.putString(x_start, y_start++, "        /;/`:       ######,          ____             _ :     M||||||M     |  ");
        tg.putString(x_start, y_start++, "       ###          /;\"\\.__\"-._   \"\"\"                   |===..M!!!!!!M_____| ");
        tg.putString(x_start, y_start++, "                           `--..`--.._____             _!_                  ");
        tg.putString(x_start, y_start++, "                                          `--...____,=\"_.'`-.____           ");
        return ti;
    }
}
