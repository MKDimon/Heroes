package heroes.boardfactory;

import heroes.statistics.StatisticsCollector;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


public class DamageCommand extends Command {
    Logger logger = LoggerFactory.getLogger(DamageCommand.class);

    public DamageCommand(final Unit att, final List<Unit> def) {
        super(att, def);

    }

    boolean getHitChance(final int accuracy) {
        Random r = new Random();
        final double rand = r.nextDouble();
        final double acc = accuracy / 100.0;
        logger.info("Unit accuracy {}", acc);
        logger.info("Randomly generated number {}", rand);
        //TODO: ternary operator
        return rand < acc;
    }

    int reducedDamage(final int pow, final int arm) {
        final double double_arm = (double) (arm) / 100.0;
        return pow - (int) ((double) pow * double_arm);
    }

    @Override
    public void execute() {

        if (getHitChance(super.getAtt().getAccuracy())) {
            for (Unit elem : super.getDef()) {
                    int reducedDamage = reducedDamage(super.getAtt().getPower(), elem.getArmor());
                    elem.setCurrentHP(elem.getCurrentHP() -
                            reducedDamage);
                    //Статистика собирается ПОСЛЕ нанесения урона
                    StatisticsCollector.recordActionToCSV(super.getAtt(),
                            elem, reducedDamage, StatisticsCollector.actionStatisticsFilename);
                    logger.info("Unit hit! Dealing damage.");
                    logger.info("Attacker power: {}. Reduced by armor damage = {}", super.getAtt().getPower(),
                            reducedDamage(super.getAtt().getPower(), elem.getArmor()));
                    logger.info("Defender current hp: {}.", elem.getCurrentHP());
                    logger.info("Defender max hp: {}.", elem.getMaxHP());
            }
            StatisticsCollector.recordMessageToCSV("\n", StatisticsCollector.actionStatisticsFilename);

        } else {
            logger.info("Unit missed");
            StatisticsCollector.recordActionToCSV(super.getAtt(),
                    getDef().get(0), 0, StatisticsCollector.actionStatisticsFilename);
            StatisticsCollector.recordMessageToCSV("\n", StatisticsCollector.actionStatisticsFilename);

        }
    }
}
