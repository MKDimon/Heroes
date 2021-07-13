package heroes.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;

import java.util.Objects;


public class General extends Unit {
    @JsonProperty
    public final int inspirationArmorBonus;
    @JsonProperty
    public final int inspirationDamageBonus;
    @JsonProperty
    public final int inspirationAccuracyBonus;

    @JsonCreator
    public General(int bonusArmor, int bonusDamage, int bonusAccuracy, ActionTypes actionType, int maxHP,
                   int currentHP, int power, int accuracy, int armor, boolean isActive,
                   int inspirationArmorBonus, int inspirationDamageBonus, int inspirationAccuracyBonus)
            throws UnitException {
        super(bonusArmor, bonusDamage, bonusAccuracy, actionType, maxHP, currentHP, power, accuracy, armor, isActive);
        this.inspirationArmorBonus = inspirationArmorBonus;
        this.inspirationDamageBonus = inspirationDamageBonus;
        this.inspirationAccuracyBonus = inspirationAccuracyBonus;
    }

    public General(GeneralTypes generalType) throws UnitException {
        super(generalType.unitType);
        inspirationArmorBonus = generalType.inspirationArmorBonus;
        inspirationDamageBonus = generalType.inspirationDamageBonus;
        inspirationAccuracyBonus = generalType.inspirationAccuracyBonus;
    }

    public General(General general) throws UnitException {
        super(general);
        inspirationArmorBonus = general.inspirationArmorBonus;
        inspirationDamageBonus = general.inspirationDamageBonus;
        inspirationAccuracyBonus = general.inspirationAccuracyBonus;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        General general = (General) o;
        return inspirationArmorBonus == general.inspirationArmorBonus && inspirationDamageBonus == general.inspirationDamageBonus && inspirationAccuracyBonus == general.inspirationAccuracyBonus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inspirationArmorBonus, inspirationDamageBonus, inspirationAccuracyBonus);
    }
}
