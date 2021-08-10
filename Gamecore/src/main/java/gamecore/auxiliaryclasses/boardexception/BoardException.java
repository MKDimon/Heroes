package gamecore.auxiliaryclasses.boardexception;

public class BoardException extends Exception {

    public BoardException(final BoardExceptionTypes error) {
        super(error.getErrorType());
    }

}
