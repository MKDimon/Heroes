package heroes.auxiliaryclasses.boardexception;

public enum BoardExceptionTypes {
    INDEX_OUT_OF_BOUNDS("Index out of bounds"),
    ACTION_INCORRECT("Action is incorrect"),
    INCORRECT_TARGET("Incorrect target"),
    NULL_UNIT_IN_ARMY("Unit in army is null"),
    NULL_POINTER("Null pointer");

    private final String errorType;

    BoardExceptionTypes(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
