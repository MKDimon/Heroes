package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.IGUI;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Обертка над LanternaWrapper, который управляет процессами запуска терминала.
 */

public class Lanterna implements IGUI {
    private static final Logger logger = LoggerFactory.getLogger(Lanterna.class);

    private LanternaWrapper lw;

    /**
     * Конструктор по умолчанию. Вызывает конструктор по умолчанию класса LanternaWrapper.
     **/

    public Lanterna() {
        try {
            this.lw = new LanternaWrapper();
        } catch (final IOException e) {
            logger.error("Error Lanterna creating", e);
        }
    }

    /**
     * Обертка над методом refresh().
     **/

    @Override
    public void refresh() {
        lw.refresh();
    }

    /**
     * Обертка над методом start(). Запускает окно терминала.
     */

    @Override
    public void start() {
        lw.start();
    }

    /**
     * Обертка над методом newTG().
     **/

    @Override
    public TextGraphics newTG() {
        return lw.newTG();
    }

    /**
     * Обертка над методом printPlayer().
     **/

    @Override
    public void printPlayer(final Fields field) {
        lw.printPlayer(field);
    }

    /**
     * Обертка над методом updateMenu().
     **/

    @Override
    public int updateMenu() {
        return lw.updateMenu();
    }

    /**
     * Обертка над методом update(). Основной метод отрисовки GUI.
     **/

    @Override
    public void update(final Answer answer, final Board board) {
        try {
            lw.update(answer, board);
        } catch (final IOException | UnitException e) {
            logger.error("Error updating by Lanterna", e);
        }
    }

    /**
     * Обертка над методом stop(). Закрывает терминал и очищает внутренний буфер Лантерны.
     **/

    @Override
    public void stop() {
        try {
            lw.stop();
        } catch (IOException e) {
            logger.error("Error Lanterna stopping", e);
        }
    }

    /**
     * Обертка над методом getScreen().
     **/

    @Override
    public Screen getScreen() {
        return lw.getScreen();
    }

    /**
     * Обертка над методом getTerminal().
     **/

    @Override
    public Terminal getTerminal() {
        return lw.getTerminal();
    }

    /**
     * Обертка над методом getTerminalSize().
     **/

    @Override
    public TerminalSize getTerminalSize() {
        return lw.getTerminalSize();
    }

}
