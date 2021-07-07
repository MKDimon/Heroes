package heroes.boardfactory;

import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealCommand extends Command {
    Logger logger = LoggerFactory.getLogger(HealCommand.class);
    public HealCommand(Unit att, Unit def) {
        super(att, def);

    }

    @Override
    public void execute() {
        logger.info("Defender current hp: {}.", super.getDef().getCurrentHP());
        super.getDef().setCurrentHP(super.getDef().getCurrentHP() + super.getAtt().getPower());
        logger.info("Unit healed!");
        logger.info("Defender current hp: {}.", super.getDef().getCurrentHP());
        logger.info("Attacker heal power: {}.", super.getAtt().getPower());
    }
}
