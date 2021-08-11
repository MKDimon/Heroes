package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.gui.heroeslanterna.LanternaContinueGame;

import java.io.BufferedWriter;

public class ContinueGameCommand extends Command{
    public ContinueGameCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        LanternaContinueGame.continueGame(getClient().getTw(), getData());
    }
}