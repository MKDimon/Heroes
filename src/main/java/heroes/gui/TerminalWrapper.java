package heroes.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.selectiondrawers.TerminalAnswerDrawer;
import heroes.gui.utils.Side;
import heroes.gui.utils.TextColorMap;
import heroes.gui.utils.UnitDrawersMap;
import heroes.gui.utils.UnitTerminalGrid;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.units.General;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Обертка над Lanterna для сокращения кода, который управляет процессами запуска терминала.
 */
public class TerminalWrapper {

    final private Screen screen;
    final private Terminal terminal;

    /**
     * Конструктор по умолчанию вызывает DefaultTerminalFactory, задает некоторые начальные настройки
     * (стартовый размер экрана и название окна) и создает терминал и screen в нём.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public TerminalWrapper() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(150, 50));
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

    private void refresh() throws IOException {
        screen.refresh();
    }

    /**
     * Обертка над методом startScreen(). Запускает окно терминала.
     * @throws IOException исключение из методов Лантерны пробрасывается выше.
     */
    public void start() throws IOException {
        screen.startScreen();
    }

    public int updateMenu() throws IOException {
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(SimpleTheme.makeTheme(
                false,
                TextColorMap.getColor("white"),
                TextColorMap.getColor("black"),
                TextColorMap.getColor("white"),
                TextColorMap.getColor("steelgray"),
                TextColorMap.getColor("steelgray"),
                TextColorMap.getColor("yellow"),
                TextColorMap.getColor("black")));

        String input = new TextInputDialogBuilder()
                .setTitle("Title")
                .setDescription("Enter a single number")
                .setValidationPattern(Pattern.compile("[0-9]"), "You didn't enter a single number!")
                .build()
                .showDialog(textGUI);

        refresh();
        return 1;
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
        TerminalBorderDrawer.drawBorders(this);

        UnitTerminalGrid utg = new UnitTerminalGrid(this);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                //draw units of 1st army
                if (board.getArmy(Fields.PLAYER_ONE) != null) {
                    General gen_one = board.getGeneralPlayerOne();
                    TerminalGeneralDrawer.drawGeneral(this, gen_one, Side.LHS);

                    UnitDrawersMap.getDrawer(board.getFieldPlayerOne()[i][j].getActionType())
                            .draw(this, utg.getPair(new Position(i, j, Fields.PLAYER_ONE)),
                                    board.getFieldPlayerOne()[i][j] == board.getGeneralPlayerOne());
                    //draw status of all units
                    TerminalStatusDrawer.invokeAllStatusDrawers(this,
                            utg.getPair(new Position(i, j, Fields.PLAYER_ONE)), board.getFieldPlayerOne()[i][j]);
                }
                //draw units of 2nd army
                if (board.getArmy(Fields.PLAYER_TWO) != null) {
                    General gen_two = board.getGeneralPlayerTwo();
                    TerminalGeneralDrawer.drawGeneral(this, gen_two, Side.RHS);
                    UnitDrawersMap.getDrawer(board.getFieldPlayerTwo()[i][j].getActionType())
                            .draw(this, utg.getPair(new Position(i, j, Fields.PLAYER_TWO)),
                                    board.getFieldPlayerTwo()[i][j] == board.getGeneralPlayerTwo());
                    //draw status of all units
                    TerminalStatusDrawer.invokeAllStatusDrawers(this,
                            utg.getPair(new Position(i, j, Fields.PLAYER_TWO)), board.getFieldPlayerTwo()[i][j]);
                }

                if (answer != null && board.getArmy(Fields.PLAYER_ONE) != null && board.getArmy(Fields.PLAYER_TWO) != null) {
                    TerminalAnswerDrawer.drawAnswer(this, answer);
                    TerminalInformationDrawer.drawInfo(this, board);
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
}
