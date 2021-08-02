package heroes.player.botdimon.simulationfeatures.functions;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.units.General;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Четвертое поколение
 * Экспоненциальная
 */
public class UtilityAnswerFuncFour implements IUtilityFunc {
    private final Logger logger = LoggerFactory.getLogger(UtilityAnswerFuncFour.class);

    private static final double HP_PRIORITY = 2;
    private static final double HP_RATE = 1.35;
    private static final double IS_DEATH_PRIORITY = 300;
    private static final double DEGREE_PRIORITY = 2.0;

    private static final Map<ActionTypes, Double> valueActionsEnemy = new HashMap<>();
    private static final Map<ActionTypes, Double> valueActionsAlly = new HashMap<>();
    static {
        valueActionsEnemy.put(ActionTypes.DEFENSE, 1.);
        valueActionsEnemy.put(ActionTypes.HEALING, 3.);
        valueActionsEnemy.put(ActionTypes.CLOSE_COMBAT, 2.);
        valueActionsEnemy.put(ActionTypes.RANGE_COMBAT, 2.);
        valueActionsEnemy.put(ActionTypes.AREA_DAMAGE, 4.);

        valueActionsAlly.put(ActionTypes.DEFENSE, 1.);
        valueActionsAlly.put(ActionTypes.HEALING, 3.);
        valueActionsAlly.put(ActionTypes.CLOSE_COMBAT, 2.);
        valueActionsAlly.put(ActionTypes.RANGE_COMBAT, 2.);
        valueActionsAlly.put(ActionTypes.AREA_DAMAGE, 4.);
    }

    private double checkGenerals(final Board board, final Fields field) {
        double result = 0;
        final General general;
        final General enemyGeneral;
        try {
            if (field == Fields.PLAYER_ONE) {
                general = board.getGeneralPlayerOne();
                enemyGeneral = board.getGeneralPlayerTwo();
            } else {
                general = board.getGeneralPlayerTwo();
                enemyGeneral = board.getGeneralPlayerOne();
            }
            if (general.isAlive()) {
                result += 1500. * valueActionsAlly.get(general.getActionType());
            }
            if (enemyGeneral.isAlive()) {
                result -= 1500. * valueActionsEnemy.get(enemyGeneral.getActionType());
            }
        }
        catch(UnitException e){
            logger.error("Check general utility function error", e);
        }
        return result;
    }

    private double checkGameEnding(final Board board, final Fields field) {
        if (field == Fields.PLAYER_ONE) {
            if (board.getStatus() == GameStatus.PLAYER_ONE_WINS) {
                return Double.MAX_VALUE;
            }
        }
        if (field == Fields.PLAYER_TWO) {
            if (board.getStatus() == GameStatus.PLAYER_TWO_WINS) {
                return Double.MAX_VALUE;
            }
        }
        return Double.MIN_VALUE;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        double result = 0;

        final Fields enemyField = (field == Fields.PLAYER_ONE)? Fields.PLAYER_TWO: Fields.PLAYER_ONE;
        final Unit[][] army = board.getArmy(field);
        final Unit[][] enemiesArmy = board.getArmy(enemyField);

        result += checkGenerals(board, field);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (enemiesArmy[i][j].isAlive()) {
                    result -= HP_PRIORITY * Math.pow(HP_RATE, Math.pow
                            (1. + valueActionsEnemy.get(enemiesArmy[i][j].getActionType()) *
                            1. * enemiesArmy[i][j].getCurrentHP() / enemiesArmy[i][j].getMaxHP(), DEGREE_PRIORITY));
                }
                else {
                    result += IS_DEATH_PRIORITY * valueActionsEnemy.get(enemiesArmy[i][j].getActionType());
                }
                if (army[i][j].isAlive()) {
                    result += HP_PRIORITY * Math.pow(HP_RATE, Math.pow
                            (1. + valueActionsAlly.get(army[i][j].getActionType()) *
                                    1. *  army[i][j].getCurrentHP() / army[i][j].getMaxHP(), DEGREE_PRIORITY));
                }
                else {
                    result -= IS_DEATH_PRIORITY * valueActionsAlly.get(army[i][j].getActionType());
                }
            }
        }

        result += checkGameEnding(board, field);

        return result;
    }
}
