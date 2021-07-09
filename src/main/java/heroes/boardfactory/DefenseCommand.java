package heroes.boardfactory;

import heroes.units.Unit;

import java.util.List;

public class DefenseCommand extends Command {
    public DefenseCommand(Unit att, List<Unit> def) {
        super(att, def);

    }

    @Override
    public void execute() {
        super.getAtt().defense();

    }
}
