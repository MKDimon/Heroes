package heroes.gui.heroeslanterna;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.heroeslanterna.selectiondrawers.LanternaAnswerDrawer;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.gui.heroeslanterna.utils.Side;
import heroes.gui.heroeslanterna.utils.UnitDrawersMap;
import heroes.gui.heroeslanterna.utils.UnitTerminalGrid;
import heroes.mathutils.Position;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Обертка над Lanterna для сокращения кода, который управляет процессами запуска терминала.
 */
public class LanternaWrapper {
    private static final Logger logger = LoggerFactory.getLogger(LanternaWrapper.class);
    final private Screen screen;
    final private Terminal terminal;

    final private TerminalSize terminalSize;

    /**
     * Конструктор по умолчанию вызывает DefaultTerminalFactory, задает некоторые начальные настройки
     * (стартовый размер экрана и название окна) и создает терминал и screen в нём.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public LanternaWrapper() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        TerminalSize ts = new TerminalSize(150, 50);
        terminalSize = ts;
        defaultTerminalFactory.setInitialTerminalSize(ts);
        defaultTerminalFactory.setTerminalEmulatorTitle("Heroes");
        terminal = defaultTerminalFactory.createTerminal();
        screen = new TerminalScreen(terminal);
    }

    /**
     * Обертка над медотом clear(). Удаляет все данные из терминала.
     */
    private void clean() {
        screen.clear();
    }

    public void refresh() {
        try {
            screen.refresh();
        } catch (IOException e) {
            logger.error("Cannot refresh terminal.", e);
        }
    }

    /**
     * Обертка над методом startScreen(). Запускает окно терминала.
     */
    public void start() {
        try {
            screen.startScreen();
        } catch (IOException e) {
            logger.error("Cannot start terminal.", e);
        }
    }

    public TextGraphics newTG() {
        return screen.newTextGraphics();
    }

    public void printPlayer(final Fields field) {
        final int x_start = (field == Fields.PLAYER_TWO) ? terminalSize.getColumns() - 34 : 1;
        final int y_start = 25;
        final TextGraphics tg = this.newTG();
        tg.setForegroundColor(Colors.GOLD.color());
        tg.setModifiers(EnumSet.of(SGR.ITALIC));
        tg.putString(x_start + 10, y_start + 1, "(YOU ARE HERE)");
        tg.setForegroundColor(Colors.WHITE.color());

        refresh();
    }

    public int updateMenu() {
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(SimpleTheme.makeTheme(
                false,
                Colors.WHITE.color(),
                Colors.BLACK.color(),
                Colors.WHITE.color(),
                Colors.STEELGRAY.color(),
                Colors.STEELGRAY.color(),
                Colors.WHITE.color(),
                Colors.BLACK.color()));

        final TextInputDialog textInputDialog = new TextInputDialogBuilder()
                .setTitle("Choose game room")
                .setDescription("Enter a single number (0-9)")
                .setValidationPattern(Pattern.compile("[0-9]"), "You didn't enter a single number!")
                .setTextBoxSize(new TerminalSize(10, 1))
                .build();

        textInputDialog.setHints(List.of(Window.Hint.FIXED_POSITION));
        textInputDialog.setPosition(new TerminalPosition(7, 10));

        final ImageComponent ic = new ImageComponent();
        ic.setTextImage(LanternaMenuPictureDrawer.drawPicture(this));
        final Panel panel = new Panel();
        panel.addComponent(ic);

        final Window win = new BasicWindow();
        win.setComponent(ic);
        win.setHints(List.of(Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
        win.setPosition(new TerminalPosition(0, 0));
        win.setFixedSize(new TerminalSize(terminalSize.getColumns() - 2, terminalSize.getRows() - 2));

        textGUI.addWindow(win);
        final String input = textInputDialog.showDialog(textGUI);

        return Integer.parseInt(input);
    }

    /**
     * Основной метод отрисовки GUI, по сути точка входа в инфраструктуру. Вызывает метод clean(), затем все методы
     * отрисовки, после чего вызывает метод refresh() для переноса данных из внутреннего буфера Lanterna на терминал.
     * @param answer отвечает за вызванное ботом (игроком) действие, необходим для отрисовки действий.
     * @param board передается для отрисовки актуального состояния игрового поля.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     * @throws UnitException исключение из методов класса Board также пробрасывается выше.
     */
    public void update(final Answer answer, final Board board) throws IOException, UnitException {
        clean();
        LanternaBorderDrawer.drawBorders(this);

        final UnitTerminalGrid utg = new UnitTerminalGrid(this);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                //draw units of 1st army
                if (board.getArmy(Fields.PLAYER_ONE) != null) {
                    LanternaGeneralDrawer.drawGeneral(this, board.getGeneralPlayerOne(), Side.LHS);

                    UnitDrawersMap.getDrawer(board.getFieldPlayerOne()[i][j].getActionType())
                            .draw(this, utg.getPair(new Position(i, j, Fields.PLAYER_ONE)),
                                    board.getFieldPlayerOne()[i][j] == board.getGeneralPlayerOne());
                    //draw status of all units
                    LanternaStatusDrawer.invokeAllStatusDrawers(this,
                            utg.getPair(new Position(i, j, Fields.PLAYER_ONE)), board.getFieldPlayerOne()[i][j]);
                }
                //draw units of 2nd army
                if (board.getArmy(Fields.PLAYER_TWO) != null) {
                    LanternaGeneralDrawer.drawGeneral(this, board.getGeneralPlayerTwo(), Side.RHS);

                    UnitDrawersMap.getDrawer(board.getFieldPlayerTwo()[i][j].getActionType())
                            .draw(this, utg.getPair(new Position(i, j, Fields.PLAYER_TWO)),
                                    board.getFieldPlayerTwo()[i][j] == board.getGeneralPlayerTwo());
                    //draw status of all units
                    LanternaStatusDrawer.invokeAllStatusDrawers(this,
                            utg.getPair(new Position(i, j, Fields.PLAYER_TWO)), board.getFieldPlayerTwo()[i][j]);
                }

                if (answer != null && board.getArmy(Fields.PLAYER_ONE) != null && board.getArmy(Fields.PLAYER_TWO) != null) {
                    LanternaAnswerDrawer.drawAnswer(this, answer);
                    LanternaInformationDrawer.drawInfo(this, board);
                }
            }
        }

        refresh();
    }

    /**
     * Обертка над методом stopScreen(). Закрывает терминал и очищает внутренний буфер Лантерны.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public void stop() throws IOException {
        screen.stopScreen();
    }

    public Screen getScreen() {
        return screen;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public TerminalSize getTerminalSize() {
        return terminalSize;
    }
}
