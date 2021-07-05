package heroes.units;

import heroes.units.auxiliaryclasses.ActionTypes;
import heroes.units.auxiliaryclasses.UnitException;
import heroes.units.auxiliaryclasses.UnitExceptionTypes;

import java.util.Objects;

public class Unit {
    private ActionTypes actionType;
    private int HP;
    private int power; //Сила удара или лечения
    private int accuracy;
    private int armor;
    private boolean isInspired;

    public Unit(UnitTypes unitType) throws UnitException {
        setHP(unitType.getHP());
        setPower(unitType.getPower());
        setArmor(unitType.getAccuracy());
        setAccuracy(unitType.getArmor());
        setActionType(actionType);
        isInspired = true;
    }

    public int getHP() {
        return HP;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getArmor() {
        return armor;
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    public void setActionType(ActionTypes actionType) throws UnitException {
        if (actionType == null) {
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        this.actionType = actionType;
    }

    public void setHP(int HP) throws UnitException {
        if(HP <= 0){
            throw new UnitException(UnitExceptionTypes.INCORRECT_HP);
        }
        this.HP = HP;
    }

    public void setPower(int power) throws UnitException {
        if(power <= 0){
            throw new UnitException(UnitExceptionTypes.INCORRECT_POWER);
        }
        this.power = power;
    }

    public void setAccuracy(int accuracy) throws UnitException {
        if(accuracy < 0 || accuracy > 100){
            throw new UnitException(UnitExceptionTypes.INCORRECT_ACCURACY);
        }
        this.accuracy = accuracy;
    }

    public void setArmor(int armor) throws UnitException {
        if(armor < 0){
            throw new UnitException(UnitExceptionTypes.INCORRECT_ARMOR);
        }
        this.armor = armor;
    }

    public boolean isAlive(){
        return HP > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return HP == unit.HP && power == unit.power && accuracy == unit.accuracy && armor == unit.armor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(HP, power, accuracy, armor);
    }
}
