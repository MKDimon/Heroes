package heroes.clientserver.serverexcetions;

public class ServerException extends Exception {

    public ServerException(final ServerExceptionType errorType) {
        super(errorType.getErrorType());
    }

}
