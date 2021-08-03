package heroes.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;

public class TerminalMenuPictureDrawer {
    public static TextImage drawPicture(final TerminalWrapper tw) {
        int y_start = 2;
        int x_start = 35;

        TextImage ti = new BasicTextImage(new TerminalSize(150, 50), TextCharacter.DEFAULT_CHARACTER.withCharacter(' '));
        TextGraphics tg = ti.newTextGraphics();
        tg.putString(x_start, y_start, "           ________________________________________________________");
        tg.putString(x_start, y_start + 1, "          |    _     _   _____    ____       __     _____      __  |");
        tg.putString(x_start, y_start + 2, "          |    /    /    /    '   /    )   /    )   /    '   /    )|");
        tg.putString(x_start, y_start + 3, "          |   /___ /    /___     /___ /   /    /   /___      \\     |");
        tg.putString(x_start, y_start + 4, "          |  /    /    /        /    |   /    /   /           \\    |");
        tg.putString(x_start, y_start + 5, "          |_/_  _/_   /____    /     |  (____/   /____   (____/    |");
        tg.putString(x_start, y_start + 6, "          |________________________________________________________|");
        tg.putString(x_start, y_start + 7, "                                 ____                               ");
        tg.putString(x_start, y_start + 8, "                              .-\"    `-.      ,                     ");
        tg.putString(x_start, y_start + 9, "                            .'          '.   /j\\                    ");
        tg.putString(x_start, y_start + 10, "                           /              \\,/:/#\\                /\\ ");
        tg.putString(x_start, y_start + 11, "                          ;              ,//' '/#\\              //#\\ ");
        tg.putString(x_start, y_start + 12, "                          |             /' :   '/#\\            /  /#\\ ");
        tg.putString(x_start, y_start + 13, "                          :         ,  /' /'    '/#\\__..--\"\"\"\"/    /#\\__      ");
        tg.putString(x_start, y_start + 14, "                           \\       /'\\'-._:__    '/#\\        ;      /#, \"\"\"---");
        tg.putString(x_start, y_start + 15, "                            `-.   / ;#\\']\" ; \"\"\"--./#J       ':____...!       ");
        tg.putString(x_start, y_start + 16, "                               `-/   /#\\  J  [;[;[;Y]         |      ;     ");
        tg.putString(x_start, y_start + 17, "\"\"\"\"\"\"---....             __.--\"/    '/#\\ ;   \" \"  |     !    |   #! |   ");
        tg.putString(x_start, y_start + 18, "             \"\"--.. _.--\"\"     /      ,/#\\'-..____.;_,   |    |   '  |   ");
        tg.putString(x_start, y_start + 19, "                   \"-.        :_....___,/#} \"####\" | '_.-\",   | #['  |   ");
        tg.putString(x_start, y_start + 20, "                      '-._      |[;[;[;[;|         |.;'  /;\\  |      |  ");
        tg.putString(x_start, y_start + 21, "                      ,   `-.   |        :     _   .;'    /;\\ |   #\" |  ");
        tg.putString(x_start, y_start + 22, "                      !      `._:      _  ;   ##' .;'      /;\\|  _,  |   ");
        tg.putString(x_start, y_start + 23, "                     .#\\\"\"\"---..._    ';, |      .;{___     /;\\  ]#' |__....--");
        tg.putString(x_start, y_start + 24, "          .--.      ;'/#\\         \\    ]! |       \"| , \"\"\"--./_J    /         ");
        tg.putString(x_start, y_start + 25, "         /  '%;    /  '/#\\         \\   !' :        |!# #! #! #|    :`.__      ");
        tg.putString(x_start, y_start + 26, "        i__..'%] _:_   ;##J         \\      :\"#...._!   '  \"  \"|__  |    `--.._");
        tg.putString(x_start, y_start + 27, "         | .--\"\"\" !|\"\"\"\"  |\"\"\"----...J     | '##\"\" `-._       |  \"\"\"---.._    ");
        tg.putString(x_start, y_start + 28, "     ____: |      #|      |         #|     |          \"]      ;   ___...-\"T,  ");
        tg.putString(x_start, y_start + 29, "    /   :  :      !|      |   _______!_    |           |__..--;\"\"\"     ,;MM;");
        tg.putString(x_start, y_start + 30, "   :____| :    .-.#|      |  /\\      /#\\   |          /'               ''MM;");
        tg.putString(x_start, y_start + 31, "    |\"\"\": |   /   \\|   .----+  ;      /#\\  :___..--\"\";                  ,'MM;");
        tg.putString(x_start, y_start + 32, "   _Y--:  |  ;     ;.-'      ;  \\______/#: /         ;                  ''MM; ");
        tg.putString(x_start, y_start + 33, "  /    |  | ;_______;     ____!  |\"##\"\"\"MM!         ;                    ,'MM;");
        tg.putString(x_start, y_start + 34, " !_____|  |  |\"#\"#\"|____.'\"\"##\"  |       :         ;                     ''MM ");
        tg.putString(x_start, y_start + 35, "  | \"\"\"\"--!._|     |##\"\"         !       !         :____.....-------\"\"\"\"\"\" |'");
        tg.putString(x_start, y_start + 36, "  |          :     |______                        ___!_ \"#\"\"#\"\"#\"\"\"#\"\"\"#\"\"\"| ");
        tg.putString(x_start, y_start + 37, "__|          ;     |MM\"MM\"\"\"\"\"---..._______...--\"\"MM\"MM]                   | ");
        tg.putString(x_start, y_start + 38, "  \"\\-.      :      |#                                  :                   |  ");
        tg.putString(x_start, y_start + 40, "    /#'.    |      /##,                                !                   |  ");
        tg.putString(x_start, y_start + 41, "   .',/'\\   |       #:#,                                ;       .==.       |  ");
        tg.putString(x_start, y_start + 42, "  /\"\\'#\"\\',.|       ##;#,                               !     ,'||||',     |  ");
        tg.putString(x_start, y_start + 43, "        /;/`:       ######,          ____             _ :     M||||||M     |  ");
        tg.putString(x_start, y_start + 44, "       ###          /;\"\\.__\"-._   \"\"\"                   |===..M!!!!!!M_____| ");
        tg.putString(x_start, y_start + 45, "                           `--..`--.._____             _!_                  ");
        tg.putString(x_start, y_start + 46, "                                          `--...____,=\"_.'`-.____           ");
        return ti;
    }
}
