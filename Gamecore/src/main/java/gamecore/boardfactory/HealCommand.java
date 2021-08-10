package gamecore.boardfactory;

import gamecore.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HealCommand extends Command {
    private static final Logger logger = LoggerFactory.getLogger(HealCommand.class);

    public HealCommand(final Unit att, final List<Unit> def) {
        super(att, def);

    }

    @Override
    public void execute() {
        for (Unit elem : super.getDef()) {
            logger.info("Defender current hp: {}.", elem.getCurrentHP());
            elem.setCurrentHP(elem.getCurrentHP() + super.getAtt().getPower());
            logger.info("Unit healed!");
            logger.info("Defender current hp: {}.", elem.getCurrentHP());
            logger.info("Attacker heal power: {}.", super.getAtt().getPower());
        }
    }
}
