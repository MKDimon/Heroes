package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetRoomCommand extends Command{
    public GetRoomCommand(Data data, BufferedWriter out, Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        //TODO: логику
        try {

            getOut().write( "1"+ '\n');
            getOut().flush();
        } catch (IOException e) {

        }
    }
}
