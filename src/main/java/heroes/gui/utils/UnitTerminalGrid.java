package heroes.gui.utils;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Fields;
import heroes.gui.generaldrawers.CommanderDrawer;
import heroes.gui.generaldrawers.IGeneralDrawer;
import heroes.gui.generaldrawers.PriestDrawer;
import heroes.gui.generaldrawers.SniperDrawer;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;

import java.util.HashMap;
import java.util.Map;

public class UnitTerminalGrid {
    private final Map<Position, Pair<Integer, Integer>> unitGrid;
    final int x_start_1;
    final int x_start_2;

    public UnitTerminalGrid(int x_start_1, int x_start_2) {
        this.x_start_1 = x_start_1;
        this.x_start_2 = x_start_2;

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
