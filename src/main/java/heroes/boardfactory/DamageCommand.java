package heroes.boardfactory;

import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


public class DamageCommand extends Command {
    Logger logger = LoggerFactory.getLogger(DamageCommand.class);

    public DamageCommand(Unit att, List<Unit> def) {
        super(att, def);

    }

    boolean getHitChance(int accuracy) {
        int min = 0;
        int max = 1;
        Random r = new Random();
        double rand = min + (max - min) * r.nextDouble();
        double acc = accuracy / 100.0;
        logger.info("Unit accuracy {}", acc);
        logger.info("Randomly generated number {}", rand);
        //TODO: ternary operator
        return rand < acc;
    }

    int reducedDamage(int pow, int arm) {
        double double_arm = (double) (arm) / 100.0;
        return pow - (int) ((double) pow * double_arm);
    }

    @Override
    public void execute() {

        if (getHitChance(super.getAtt().getAccuracy())) {
            for (Unit elem : super.getDef()) {
                elem.setCurrentHP(elem.getCurrentHP() -
                        reducedDamage(super.getAtt().getPower(), elem.getArmor()));
                logger.info("Unit hit! Dealing damage.");
                logger.info("Attacker power: {}. Reduced by armor damage = {}", super.getAtt().getPower(),
                        reducedDamage(super.getAtt().getPower(), elem.getArmor()));
                logger.info("Defender current hp: {}.", elem.getCurrentHP());
                logger.info("Defender max hp: {}.", elem.getMaxHP());
            }

        } else {
            logger.info("Unit missed");
        }
    }
}
