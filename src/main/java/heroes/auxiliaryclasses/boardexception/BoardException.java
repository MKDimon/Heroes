package heroes.auxiliaryclasses.boardexception;

public class BoardException extends Exception{

    public BoardException(BoardExceptionTypes error) { super(error.getErrorType()); }

}
