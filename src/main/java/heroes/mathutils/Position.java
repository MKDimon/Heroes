package heroes.mathutils;

import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.gamelogic.Fields;

import java.util.Objects;

public class Position {
    @JsonProperty
    private final int x;
    @JsonProperty
    private final int y;
    @JsonProperty
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

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", field=" + f +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && f == position.f;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, f);
    }
}
