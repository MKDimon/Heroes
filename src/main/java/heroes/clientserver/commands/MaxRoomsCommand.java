package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;

public class MaxRoomsCommand extends Command {
    Logger logger = LoggerFactory.getLogger(FieldOneCommand.class);

    public MaxRoomsCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        super.getClient().downService();
    }
}
