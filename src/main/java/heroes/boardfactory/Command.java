package heroes.boardfactory;

import heroes.units.Unit;
import heroes.units.auxiliaryclasses.ActionTypes;

public abstract class Command {
    private Unit att;
    private Unit def;

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
