package heroes.boardfactory;

import heroes.units.Unit;

public class DamageCommand extends Command {
    public DamageCommand(Unit att, Unit def) {
        super(att, def);

    }

    @Override
    public void execute() {
        System.out.println("Damaging...");
    }
}
