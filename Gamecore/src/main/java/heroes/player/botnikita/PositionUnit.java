package heroes.player.botnikita;

import heroes.mathutils.Position;
import heroes.units.Unit;

public class PositionUnit {
    final private Position position;
    final private Unit unit;

    public PositionUnit(Position position, Unit unit) {
        this.position = position;
        this.unit = unit;
    }

    public Position getPosition() {
        return position;
    }

    public Unit getUnit() {
        return unit;
    }
}
