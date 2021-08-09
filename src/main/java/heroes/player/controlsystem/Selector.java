package heroes.player.controlsystem;

import com.googlecode.lanterna.input.KeyType;
import heroes.mathutils.Pair;

import java.util.HashMap;
import java.util.Map;

public class Selector {
    private final int[][] selectionMatrix;
    final int rows;
    final int cols;
    private Pair<Integer, Integer> currentSelection = new Pair<>(0,0);

    private final Map<KeyType, Pair<Integer, Integer>> keyTypePairMap;

    public Selector(final int rows, final int cols) {
        this.rows = rows;
        this.cols = cols;
        selectionMatrix = new int[rows][cols];

        //filling matrix with numbers [1, number of elements] to have unique number of every cell
        int counter = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                selectionMatrix[i][j] = counter;
                counter++;
            }
        }

        keyTypePairMap = new HashMap<>();
        keyTypePairMap.put(KeyType.ArrowUp, new Pair<>(-1, 0));
        keyTypePairMap.put(KeyType.ArrowDown, new Pair<>(1, 0));
        keyTypePairMap.put(KeyType.ArrowLeft, new Pair<>(0, -1));
        keyTypePairMap.put(KeyType.ArrowRight, new Pair<>(0, 1));
        keyTypePairMap.put(null, new Pair<>(0, 0));
    }

    public void updateSelection(final KeyType kt) {
        Pair<Integer, Integer> fromMap = keyTypePairMap.getOrDefault(kt, new Pair<>(0, 0));

        int x = (fromMap.getX() + currentSelection.getX() + rows) % rows; // java % - ост. от деления, нужно добавить
        int y = (fromMap.getY() + currentSelection.getY() + cols) % cols; // чтобы не уйти в отрицательные числа

        currentSelection = new Pair<>(x, y);
    }

    public int getSelectedNumber() {
        return selectionMatrix[currentSelection.getX()][currentSelection.getY()];
    }

    public int getSelectedNumber(final int x, final int y) {
        return  selectionMatrix[x][y];
    }

    public Pair<Integer, Integer> getCurrentSelection() {
        return currentSelection;
    }
}
