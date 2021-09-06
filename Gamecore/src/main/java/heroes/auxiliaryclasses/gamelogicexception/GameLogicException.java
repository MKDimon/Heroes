package heroes.auxiliaryclasses.gamelogicexception;

public class GameLogicException extends Exception {

    public GameLogicException(final GameLogicExceptionType error) {
        super(error.getErrorType());
    }
}
