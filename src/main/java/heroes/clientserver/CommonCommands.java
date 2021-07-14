package heroes.clientserver;

import heroes.gamelogic.Fields;

public enum CommonCommands {
    END_GAME("END_GAME"),
    GET_ARMY("GET_ARMY"),
    MAX_ROOMS("MAX_ROOMS"),
    FIELD_ONE(Fields.PLAYER_ONE.name()),
    FIELD_TWO(Fields.PLAYER_TWO.name()),
    ;
    public final String command;

    CommonCommands(String command) {
        this.command = command;
    }
}
