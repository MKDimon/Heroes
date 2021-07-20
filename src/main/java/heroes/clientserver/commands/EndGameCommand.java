package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;

public class EndGameCommand extends Command{
    public EndGameCommand(Data data, BufferedWriter out, Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {

    }
}
