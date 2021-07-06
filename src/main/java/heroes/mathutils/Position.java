package heroes.mathutils;

import heroes.Fields;

public class Position {
    private int x;
    private int y;
    private Fields f;

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
