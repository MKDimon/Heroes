package heroes.clientserver.commands;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetAnswerCommand extends Command {

    public GetAnswerCommand(Data data, BufferedWriter out, Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            getOut().write(Serializer.serializeData(
                    new Data(getClient().getPlayer().getAnswer(getData().board))
            ) + '\n');
            getOut().flush();
        } catch (IOException | GameLogicException e) {

        }
    }
}
