package heroes.boardfactory;

import heroes.units.Unit;
import heroes.units.auxiliaryclasses.ActionTypes;

public class HealCommand extends Command {
    public HealCommand(Unit att, Unit def) {
        super(att, def);

    }
    @Override
    public void execute() {
        System.out.println("Healing...");
    }
}
