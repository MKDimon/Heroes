package heroes.gamelogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.General;
import heroes.units.Unit;

import java.util.Arrays;
import java.util.Objects;

public class Army {
    @JsonProperty
    private final Unit[][] playerUnits;
    @JsonProperty
    private General general;

    @JsonCreator
    public Army(@JsonProperty("playerUnits") Unit[][] playerUnits,@JsonProperty("general") General general)
            throws BoardException, UnitException {
        if (playerUnits == null || general == null){
            throw new BoardException(BoardExceptionTypes.NULL_POINTER);
        }
        if (Arrays.stream(playerUnits).noneMatch(x -> Arrays.asList(x).contains(general))) {
            throw new BoardException(BoardExceptionTypes.GENERAL_IS_NOT_IN_ARMY);
        }
        this.playerUnits = playerUnits;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if(playerUnits[i][j].equals(general)){
                    playerUnits[i][j] = general;
                }
            }
        }
        this.general = general;
    }

    public Army(Army army) throws UnitException, BoardException {
        if (army == null) {
            throw new BoardException(BoardExceptionTypes.NULL_POINTER);
        }
        playerUnits = new Unit[2][3];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (army.playerUnits[i][j] == null) {
                    throw new BoardException(BoardExceptionTypes.NULL_POINTER);
                }
                if (army.playerUnits[i][j] == army.general) {
                    general = new General(army.general);
                    playerUnits[i][j] = general;
                } else {
                    playerUnits[i][j] = new Unit(army.playerUnits[i][j]);
                }
            }
        }
    }

    public Unit[][] getPlayerUnits() {
        return playerUnits;
    }

    public final General getGeneral() throws UnitException {
        return general;
    }

    public boolean equals(Army army) {
        if (this == army) return true;
        if (army == null || getClass() != army.getClass()) return false;
        if(!this.general.equals(army.general)){
            return false;
        }
        for(int i = 0; i < 2; i++){
            for (int j = 0; j < 3; j++) {
                if(!this.playerUnits[i][j].equals(army.playerUnits[i][j])){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(general);
        result = 31 * result + Arrays.deepHashCode(playerUnits);
        return result;
    }
}
