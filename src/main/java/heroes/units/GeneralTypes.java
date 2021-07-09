package heroes.units;

import heroes.auxiliaryclasses.Deinspiration;
import heroes.auxiliaryclasses.Inspiration;

public enum GeneralTypes {
    ARCHMAGE(UnitTypes.MAGE, u -> u.setPower(u.getPower() + 10), u -> u.setPower(u.getPower() - 10)),
    COMMANDER(UnitTypes.SWORDSMAN, u -> u.setArmor(u.getArmor() + 10), u -> u.setArmor(u.getArmor() - 10)),
    SNIPER(UnitTypes.BOWMAN,
            u -> {
                int buffedAccuracy = u.getAccuracy() + 10;
                u.setAccuracy(buffedAccuracy);
            },
            u -> {
                int debuffedAccuracy = u.getAccuracy() - 10;
                u.setAccuracy(debuffedAccuracy);
            });

    private final UnitTypes unitType;
    private final Inspiration inspiration;
    private final Deinspiration deinspiration;

    GeneralTypes(UnitTypes unitType, Inspiration inspiration, Deinspiration deinspiration) {
        this.unitType = unitType;
        this.inspiration = inspiration;
        this.deinspiration = deinspiration;
    }

    public UnitTypes getUnitType() {
        return unitType;
    }

    public Inspiration getInspiration() {
        return inspiration;
    }

    public Deinspiration getDeinspiration() {
        return deinspiration;
    }

}
