package heroes.clientserver.commands;

import heroes.auxiliaryclasses.serverexcetions.ServerExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Client;
import heroes.clientserver.Data;
import heroes.clientserver.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class DrawCommand extends Command{
    Logger logger = LoggerFactory.getLogger(DrawCommand.class);

    public DrawCommand(final Data data, final BufferedWriter out, final Client client) {
        super(data, out, client);
    }

    @Override
    public void execute() {
        try {
            getClient().getTw().update(getData().answer, getData().board);
            getOut().write(Serializer.serializeData(new Data(CommonCommands.DRAW_SUCCESSFUL)) + '\n');
        } catch (IOException | UnitException e) {
            logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
            try {
                getOut().write(Serializer.serializeData(new Data(CommonCommands.DRAW_UNSUCCESSFUL)) + '\n');
            } catch (IOException ioException) {
                logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), ioException);
            }
        }
        finally {
            try {
                getOut().flush();
            } catch (IOException e) {
                logger.error(ServerExceptionType.ERROR_COMMAND_RUNNING.getErrorType(), e);
            }
        }
    }
}
