package heroes.gui.heroeslanterna.utils;

import heroes.gamelogic.Fields;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс содержит Map, который создает связь между расположением юнита в массиве юнитов и координатами для его
 * отрисовки в терминале.
 */
public class UnitTerminalGrid {
    Logger logger = LoggerFactory.getLogger(UnitTerminalGrid.class);
    private final Map<Position, Pair<Integer, Integer>> unitGrid;
    private int x_start_1;
    private int x_start_2;

    private void setConstants(final LanternaWrapper tw) {
        try {
            this.x_start_1 = tw.getTerminal().getTerminalSize().getColumns() / 2 - 35 + 2;
            this.x_start_2 = tw.getTerminal().getTerminalSize().getColumns() / 2 + 35 - 1;
        } catch (IOException e) {
            logger.error("Cannot get terminal size in class UnitTerminalGrid", e);
        }
    }

    public UnitTerminalGrid(final LanternaWrapper tw) {
        setConstants(tw);

        unitGrid = new HashMap<>();

        unitGrid.put(new Position(1, 0, Fields.PLAYER_ONE), new Pair<>(x_start_1, 5));
        unitGrid.put(new Position(1, 1, Fields.PLAYER_ONE), new Pair<>(x_start_1, 15));
        unitGrid.put(new Position(1, 2, Fields.PLAYER_ONE), new Pair<>(x_start_1, 25));
        unitGrid.put(new Position(0, 0, Fields.PLAYER_ONE), new Pair<>(x_start_1 + 13, 5));
        unitGrid.put(new Position(0, 1, Fields.PLAYER_ONE), new Pair<>(x_start_1 + 13, 15));
        unitGrid.put(new Position(0, 2, Fields.PLAYER_ONE), new Pair<>(x_start_1 + 13, 25));

        unitGrid.put(new Position(0, 0, Fields.PLAYER_TWO), new Pair<>(x_start_2 - 21, 5));
        unitGrid.put(new Position(0, 1, Fields.PLAYER_TWO), new Pair<>(x_start_2 - 21, 15));
        unitGrid.put(new Position(0, 2, Fields.PLAYER_TWO), new Pair<>(x_start_2 - 21, 25));
        unitGrid.put(new Position(1, 0, Fields.PLAYER_TWO), new Pair<>(x_start_2 - 8, 5));
        unitGrid.put(new Position(1, 1, Fields.PLAYER_TWO), new Pair<>(x_start_2 - 8, 15));
        unitGrid.put(new Position(1, 2, Fields.PLAYER_TWO), new Pair<>(x_start_2 - 8, 25));
    }

    public Pair<Integer, Integer> getPair(final Position position) {
        return unitGrid.get(position);
    }
}
