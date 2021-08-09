package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.gui.TerminalContinueGame;
import heroes.gui.TerminalEndGame;

import java.io.BufferedWriter;

public class ContinueGameCommand extends Command{
    public ContinueGameCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        TerminalContinueGame.continueGame(getClient().getTw(), getData());
    }
}
