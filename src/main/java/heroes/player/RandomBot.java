package heroes.player;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
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

public class RandomBot implements IPlayer{
    Logger logger = LoggerFactory.getLogger(RandomBot.class);
    private Fields field;

    public RandomBot(Fields field){
        this.field = field;
    }

    @Override
    public Army getArmy() {
        try {
            Random r = new Random();
            Unit[][] armyArr = new Unit[2][3];
            UnitTypes[] unitTypes = UnitTypes.values();
            General general = null;
            try {
                GeneralTypes[] generalTypes = GeneralTypes.values();
                general = new General(generalTypes[r.nextInt(generalTypes.length)]);
            } catch (UnitException e) {
                logger.error("Error creating general in RandomBot", e);
            }
            armyArr[r.nextInt(2)][r.nextInt(3)] = general;
            try {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (armyArr[i][j] == null) {
                            armyArr[i][j] = new Unit(unitTypes[r.nextInt(unitTypes.length)]);
                        }
                    }
                }
            } catch (UnitException e){
                logger.error("Error creating unit in RandomBot", e);
            }
            return new Army(armyArr, general);
        } catch (BoardException e) {
            logger.error("Error creating army in RandomBot", e);
            return null;
        }
    }

    /**
     * Из списка активных юнитов своей стороны выбирает атакующего
     * Из списка живых юнитов противника выбирает цель
     * Учитывает смену поля при ActionTypes.HEALING
     *
     * @param board
     * @return
     * @throws GameLogicException
     */
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
