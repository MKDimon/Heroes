package heroes.clientserver.commands;

import heroes.auxiliaryclasses.serverexcetions.ServerExceptionType;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class GetRoomCommand extends Command{
    Logger logger = LoggerFactory.getLogger(GetRoomCommand.class);

    public GetRoomCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            int id, max = Deserializer.getConfig().MAX_ROOMS;
            Scanner scanner = new Scanner(System.in);
            do {
                System.out.println("Change room id (max: " + max + "):");
                id = scanner.nextInt();
            } while (id > max || id < 1);
            getOut().write( String.valueOf(id)+ '\n');
            getOut().flush();
        } catch (IOException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }
}
