package heroes.units;

import heroes.auxiliaryclasses.ActionTypes;

public enum UnitTypes {
    SWORDSMAN(100, 30, 85, 30, ActionTypes.CLOSE_COMBAT),
    MAGE(70, 15, 85, 10, ActionTypes.AREA_DAMAGE),
    BOWMAN(70, 25, 85, 20, ActionTypes.RANGE_COMBAT),
    HEALER(60, 30, 100, 15, ActionTypes.HEALING);

    public final ActionTypes actionType;
    public final int HP;
    public final int power; //Сила удара или лечения
    public final int accuracy;
    public final int armor;

    UnitTypes(final int HP, final int power, final int accuracy, final int armor, final ActionTypes actionType) {
        this.HP = HP;
        this.power = power;
        this.armor = armor;
        this.accuracy = accuracy;
        this.actionType = actionType;
    }
}
