package heroes.boardfactory;

import heroes.statistics.StatisticsCollector;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HealCommand extends Command {
    Logger logger = LoggerFactory.getLogger(HealCommand.class);

    public HealCommand(final Unit att, final List<Unit> def) {
        super(att, def);

    }

    @Override
    public void execute() {
        for (Unit elem : super.getDef()) {
            int healPower = super.getAtt().getPower();
            int reducedHealPower = elem.getCurrentHP();
            logger.info("Defender current hp: {}.", elem.getCurrentHP());
            elem.setCurrentHP(elem.getCurrentHP() + healPower);
            logger.info("Unit healed!");
            logger.info("Defender current hp: {}.", elem.getCurrentHP());
            logger.info("Attacker heal power: {}.", healPower);
            reducedHealPower = -1 * (reducedHealPower - elem.getCurrentHP());
            StatisticsCollector.recordActionToCSV(super.getAtt(),
                    elem, Math.min(reducedHealPower, healPower), StatisticsCollector.actionStatisticsFilename);
        }
        StatisticsCollector.recordMessageToCSV("\n", StatisticsCollector.actionStatisticsFilename);
    }
}
