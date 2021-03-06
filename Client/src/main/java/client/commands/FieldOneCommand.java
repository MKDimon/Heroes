package client.commands;

import client.Client;
import heroes.clientserver.Data;
import heroes.gamelogic.Fields;

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
