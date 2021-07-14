package heroes.clientserver;

import heroes.gamelogic.Fields;

public enum CommonCommands {
    END_GAME("END_GAME"),
    GET_ARMY("GET_ARMY"),
    MAX_ROOMS("MAX_ROOMS"),
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
