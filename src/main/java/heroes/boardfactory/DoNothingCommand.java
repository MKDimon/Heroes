package heroes.boardfactory;

import heroes.units.Unit;

import java.util.List;

public class DoNothingCommand extends Command {
    public DoNothingCommand(Unit att, List<Unit> def) {
        super(att, def);

    }

    @Override
    public void execute() {

    }
}
