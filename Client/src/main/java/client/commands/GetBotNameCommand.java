package client.commands;

import client.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;
import heroes.clientserver.serverexcetions.ServerExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetBotNameCommand extends Command {
    private static final Logger logger = LoggerFactory.getLogger(GetAnswerCommand.class);

    public GetBotNameCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            getOut().write(Serializer.serializeData(new Data(getClient().getBotType())) + '\n');
            getOut().flush();
        } catch (final IOException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }

}
