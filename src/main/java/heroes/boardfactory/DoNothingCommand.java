package heroes.boardfactory;

import heroes.units.Unit;

public class DoNothingCommand extends Command {
    public DoNothingCommand(Unit att, Unit def) {
        super(att, def);

    }

    @Override
    public void execute() {

    }
}
