package heroes.clientserver.commands;

import heroes.auxiliaryclasses.serverexcetions.ServerExceptionType;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetArmyCommand extends Command{
    Logger logger = LoggerFactory.getLogger(GetArmyCommand.class);

    public GetArmyCommand(final Data data, final BufferedWriter out, final Client client) {
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
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }
}
