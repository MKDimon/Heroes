package heroes.mathutils;

public class Position {
    private int x;
    private int y;
    private int f;

    public Position(int x, int y, int f) {
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

    public int F() {
        return f;
    }
}
