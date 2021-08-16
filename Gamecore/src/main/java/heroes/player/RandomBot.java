package heroes.player;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.Visualisable;
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

/**
 * Случайный бот.
 * Случайнвм образом выбирает армию и случайно делает ходы.
 **/

public class RandomBot extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(RandomBot.class);

    /**
     * Фабрика ботов
     **/

    public static class RandomBotFactory extends BaseBotFactory {
        @Override
        public RandomBot createBot(final Fields fields) throws GameLogicException {
            return new RandomBot(fields);
        }
    }

    public RandomBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            final Random r = new Random();
            final Unit[][] armyArr = new Unit[2][3];
            final UnitTypes[] unitTypes = UnitTypes.values();
            final GeneralTypes[] generalTypes = GeneralTypes.values();
            final General general = new General(generalTypes[r.nextInt(generalTypes.length)]);
            armyArr[r.nextInt(2)][r.nextInt(3)] = general;
            try {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (armyArr[i][j] == null) {
                            armyArr[i][j] = new Unit(unitTypes[r.nextInt(unitTypes.length)]);
                        }
                    }
                }
            } catch (UnitException e) {
                logger.error("Error creating unit in RandomBot", e);
            }
            return new Army(armyArr, general);
        } catch (final BoardException | UnitException e) {
            logger.error("Error creating army in RandomBot", e);
            return null;
        }
    }

    /**
     * Из списка активных юнитов своей стороны выбирает атакующего
     * Из списка живых юнитов противника выбирает цель
     * Учитывает смену поля при ActionTypes.HEALING
     *
     * @param board - состояние игры
     * @return - ответ
     * @throws GameLogicException ошибка логики
     */
    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        Random r = new Random();


        Fields defField = (getField() == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final List<Position> posAttack = new ArrayList<>(6);
        final List<Position> posDefend = new ArrayList<>(6);

        final Unit[][] armyAttack = board.getArmy(getField());
        final Unit[][] armyDefend = board.getArmy(defField);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (armyAttack[i][j].isActive()) {
                    posAttack.add(new Position(i, j, getField()));
                }
                if (armyDefend[i][j].isAlive()) {
                    posDefend.add(new Position(i, j, defField));
                }
            }
        }

        final Position attackerPos = posAttack.get(r.nextInt(posAttack.size()));
        if(r.nextInt(100) < 20){
            return new Answer(attackerPos, attackerPos, ActionTypes.DEFENSE);
        }
        final ActionTypes attackType = board.getUnitByCoordinate(attackerPos).getActionType();
        if (attackType == ActionTypes.HEALING) {
            defField = getField();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    if (armyAttack[i][j].isAlive() && !posAttack.contains(new Position(i, j, getField()))) {
                        posAttack.add(new Position(i, j, getField()));
                    }
                }
            }
        }

        final Position defenderPos = (defField == getField()) ?
                posAttack.get(r.nextInt(posAttack.size())) :
                posDefend.get(r.nextInt(posDefend.size()));

        return new Answer(attackerPos, defenderPos, attackType);
    }
}
