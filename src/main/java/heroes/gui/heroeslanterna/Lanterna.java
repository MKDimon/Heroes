package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.input.KeyStroke;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.controller.IController;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.IGUI;
import heroes.gui.heroeslanterna.menudrawers.botchoicedrawers.BotMenuMap;
import heroes.gui.heroeslanterna.menudrawers.botchoicedrawers.MenuBotDrawer;
import heroes.player.Answer;
import heroes.player.controlsystem.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.kerberos.KerberosTicket;
import javax.swing.*;
import java.io.IOException;

/**
 * Обертка над LanternaWrapper, который управляет процессами запуска терминала.
 */

public class Lanterna implements IGUI, IController {
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

    @Override
    public void drawBots(final Selector selector) {
        MenuBotDrawer.drawBots(lw, selector.getSelectedNumber());
    }

    @Override
    public void drawWait() {
        MenuBotDrawer.drawWait(lw);
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
    public KeyStroke pollInput() {
        try {
            return lw.getScreen().pollInput();
        } catch (IOException e) {
            logger.error("Error poll input by Lanterna");
        }
        return null;
    }

    @Override
    public int getFieldCommand() {
        return lw.updateMenu("Choose field (1-2 or 3 (any) )");
    }

    @Override
    public int getRoomCommand() {
        return lw.updateMenu("Choose room:");
    }

    @Override
    public String getBot(final Selector selector) {
        return BotMenuMap.getDrawer(selector.getSelectedNumber());
    }
    @Override
    public void endGame(final Data data) {
        LanternaEndGame.endGame(lw, data);
    }

    @Override
    public void continueGame(final Data data) {
        LanternaContinueGame.continueGame(lw, data);
    }

}
