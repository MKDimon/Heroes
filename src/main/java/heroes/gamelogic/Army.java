package heroes.gamelogic;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.General;
import heroes.units.Unit;

public class Army {
    private Unit[][] playerUnits;
    private General general;

    public Army(Unit[][] playerUnits, General general) {
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
}
