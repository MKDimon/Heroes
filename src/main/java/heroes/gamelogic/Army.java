package heroes.gamelogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public Army(@JsonProperty("playerUnits") final Unit[][] playerUnits,
                @JsonProperty("general") final General general)
            throws BoardException, UnitException {
        Validator.checkNullPointer(playerUnits, general);
        if (Arrays.stream(playerUnits).noneMatch(x -> Arrays.asList(x).contains(general))) {
            throw new BoardException(BoardExceptionTypes.GENERAL_IS_NOT_IN_ARMY);
        }
        Validator.checkNullPointerInArmy(playerUnits);

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

    public Army(final Army army) throws UnitException, BoardException {
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

    @JsonIgnore
    public Unit[][] getPlayerUnits() {
        return playerUnits;
    }

    @JsonIgnore
    public final General getGeneral() throws UnitException {
        return general;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Army army = (Army)o;
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
