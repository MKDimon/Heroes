package heroes.player;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBot implements IPlayer  {
    Logger logger = LoggerFactory.getLogger(TestBot.class);

    Unit[][] army;
    General general;
    Fields field;

    public TestBot(final Fields f, final int TEST_PARAMETER) {
        field = f;
        createArmyAndGeneral(TEST_PARAMETER);
    }

    @Override
    public boolean createArmyAndGeneral(int TEST_PARAMETER) {
        try {
            if (TEST_PARAMETER == 1) {
                general = new General(GeneralTypes.COMMANDER);
                army = new Unit[2][3];
                army[0][0] = new Unit(UnitTypes.SWORDSMAN);     army[1][0] = new Unit(UnitTypes.HEALER);
                army[0][1] = general;                           army[1][1] = new Unit(UnitTypes.BOWMAN);
                army[0][2] = new Unit(UnitTypes.SWORDSMAN);     army[1][2] = new Unit(UnitTypes.MAGE);
                return true;
            } else {
                general = new General(GeneralTypes.ARCHMAGE);
                army = new Unit[2][3];
                army[0][0] = new Unit(UnitTypes.SWORDSMAN);     army[1][0] = new Unit(UnitTypes.HEALER);
                army[0][1] = new Unit(UnitTypes.SWORDSMAN);     army[1][1] = general;
                army[0][2] = new Unit(UnitTypes.SWORDSMAN);     army[1][2] = new Unit(UnitTypes.MAGE);
                return true;
            }

        } catch (UnitException e) {
            logger.error("Error creating unit in TestBot", e);
            return false;
        }
    }

    @Override
    public Unit[][] getArmy() {
        return army;
    }

    @Override
    public General getGeneral() {
        return general;
    }

    @Override
    public Answer getAnswer(Board board) {
        return null;
    }

    @Override
    public Fields getField() {
        return field;
    }
}
