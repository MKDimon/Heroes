package heroes.units;

import com.fasterxml.jackson.annotation.*;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = General.class)
}
)
public class Unit {
    @JsonProperty
    private int defenseArmor;
    @JsonProperty
    private int bonusArmor;
    @JsonProperty
    private int bonusDamage;
    @JsonProperty
    private int bonusAccuracy;
    @JsonProperty
    private final ActionTypes actionType;
    @JsonProperty
    private final int maxHP;
    @JsonProperty
    private int currentHP;
    @JsonProperty
    private int power; //Сила удара или лечения
    @JsonProperty
    private int accuracy;
    @JsonProperty
    private int armor;
    @JsonProperty
    private boolean isActive;

    @JsonCreator
    public Unit(@JsonProperty("defenseArmor") int defenseArmor, @JsonProperty("bonusArmor") int bonusArmor,@JsonProperty("bonusDamage") int bonusDamage,
                @JsonProperty("bonusAccuracy") int bonusAccuracy, @JsonProperty("actionType") ActionTypes actionType,
                @JsonProperty("maxHP")int maxHP, @JsonProperty("currentHP") int currentHP,
                @JsonProperty("power") int power, @JsonProperty("accuracy") int accuracy,
                @JsonProperty("armor") int armor, @JsonProperty("isActive") boolean isActive) throws UnitException {
        if(actionType == null){
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        this.defenseArmor = defenseArmor;
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

    @JsonIgnore
    public boolean isActive() {
        return isActive && isAlive();
    }

    public void setActive(boolean active) {
        isActive = active && isAlive();
    }

    public Unit(UnitTypes unitType) throws UnitException {
        this(0,0,0,0, unitType.actionType,unitType.HP,unitType.HP,unitType.power,
                unitType.accuracy, unitType.armor, true);
    }

    public Unit(Unit unit) throws UnitException {
        if (unit == null || unit.getActionType() == null) {
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        defenseArmor = unit.defenseArmor;
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
        bonusArmor = inspirationArmorBonus;
    }

    public void deinspire() {
        bonusDamage = 0;
        bonusAccuracy = 0;
        bonusArmor = 0;
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
        return armor + bonusArmor + defenseArmor;
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

    @JsonIgnore
    public boolean isAlive() {
        return currentHP > 0;
    }

    public void defense() {
        defenseArmor += 20;
        isActive = false;
    }

    public int getDefenseArmor() {
        return defenseArmor;
    }

    public void setDefenseArmor(int defenseArmor) {
        this.defenseArmor = defenseArmor;
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
