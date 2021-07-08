package heroes.player;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestBot implements IPlayer {
    Logger logger = LoggerFactory.getLogger(TestBot.class);

    Fields field;

    public TestBot(final Fields f) {
        field = f;
    }

    @Override
    public Army getArmy() {
        try {
            if (field == Fields.PLAYER_ONE) {
                General general = new General(GeneralTypes.COMMANDER);
                Unit[][] army = new Unit[2][3];
                army[0][0] = new Unit(UnitTypes.SWORDSMAN);
                army[1][0] = new Unit(UnitTypes.HEALER);
                army[0][1] = general;
                army[1][1] = new Unit(UnitTypes.MAGE);
                army[0][2] = new Unit(UnitTypes.SWORDSMAN);
                army[1][2] = new Unit(UnitTypes.MAGE);

                return new Army(army, general);
            } else {
                General general = new General(GeneralTypes.ARCHMAGE);
                Unit[][] army = new Unit[2][3];
                army[0][0] = new Unit(UnitTypes.SWORDSMAN);
                army[1][0] = new Unit(UnitTypes.HEALER);
                army[0][1] = new Unit(UnitTypes.SWORDSMAN);
                army[1][1] = general;
                army[0][2] = new Unit(UnitTypes.SWORDSMAN);
                army[1][2] = new Unit(UnitTypes.HEALER);

                return new Army(army, general);
            }

        } catch (UnitException e) {
            logger.error("Error creating unit in TestBot", e);
            return null;
        }
    }

    @Override
    public Answer getAnswer(Board board) throws GameLogicException {
        Random r = new Random();


        Fields defField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        List<Position> posAttack = new ArrayList<>();
        List<Position> posDefend = new ArrayList<>();

        Unit[][] armyAttack = board.getArmy(field);
        Unit[][] armyDefend = board.getArmy(defField);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (armyAttack[i][j].isActive()) {
                    posAttack.add(new Position(i, j, field));
                }
                if (armyDefend[i][j].isAlive()) {
                    posDefend.add(new Position(i, j, defField));
                }
            }
        }

        Position attackerPos = posAttack.get(r.nextInt(posAttack.size()));
        ActionTypes attackType = board.getUnitByCoordinate(attackerPos).getActionType();
        if (attackType == ActionTypes.HEALING) {
            defField = field;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    if (armyAttack[i][j].isAlive() && !posAttack.contains(new Position(i, j, field))) {
                        posAttack.add(new Position(i, j, field));
                    }
                }
            }
        }

        Position defenderPos = (defField == field)?
                posAttack.get(r.nextInt(posAttack.size())):
                posDefend.get(r.nextInt(posDefend.size()));

        return new Answer(attackerPos, defenderPos, attackType);
    }

    @Override
    public Fields getField() {
        return field;
    }
}
