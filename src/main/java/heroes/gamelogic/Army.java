package heroes.gamelogic;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.General;
import heroes.units.Unit;

import java.util.Arrays;
import java.util.Objects;

public class Army {
    private final Unit[][] playerUnits;
    private General general;

    public Army(Unit[][] playerUnits, General general) throws BoardException {
        if (Arrays.stream(playerUnits).noneMatch(x -> Arrays.asList(x).contains(general))) {
            throw new BoardException(BoardExceptionTypes.GENERAL_IS_NOT_IN_ARMY);
        }
        this.playerUnits = playerUnits;
        this.general = general;
    }

    public Army(Army army) throws UnitException, BoardException {
        playerUnits = new Unit[2][3];
        for (int i = 0; i < 2; i++) {
            if (army == null) {
                throw new BoardException(BoardExceptionTypes.NULL_POINTER);
            }
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

    public General getGeneral() {
        return general;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Army army = (Army) o;
        return Arrays.deepEquals(playerUnits, army.playerUnits) && Objects.equals(general, army.general);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(general);
        result = 31 * result + Arrays.deepHashCode(playerUnits);
        return result;
    }
}
