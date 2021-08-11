package heroes.gui.heroeslanterna.menudrawers.unitmenudrawers;

import gamecore.gamelogic.Fields;
import heroes.gui.heroeslanterna.LanternaWrapper;
import gamecore.mathutils.Pair;
import gamecore.mathutils.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс содержит Map, который создает связь между расположением юнита в массиве юнитов и координатами для его
 * отрисовки в терминале.
 */
public class UnitMenuTerminalGrid {
    Logger logger = LoggerFactory.getLogger(UnitMenuTerminalGrid.class);
    private final Map<Position, Pair<Integer, Integer>> unitGrid;
    private int x_start_1;
    private int x_start_2;

    private void setConstants(final LanternaWrapper tw) {
        try {
            this.x_start_1 = tw.getTerminal().getTerminalSize().getColumns() / 2 - 70 + 2;
            this.x_start_2 = 110;
        } catch (IOException e) {
            logger.error("Cannot get terminal size in class UnitTerminalGrid", e);
        }
    }

    public UnitMenuTerminalGrid(final LanternaWrapper tw) {
        setConstants(tw);

        unitGrid = new HashMap<>();

        unitGrid.put(new Position(1, 0, Fields.PLAYER_ONE), new Pair<>(x_start_1, 14));
        unitGrid.put(new Position(1, 1, Fields.PLAYER_ONE), new Pair<>(x_start_1, 25));
        unitGrid.put(new Position(1, 2, Fields.PLAYER_ONE), new Pair<>(x_start_1, 36));
        unitGrid.put(new Position(0, 0, Fields.PLAYER_ONE), new Pair<>(x_start_1 + 13, 14));
        unitGrid.put(new Position(0, 1, Fields.PLAYER_ONE), new Pair<>(x_start_1 + 13, 25));
        unitGrid.put(new Position(0, 2, Fields.PLAYER_ONE), new Pair<>(x_start_1 + 13, 36));

        unitGrid.put(new Position(0, 0, Fields.PLAYER_TWO), new Pair<>(x_start_2 + 4, 14));
        unitGrid.put(new Position(0, 1, Fields.PLAYER_TWO), new Pair<>(x_start_2 + 4, 25));
        unitGrid.put(new Position(0, 2, Fields.PLAYER_TWO), new Pair<>(x_start_2 + 4, 36));
        unitGrid.put(new Position(1, 0, Fields.PLAYER_TWO), new Pair<>(x_start_2 + 17, 14));
        unitGrid.put(new Position(1, 1, Fields.PLAYER_TWO), new Pair<>(x_start_2 + 17, 25));
        unitGrid.put(new Position(1, 2, Fields.PLAYER_TWO), new Pair<>(x_start_2 + 17, 36));
    }

    public Pair<Integer, Integer> getPair(final Position position) {
        return unitGrid.get(position);
    }
}
