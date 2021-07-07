package heroes.boardfactory;

import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DamageCommand extends Command {
    Logger logger = LoggerFactory.getLogger(DamageCommand.class);
    public DamageCommand(Unit att, Unit def) {
        super(att, def);

    }
    @Override
    public void execute() {
        logger.info("Dealing damage");
        System.out.println("Damaging...");
    }
}
