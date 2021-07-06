package heroes.units;

import heroes.auxiliaryclasses.*;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;

import java.util.Objects;

public class Unit {
    private ActionTypes actionType;
    private final int maxHP;
    private int currentHP;
    private int power; //Сила удара или лечения
    private int accuracy;
    private int armor;
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Unit(UnitTypes unitType) throws UnitException {
        if(unitType == null || unitType.getActionType() == null){
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        maxHP = unitType.getHP();
        setCurrentHP(maxHP);
        setPower(unitType.getPower());
        setArmor(unitType.getArmor());
        setAccuracy(unitType.getAccuracy());
        actionType = unitType.getActionType();
        isActive = true;
    }

    public void inspire(Inspiration inspiration) throws UnitException {
        if(inspiration == null){
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        inspiration.inspire(this);
    }

    public void deinspire(Deinspiration deinspiration) throws UnitException {
        deinspiration.deinspire(this);
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP(){
        return maxHP;
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

    public void setCurrentHP(int currentHP) throws UnitException {
        if(currentHP > maxHP){
            throw new UnitException(UnitExceptionTypes.INCORRECT_HP);
        }
        this.currentHP = currentHP;
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
        return currentHP > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return maxHP == unit.maxHP && currentHP == unit.currentHP && power == unit.power && accuracy == unit.accuracy && armor == unit.armor && isActive == unit.isActive && actionType == unit.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionType, maxHP, currentHP, power, accuracy, armor, isActive);
    }
}
