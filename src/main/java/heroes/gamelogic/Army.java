package heroes.gamelogic;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.General;
import heroes.units.Unit;

public class Army {
    private Unit[][] army;
    private General general;

    public Army(Unit[][] army, General general) throws GameLogicException {
        if(army == null || general == null){
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.general = general;
        this.army = army;
    }

    public Army(Army army) throws GameLogicException, UnitException {
            this(copyArmyArray(army.army, army.general), army.general);
    }

    private static Unit[][] copyArmyArray(Unit[][] units, General general) throws UnitException {
        Unit[][] result = new Unit[2][3];
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 3; j++){
                if(units[i][j] == general){
                    result[i][j] = new General(general);
                } else {
                    result[i][j] = new Unit(units[i][j]);
                }
            }
        }
        return result;
    }

    public Unit[][] getArmy() {
        return army;
    }

    public General getGeneral() {
        return general;
    }
}
