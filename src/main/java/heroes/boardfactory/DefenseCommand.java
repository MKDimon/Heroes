package heroes.boardfactory;

import heroes.units.Unit;

public class DefenseCommand extends Command {
    public DefenseCommand(Unit att, Unit def) {
        super(att, def);

    }

    @Override
    public void execute() {
        System.out.println("Defending...");
    }
}
