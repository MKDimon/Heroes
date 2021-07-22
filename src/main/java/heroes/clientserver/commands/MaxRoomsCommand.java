package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;

public class MaxRoomsCommand extends Command {

    public MaxRoomsCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        super.getClient().downService();
    }
}
