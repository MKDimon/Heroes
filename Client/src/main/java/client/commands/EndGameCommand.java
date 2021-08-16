package client.commands;

import client.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;

public class EndGameCommand extends Command{
    public EndGameCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        getClient().getGUI().endGame(getData());
        getClient().downService();
    }
}
