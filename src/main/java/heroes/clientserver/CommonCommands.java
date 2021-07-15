package heroes.clientserver;

import heroes.gamelogic.Fields;

public enum CommonCommands {
    END_GAME("END_GAME"),
    GET_ARMY("GET_ARMY"),
    GET_ANSWER("GET_ANSWER"),
    SEE_ARMY("SEE_ARMY"),
    MAX_ROOMS("MAX_ROOMS"),
    GET_ROOM("GET_ROOM"),
    DRAW("DRAW"),
    DRAW_SUCCESSFUL("DRAW_SUCCESSFUL"),
    DRAW_UNSUCCESSFUL("DRAW_UNSUCCESSFUL"),
    FIELD_ONE(Fields.PLAYER_ONE.name()),
    FIELD_TWO(Fields.PLAYER_TWO.name()),
    ;
    public final String command;

    CommonCommands(String command) {
        this.command = command;
    }
}
