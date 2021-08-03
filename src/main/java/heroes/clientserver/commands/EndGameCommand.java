package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.gui.TerminalEndGame;

import java.io.BufferedWriter;

public class EndGameCommand extends Command{
    public EndGameCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        TerminalEndGame.endGame(getClient().getTw(), getData());
        getClient().downService();
    }
}
