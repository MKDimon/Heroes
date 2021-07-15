package heroes.boardfactory;

import heroes.auxiliaryclasses.statistics.StatisticsCollector;
import heroes.units.Unit;

import java.util.List;

public class DefenseCommand extends Command {
    public DefenseCommand(final Unit att, final List<Unit> def) {
        super(att, def);

    }

    @Override
    public void execute() {
        super.getAtt().defense();
        StatisticsCollector.recordActionToCSV(super.getAtt(),
                getAtt(), super.getAtt().getArmor(), StatisticsCollector.actionStatisticsFilename);
    }
}
