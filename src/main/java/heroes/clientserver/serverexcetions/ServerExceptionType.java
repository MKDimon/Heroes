package heroes.clientserver.serverexcetions;

public enum ServerExceptionType {
    ERROR_SERVER_ROOM_CHANGED("Error server room changed"),
    ERROR_SEND_ENDGAME("Error send endgame"),
    ERROR_SERVER_RUNNING("Error server running"),
    ERROR_ROOM_RUNNING("Error room running"),
    ERROR_DRAWING("Error drawing"),
    ERROR_COMMAND_RUNNING("Error command running"),
    ;


    private final String errorType;

    public String getErrorType() {
        return errorType;
    }

    ServerExceptionType(String errorType) {
        this.errorType = errorType;
    }
}
