package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.gamelogic.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;

public class FieldTwoCommand extends Command{
    Logger logger = LoggerFactory.getLogger(FieldOneCommand.class);

    public FieldTwoCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        super.getClient().chooseBot(Fields.PLAYER_TWO);
    }
}
