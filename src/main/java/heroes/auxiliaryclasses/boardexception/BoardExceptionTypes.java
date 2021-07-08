package heroes.auxiliaryclasses.boardexception;

public enum BoardExceptionTypes {
    INDEX_OUT_OF_BOUNDS("Index out of bounds"),
    ACTION_INCORRECT("Action is incorrect"),
    INCORRECT_TARGET("Incorrect target"),
    NULL_UNIT_IN_ARMY("Unit in army is null"),
    NULL_POINTER("Null pointer"),
    INCORRECT_PLAYER("Incorrect player"),
    UNIT_IS_DEAD("Unit is dead"),
    UNIT_IS_NOT_ACTIVE("Unit is not active")
    ;

    private final String errorType;

    BoardExceptionTypes(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
