package heroes.clientserver.commands;

import gamecore.gamelogic.Fields;
import heroes.clientserver.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;

public class FieldOneCommand extends Command{

    public FieldOneCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        super.getClient().chooseBot(Fields.PLAYER_ONE);
    }
}
