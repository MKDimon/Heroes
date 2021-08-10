package heroes.player.botnikita;

import gamecore.units.Unit;
import gamecore.mathutils.Position;

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
