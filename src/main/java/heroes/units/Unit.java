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

/**
 * Класс - игровой юнит
 */

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
    public Unit(@JsonProperty("defenseArmor") final int defenseArmor, @JsonProperty("bonusArmor") final int bonusArmor,
                @JsonProperty("bonusDamage") final int bonusDamage, @JsonProperty("bonusAccuracy") final int bonusAccuracy,
                @JsonProperty("actionType") final ActionTypes actionType,
                @JsonProperty("maxHP") final int maxHP, @JsonProperty("currentHP") final int currentHP,
                @JsonProperty("power") final int power, @JsonProperty("accuracy") final int accuracy,
                @JsonProperty("armor") final int armor, @JsonProperty("isActive") final boolean isActive) throws UnitException {
        if (actionType == null){
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

    public void setActive(final boolean active) {
        isActive = active && isAlive();
    }

    public Unit(final UnitTypes unitType) throws UnitException {
        this(0,0,0,0, unitType.actionType,unitType.HP,unitType.HP,unitType.power,
                unitType.accuracy, unitType.armor, true);
    }

    public Unit(final Unit unit) throws UnitException {
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

    public void inspire(final int inspirationArmorBonus, final int inspirationDamageBonus,
                        final int inspirationAccuracyBonus) {
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

    public void setCurrentHP(final int currentHP) {
        this.currentHP = currentHP;
        if (currentHP > maxHP) {
            this.currentHP = maxHP;
        }
        if (currentHP <= 0) {
            isActive = false;
        }
    }

    public void setPower(final int power) throws UnitException {
        if (power <= 0) {
            throw new UnitException(UnitExceptionTypes.INCORRECT_POWER);
        }
        this.power = power;
    }

    public void setAccuracy(final int accuracy) throws UnitException {
        this.accuracy = Math.min(Math.max(accuracy, 0), 100);
    }

    public void setArmor(final int armor) throws UnitException {
        if (armor < 0) {
            throw new UnitException(UnitExceptionTypes.INCORRECT_ARMOR);
        }
        this.armor = armor;
    }

    @JsonIgnore
    public boolean isAlive() {
        return currentHP > 0;
    }

    public void defense() {
        defenseArmor += 20;
        isActive = false;
    }

    public void setDefenseArmor(final int defenseArmor) {
        this.defenseArmor = defenseArmor;
    }

    public int getDefenseArmor() {
        return defenseArmor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return defenseArmor == unit.defenseArmor && bonusArmor == unit.bonusArmor &&
                bonusDamage == unit.bonusDamage && bonusAccuracy == unit.bonusAccuracy &&
                maxHP == unit.maxHP && currentHP == unit.currentHP && power == unit.power &&
                accuracy == unit.accuracy && armor == unit.armor && isActive == unit.isActive &&
                actionType == unit.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(defenseArmor, bonusArmor, bonusDamage, bonusAccuracy, actionType, maxHP,
                currentHP, power, accuracy, armor, isActive);
    }
}
