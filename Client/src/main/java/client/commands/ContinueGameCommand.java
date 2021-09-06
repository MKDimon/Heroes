package client.commands;

import client.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;

public class ContinueGameCommand extends Command{
    public ContinueGameCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        getClient().getGUI().continueGame(getData());
    }
}
