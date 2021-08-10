package gamecore.auxiliaryclasses.gamelogicexception;

public enum GameLogicExceptionType {
    INCORRECT_PARAMS("Incorrect params for GL"),
    NULL_POINTER("Null pointer");

    private final String errorType;

    GameLogicExceptionType(final String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
