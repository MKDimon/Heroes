package heroes.units;

public enum GeneralTypes {
    ARCHMAGE(UnitTypes.MAGE),
    COMMANDER(UnitTypes.SWORDSMAN),
    SNIPER(UnitTypes.BOWMAN);

    private UnitTypes unitType;
    GeneralTypes(UnitTypes unitType){
        this.unitType = unitType;
    }

    public UnitTypes getUnitType(){
        return unitType;
    }

}
