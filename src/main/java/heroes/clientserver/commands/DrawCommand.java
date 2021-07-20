package heroes.clientserver.commands;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;

import java.io.BufferedWriter;
import java.io.IOException;

public class DrawCommand extends Command{
    public DrawCommand(Data data, BufferedWriter out, Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            getClient().getTw().update(getData().answer, getData().board);
            getOut().write(Serializer.serializeData(new Data(CommonCommands.DRAW_SUCCESSFUL)) + '\n');
        } catch (IOException | UnitException e) {
            try {
                getOut().write(Serializer.serializeData(new Data(CommonCommands.DRAW_UNSUCCESSFUL)) + '\n');
            } catch (IOException ioException) {

            }
        }
        finally {
            try {
                getOut().flush();
            } catch (IOException e) {

            }
        }
    }
}
