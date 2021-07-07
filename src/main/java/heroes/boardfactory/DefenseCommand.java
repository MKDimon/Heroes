package heroes.boardfactory;

import heroes.units.Unit;

public class DefenseCommand extends Command {
    Unit att;
    Unit def;
    public DefenseCommand(Unit att, Unit def) {
        super(att, def);

    }

    @Override
    public void execute() {
        att.defense();
    }
}
