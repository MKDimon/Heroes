package heroes.boardfactory;

import heroes.units.Unit;

public class HealCommand extends Command {
    public HealCommand(Unit att, Unit def) {
        super(att, def);

    }

    @Override
    public void execute() {
        System.out.println("Healing...");
    }
}
