package client.commands;

import client.Client;
import heroes.clientserver.Data;
import heroes.clientserver.serverexcetions.ServerExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class GetRoomCommand extends Command{
    private final Logger logger = LoggerFactory.getLogger(GetRoomCommand.class);

    public GetRoomCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            int id;
            Scanner scanner = new Scanner(System.in);
            do {
                id = getClient().getController().getRoomCommand();
            } while (id > getData().info || id < 1);
            getOut().write( String.valueOf(id)+ '\n');
            getOut().flush();
        } catch (final IOException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }
}
