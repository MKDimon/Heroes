package heroes.units;

import heroes.units.auxiliaryclasses.ActionTypes;

public enum UnitTypes {
    SWORDSMAN(100, 30, 85, 30, ActionTypes.CLOSE_COMBAT),
    MAGE(70, 15,85,10,ActionTypes.AREA_DAMAGE),
    BOWMAN(70,25,85,20,ActionTypes.RANGE_COMBAT),
    HEALER(60, 30, 100, 15, ActionTypes.HEALING);

    private ActionTypes actionType;
    private int HP;
    private int power; //Сила удара или лечения
    private int accuracy;
    private int armor;

    UnitTypes(int HP, int power, int accuracy, int armor, ActionTypes actionType){
        this.HP = HP;
        this.power = power;
        this.armor = armor;
        this.accuracy = accuracy;
        this.actionType = actionType;
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    public int getHP() {
        return HP;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getArmor() {
        return armor;
    }
}
