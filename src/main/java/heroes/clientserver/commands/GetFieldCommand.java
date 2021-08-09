package heroes.clientserver.commands;

import heroes.auxiliaryclasses.serverexcetions.ServerExceptionType;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class GetFieldCommand extends Command {
    private final Logger logger = LoggerFactory.getLogger(GetFieldCommand.class);

    public GetFieldCommand(Data data, BufferedWriter out, Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            int id;
            Scanner scanner = new Scanner(System.in);
            do {
                id = getClient().getTw().updateMenu();
            } while (id > 2 || id < 1);
            getOut().write( Serializer.serializeData(new Data(id)) + '\n');
            getOut().flush();
        } catch (final IOException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }
}
