package heroes.gui.heroeslanterna;

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

    @Override
    public void clear() {
        lw.getScreen().clear();
    }

    @Override
    public void pollInput() {
        try {
            lw.getScreen().pollInput();
        } catch (IOException e) {
            logger.error("Error poll input by Lanterna");
        }
    }

}
