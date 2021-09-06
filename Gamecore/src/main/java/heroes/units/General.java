package heroes.units;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;

import java.util.Objects;

/**
 * Класс - генерал
 **/

public class General extends Unit {
    @JsonProperty
    public final int inspirationArmorBonus;
    @JsonProperty
    public final int inspirationDamageBonus;
    @JsonProperty
    public final int inspirationAccuracyBonus;

    @JsonCreator
    public General(@JsonProperty("defenseArmor") final int defenseArmor, @JsonProperty("bonusArmor") final int bonusArmor,
                   @JsonProperty("bonusDamage") final int bonusDamage,
                   @JsonProperty("bonusAccuracy") final int bonusAccuracy,
                   @JsonProperty("actionType") final ActionTypes actionType,
                   @JsonProperty("maxHP") final int maxHP, @JsonProperty("currentHP") final  int currentHP,
                   @JsonProperty("power") final int power, @JsonProperty("accuracy") final int accuracy,
                   @JsonProperty("armor") final int armor, @JsonProperty("isActive") final boolean isActive,
                   @JsonProperty("inspirationArmorBonus") final int inspirationArmorBonus,
                   @JsonProperty("inspirationDamageBonus") final int inspirationDamageBonus,
                   @JsonProperty("inspirationAccuracyBonus") final int inspirationAccuracyBonus)
            throws UnitException {
        super(defenseArmor, bonusArmor, bonusDamage, bonusAccuracy, actionType, maxHP, currentHP, power, accuracy, armor, isActive);
        this.inspirationArmorBonus = inspirationArmorBonus;
        this.inspirationDamageBonus = inspirationDamageBonus;
        this.inspirationAccuracyBonus = inspirationAccuracyBonus;
    }

    public General(final GeneralTypes generalType) throws UnitException {
        super(generalType.unitType);
        inspirationArmorBonus = generalType.inspirationArmorBonus;
        inspirationDamageBonus = generalType.inspirationDamageBonus;
        inspirationAccuracyBonus = generalType.inspirationAccuracyBonus;
    }

    public General(final General general) throws UnitException {
        super(general);
        inspirationArmorBonus = general.inspirationArmorBonus;
        inspirationDamageBonus = general.inspirationDamageBonus;
        inspirationAccuracyBonus = general.inspirationAccuracyBonus;

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final General general = (General) o;
        return inspirationArmorBonus == general.inspirationArmorBonus && inspirationDamageBonus == general.inspirationDamageBonus && inspirationAccuracyBonus == general.inspirationAccuracyBonus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inspirationArmorBonus, inspirationDamageBonus, inspirationAccuracyBonus);
    }
}
