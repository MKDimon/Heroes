package heroes.auxiliaryclasses.serverexcetions;

public class ServerException extends Exception {

    public ServerException(ServerExceptionType errorType) {
        super(errorType.getErrorType());
    }

}
