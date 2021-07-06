package heroes.mathutils;

import heroes.gamelogic.Fields;

public class Position {
    private final int x;
    private final int y;
    private final Fields f;

    public Position(int x, int y, Fields f) {
        this.x = x;
        this.y = y;
        this.f = f;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public Fields F() {
        return f;
    }
}
