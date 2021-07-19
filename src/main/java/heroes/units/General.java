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
    public General(@JsonProperty("defenseArmor") int defenseArmor, @JsonProperty("bonusArmor") int bonusArmor,@JsonProperty("bonusDamage") int bonusDamage,
                   @JsonProperty("bonusAccuracy") int bonusAccuracy, @JsonProperty("actionType") ActionTypes actionType,
                   @JsonProperty("maxHP")int maxHP, @JsonProperty("currentHP") int currentHP,
                   @JsonProperty("power") int power, @JsonProperty("accuracy") int accuracy,
                   @JsonProperty("armor") int armor, @JsonProperty("isActive") boolean isActive,
                   @JsonProperty("inspirationArmorBonus") int inspirationArmorBonus,
                   @JsonProperty("inspirationDamageBonus") int inspirationDamageBonus,
                   @JsonProperty("inspirationAccuracyBonus") int inspirationAccuracyBonus)
            throws UnitException {
        super(defenseArmor, bonusArmor, bonusDamage, bonusAccuracy, actionType, maxHP, currentHP, power, accuracy, armor, isActive);
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
