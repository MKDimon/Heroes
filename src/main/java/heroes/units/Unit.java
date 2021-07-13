package heroes.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;

import java.util.Objects;

public class Unit {
    @JsonProperty
    private int bonusArmor;
    private int bonusDamage;
    private int bonusAccuracy;
    @JsonProperty
    private final ActionTypes actionType;
    private final int maxHP;
    private int currentHP;
    private int power; //Сила удара или лечения
    private int accuracy;
    private int armor;
    private boolean isActive;


    public boolean isActive() {
        return isActive && isAlive();
    }

    public void setActive(boolean active) {
        isActive = active && isAlive();
    }

    @JsonCreator
    public Unit(int bonusArmor, int bonusDamage, int bonusAccuracy, ActionTypes actionType, int maxHP, int currentHP, int power,
                int accuracy, int armor, boolean isActive) throws UnitException {
        if(actionType == null){
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        this.bonusArmor = bonusArmor;
        this.bonusDamage = bonusDamage;
        this.bonusAccuracy = bonusAccuracy;
        this.actionType = actionType;
        this.maxHP = maxHP;
        setCurrentHP(currentHP);
        setPower(power);
        setAccuracy(accuracy);
        setArmor(armor);
        setActive(isActive);
    }

    public Unit(UnitTypes unitType) throws UnitException {
        this(0,0,0, unitType.actionType,unitType.HP,unitType.HP,unitType.power,
                unitType.accuracy, unitType.armor, true);
    }

    public Unit(Unit unit) throws UnitException {
        if (unit == null || unit.getActionType() == null) {
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        maxHP = unit.maxHP;
        setCurrentHP(unit.currentHP);
        setPower(unit.power);
        setArmor(unit.armor);
        setAccuracy(unit.accuracy);
        actionType = unit.actionType;
        isActive = unit.isActive;
        bonusArmor = unit.bonusArmor;
        bonusAccuracy = unit.bonusAccuracy;
        bonusDamage = unit.bonusDamage;
    }

    public void inspire(int inspirationArmorBonus, int inspirationDamageBonus, int inspirationAccuracyBonus) {
        bonusDamage = inspirationDamageBonus;
        bonusAccuracy = inspirationAccuracyBonus;
        bonusArmor += inspirationArmorBonus;
    }

    public void deinspire(int inspirationBonusArmor) {
        bonusDamage = 0;
        bonusAccuracy = 0;
        bonusArmor -= inspirationBonusArmor;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getPower() {
        return power + bonusDamage;
    }

    public int getAccuracy() {
        return accuracy + bonusAccuracy;
    }

    public int getArmor() {
        return armor + bonusArmor;
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    public void setCurrentHP(int currentHP) {
        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
        if (currentHP <= 0) {
            isActive = false;
        }
        this.currentHP = currentHP;
    }

    public void setPower(int power) throws UnitException {
        if (power <= 0) {
            throw new UnitException(UnitExceptionTypes.INCORRECT_POWER);
        }
        this.power = power;
    }

    public void setAccuracy(int accuracy) throws UnitException {
        this.accuracy = Math.min(Math.max(accuracy, 0), 100);
    }

    public void setArmor(int armor) throws UnitException {
        if (armor < 0) {
            throw new UnitException(UnitExceptionTypes.INCORRECT_ARMOR);
        }
        this.armor = armor;
    }

    public void setBonusArmor(int bonusArmor) {
        this.bonusArmor = bonusArmor;
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void defense() {
        bonusArmor += 20;
        isActive = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return bonusArmor == unit.bonusArmor && bonusDamage == unit.bonusDamage && bonusAccuracy == unit.bonusAccuracy && maxHP == unit.maxHP && currentHP == unit.currentHP && power == unit.power && accuracy == unit.accuracy && armor == unit.armor && isActive == unit.isActive && actionType == unit.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bonusArmor, bonusDamage, bonusAccuracy, actionType, maxHP, currentHP, power, accuracy, armor, isActive);
    }
}
