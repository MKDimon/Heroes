package heroes.auxiliaryclasses;

public enum GameLogicExceptionType {
    INCORRECT_PARAMS("Incorrect params for GL");

    private final String errorType;

    GameLogicExceptionType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
