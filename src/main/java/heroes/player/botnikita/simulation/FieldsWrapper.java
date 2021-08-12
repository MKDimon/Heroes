package heroes.player.botnikita.simulation;

import heroes.gamelogic.Fields;

public class FieldsWrapper {
    public static Fields getOppField(final Fields field) {
        return field == Fields.PLAYER_ONE ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;
    }
}
