package heroes.clientserver.commands;

import gamecore.gamelogic.Fields;

public enum CommonCommands {
    END_GAME("END_GAME"),
    CONTINUE_GAME("CONTINUE_GAME"),
    GET_ARMY("GET_ARMY"),
    GET_ANSWER("GET_ANSWER"),
    MAX_ROOMS("MAX_ROOMS"),
    GET_ROOM("GET_ROOM"),
    GET_FIELD("GET_FIELD"),
    DRAW("DRAW"),
    DRAW_SUCCESSFUL("DRAW_SUCCESSFUL"),
    DRAW_UNSUCCESSFUL("DRAW_UNSUCCESSFUL"),
    FIELD_ONE(Fields.PLAYER_ONE.name()),
    FIELD_TWO(Fields.PLAYER_TWO.name()),
    ;
    public final String command;

    CommonCommands(final String command) {
        this.command = command;
    }
}
