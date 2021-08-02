package heroes.units;

import heroes.auxiliaryclasses.ActionTypes;

public enum UnitTypes {
    SWORDSMAN(80, 30, 70, 25, ActionTypes.CLOSE_COMBAT),
    MAGE(50, 5, 80, 10, ActionTypes.AREA_DAMAGE),
    BOWMAN(60, 25, 85, 15, ActionTypes.RANGE_COMBAT),
    HEALER(40, 30, 100, 10, ActionTypes.HEALING);

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
