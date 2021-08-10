package gamecore.units;


public enum GeneralTypes {
    ARCHMAGE(UnitTypes.MAGE, 0, 5, 0),
    COMMANDER(UnitTypes.SWORDSMAN, 10, 0, 0),
    SNIPER(UnitTypes.BOWMAN,0,0,10)
    ;

    public final UnitTypes unitType;
    public final int inspirationArmorBonus;
    public final int inspirationDamageBonus;
    public final int inspirationAccuracyBonus;

    GeneralTypes(final UnitTypes unitType, final int inspirationArmorBonus,final int inspirationDamageBonus,
                 final int inspirationAccuracyBonus) {
        this.unitType = unitType;
        this.inspirationArmorBonus = inspirationArmorBonus;
        this.inspirationDamageBonus = inspirationDamageBonus;
        this.inspirationAccuracyBonus = inspirationAccuracyBonus;
    }
}
