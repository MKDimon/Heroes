package heroes.auxiliaryclasses;

public class GameLogicException extends Exception {

    public GameLogicException(GameLogicExceptionType error) {
        super(error.getErrorType());
    }
}
