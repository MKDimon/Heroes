package heroes.clientserver.commands;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.serverexcetions.ServerExceptionType;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class GetAnswerCommand extends Command {
    private final Logger logger = LoggerFactory.getLogger(GetAnswerCommand.class);

    public GetAnswerCommand(final Data data, final BufferedWriter out, final Client client) {
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
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
        }
    }
}
