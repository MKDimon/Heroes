package heroes.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.unitdrawers.HealerDrawer;
import heroes.gui.unitdrawers.MageDrawer;
import heroes.gui.utils.UnitDrawersMap;
import heroes.gui.utils.UnitTerminalGrid;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.units.General;
import heroes.units.GeneralTypes;

import java.awt.*;
import java.io.IOException;

public class TerminalWrapper {

    final private Screen screen;
    final private Terminal terminal;

    public TerminalWrapper() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(150, 50));
        defaultTerminalFactory.setTerminalEmulatorTitle("Heroes");
        terminal = defaultTerminalFactory.createTerminal();
        screen = new TerminalScreen(terminal);
    }

    private void clean() {
        screen.clear();
    }

    private void refresh() throws IOException {
        screen.refresh();
    }

    public void start() throws IOException {
        screen.startScreen();
    }

    public void update(final Answer answer, final Board board) throws IOException {
        clean();

        TerminalBorderDrawer.drawBorders(this);
        General gen_one = board.getGeneralPlayerOne();
        General gen_two = board.getGeneralPlayerTwo();
        TerminalGeneralDrawer.drawGenerals(this, gen_one.getActionType(), gen_two.getActionType());
        UnitTerminalGrid utg = new UnitTerminalGrid(this.getTerminal().getTerminalSize().getColumns() / 2 - 35 + 2,
                                                    this.getTerminal().getTerminalSize().getColumns() / 2 + 35 - 1);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                UnitDrawersMap.getDrawer(board.getFieldPlayerOne()[i][j].getActionType())
                        .draw(this, utg.getPair(new Position(i, j, Fields.PLAYER_ONE)),
                                board.getFieldPlayerOne()[i][j] == board.getGeneralPlayerOne());

                UnitDrawersMap.getDrawer(board.getFieldPlayerTwo()[i][j].getActionType())
                        .draw(this, utg.getPair(new Position(i, j, Fields.PLAYER_TWO)),
                                board.getFieldPlayerTwo()[i][j] == board.getGeneralPlayerTwo());
            }
        }

        refresh();
    }

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
