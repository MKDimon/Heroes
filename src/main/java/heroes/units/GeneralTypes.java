package heroes.units;


public enum GeneralTypes {
    ARCHMAGE(UnitTypes.MAGE, 0, 10, 0),
    COMMANDER(UnitTypes.SWORDSMAN, 10, 0, 0),
    SNIPER(UnitTypes.BOWMAN,0,0,10)
    ;

    public final UnitTypes unitType;
    public final int inspirationArmorBonus;
    public final int inspirationDamageBonus;
    public final int inspirationAccuracyBonus;

    GeneralTypes(UnitTypes unitType, int inspirationArmorBonus, int inspirationDamageBonus, int inspirationAccuracyBonus) {
        this.unitType = unitType;
        this.inspirationArmorBonus = inspirationArmorBonus;
        this.inspirationDamageBonus = inspirationDamageBonus;
        this.inspirationAccuracyBonus = inspirationAccuracyBonus;
    }
}
