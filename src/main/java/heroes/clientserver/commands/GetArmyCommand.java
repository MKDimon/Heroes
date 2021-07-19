package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetArmyCommand extends Command{
    public GetArmyCommand(Data data, BufferedWriter out, Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            super.getOut().write(Serializer.serializeData(
                    new Data(null, getClient().getPlayer().getArmy())
            ) + '\n');
            super.getOut().flush();
        } catch (IOException e) {

        }
    }
}
