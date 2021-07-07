package heroes.boardfactory;

import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class DamageCommand extends Command {
    Logger logger = LoggerFactory.getLogger(DamageCommand.class);

    public DamageCommand(Unit att, Unit def) {
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
        double double_arm = (double)(arm) / 100.0;
        return pow - (int)((double)pow * double_arm);
    }

    @Override
    public void execute() {

        if (getHitChance(super.getAtt().getAccuracy())) {
            super.getDef().setCurrentHP(super.getDef().getCurrentHP() -
                        reducedDamage(super.getAtt().getPower(), super.getDef().getArmor()));
            logger.info("Unit hit! Dealing damage.");
            logger.info("Attacker power: {}. Reduced by armor damage = {}", super.getAtt().getPower(),
                    reducedDamage(super.getAtt().getPower(), super.getDef().getArmor()));
            logger.info("Defender current hp: {}.", super.getDef().getCurrentHP());
            logger.info("Defender max hp: {}.", super.getDef().getMaxHP());
        } else {
            logger.info("Unit missed");
        }
    }
}
