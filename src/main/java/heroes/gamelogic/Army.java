package heroes.gamelogic;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;

public class Army {
    private Unit[][] army;
    private General general;
    private Position generalPosition;
    private Fields field;

    public Army(Fields field, Unit[][] army, General general, Position generalPosition) throws GameLogicException {
        if(field == null || army == null || general == null || generalPosition == null){
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.field = field;
        this.general = general;
        this.army = army;
        this.generalPosition = generalPosition;
    }

    public Army(Army army) throws GameLogicException, UnitException {
            this(army.field, copyArmyArray(army.army, army.general), army.general,
                    new Position(army.generalPosition.X(), army.generalPosition.Y(), army.field));
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

    public Position getGeneralPosition() {
        return generalPosition;
    }

    public Fields getField() {
        return field;
    }


}
