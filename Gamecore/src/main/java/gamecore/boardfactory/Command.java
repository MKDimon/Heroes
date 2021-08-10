package gamecore.boardfactory;

import gamecore.units.Unit;

import java.util.List;

public abstract class Command {
    private final Unit att;
    private final List<Unit> def;

    public Unit getAtt() {
        return att;
    }

    public List<Unit> getDef() {
        return def;
    }

    public Command(final Unit att, final List<Unit> def) {
        this.att = att;
        this.def = def;
    }

    public abstract void execute();
}
