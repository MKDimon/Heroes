package heroes.clientserver.serverexcetions;

public class ServerException extends Exception {

    public ServerException(ServerExceptionType errorType) {
        super(errorType.getErrorType());
    }

}
