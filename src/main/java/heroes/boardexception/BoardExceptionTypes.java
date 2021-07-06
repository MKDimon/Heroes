package heroes.boardexception;

public enum BoardExceptionTypes {
    INDEX_OUT_OF_BOUNDS("Index out of bounds"),
    ACTION_INCORRECT("Action is incorrect"),
    INCORRECT_TARGET("Incorrect target");

    private String errorType;

    BoardExceptionTypes(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
