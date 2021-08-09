package heroes.player;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
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
 * Тестовый бот. Имеет фиксированный состав армии. Дейвтсвия выбирает случайнвм образом.
 **/

public class TestBot extends BaseBot implements Visualisable {
    Logger logger = LoggerFactory.getLogger(TestBot.class);

    protected TerminalWrapper tw;

    @Override
    public void setTerminal(TerminalWrapper tw) {
        super.tw = tw;
    }

    /**
     * Фабрика ботов
     **/

    public static class TestBotFactory extends BaseBotFactory {

        @Override
        public TestBot createBot(final Fields fields) throws GameLogicException {
            return new TestBot(fields);
        }
    }

    public TestBot(final Fields f) throws GameLogicException {
        super(f);
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            final General general = new General(GeneralTypes.COMMANDER);
            final Unit[][] army = new Unit[2][3];
            army[0][0] = new Unit(UnitTypes.SWORDSMAN);
            army[1][0] = new Unit(UnitTypes.BOWMAN);
            army[0][1] = new Unit(UnitTypes.SWORDSMAN);
            army[1][1] = new Unit(UnitTypes.HEALER);
            army[0][2] = general;
            army[1][2] = new Unit(UnitTypes.BOWMAN);
            return new Army(army, general);
        } catch (UnitException | BoardException e) {
            logger.error("Error creating unit in TestBot", e);
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
     * @throws GameLogicException - ошибки логики
     */
    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        Random r = new Random();

        Fields defField = (getField() == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        List<Position> posAttack = new ArrayList<>();
        List<Position> posDefend = new ArrayList<>();

        Unit[][] armyAttack = board.getArmy(getField());
        Unit[][] armyDefend = board.getArmy(defField);

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

        Position attackerPos = posAttack.get(r.nextInt(posAttack.size()));
        if(r.nextInt(100) < 20){
            return new Answer(attackerPos, attackerPos, ActionTypes.DEFENSE);
        }
        ActionTypes attackType = board.getUnitByCoordinate(attackerPos).getActionType();
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

        Position defenderPos = (defField == getField()) ?
                posAttack.get(r.nextInt(posAttack.size())) :
                posDefend.get(r.nextInt(posDefend.size()));

        return new Answer(attackerPos, defenderPos, attackType);
    }
}
