package gamecore.auxiliaryclasses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Типы атаки юнитов
 *
 * isMassEffect - эффект массовой атаки (все юниты)
 */
public enum ActionTypes {
    CLOSE_COMBAT(false),
    RANGE_COMBAT(false),
    HEALING(false),
    AREA_DAMAGE(true),
    DEFENSE(false);

    @JsonProperty
    private final boolean massEffect;

    @JsonCreator
    ActionTypes(final boolean isMassEffect) {
        this.massEffect = isMassEffect;
    }

    public boolean isMassEffect() {
        return massEffect;
    }
}
