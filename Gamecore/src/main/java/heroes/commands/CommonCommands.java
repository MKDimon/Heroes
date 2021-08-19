package heroes.commands;

public enum CommonCommands {
    END_GAME("END_GAME"),
    CONTINUE_GAME("CONTINUE_GAME"),
    GET_ARMY("GET_ARMY"),
    GET_ANSWER("GET_ANSWER"),
    MAX_ROOMS("MAX_ROOMS"),
    GET_ROOM("GET_ROOM"),
    GET_FIELD("GET_FIELD"),
    GET_OPPONENT("GET_OPPONENT"),
    DRAW("DRAW"),
    DRAW_SUCCESSFUL("DRAW_SUCCESSFUL"),
    DRAW_UNSUCCESSFUL("DRAW_UNSUCCESSFUL"),
    FIELD_ONE("PLAYER_ONE"),
    FIELD_TWO("PLAYER_TWO"),
    GET_BOT_NAME("GET_BOT_NAME"),
    ;
    public final String command;

    CommonCommands(final String command) {
        this.command = command;
    }
}
