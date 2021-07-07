package heroes.boardfactory;

import heroes.units.Unit;

public abstract class Command {
    private final Unit att;
    private final Unit def;

    public Unit getAtt() {
        return att;
    }

    public Unit getDef() {
        return def;
    }

    public Command(Unit att, Unit def) {
        this.att = att;
        this.def = def;
    }

    public abstract void execute();
}
