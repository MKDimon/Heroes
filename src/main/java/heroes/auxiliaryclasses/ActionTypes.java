package heroes.auxiliaryclasses;

public enum ActionTypes {
    CLOSE_COMBAT(false),
    RANGE_COMBAT(false),
    HEALING(false),
    AREA_DAMAGE(true),
    DEFENSE(false);

    private final boolean massEffect;

    ActionTypes(boolean isMassEffect) { this.massEffect = isMassEffect; }

    public boolean isMassEffect() { return massEffect; }
}
