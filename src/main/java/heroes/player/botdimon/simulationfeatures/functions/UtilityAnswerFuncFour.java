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

    private static final Map<ActionTypes, Double> valueActions = new HashMap<>();
    static {
        valueActions.put(ActionTypes.DEFENSE, 1.);
        valueActions.put(ActionTypes.HEALING, 3.);
        valueActions.put(ActionTypes.CLOSE_COMBAT, 2.);
        valueActions.put(ActionTypes.RANGE_COMBAT, 2.5);
        valueActions.put(ActionTypes.AREA_DAMAGE, 3.);
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
                result += 1000.;
            }
            if (enemyGeneral.isAlive()) {
                result -= 1000.;
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
        return 0;
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
                            (1. + valueActions.get(enemiesArmy[i][j].getActionType()) *
                            1. * enemiesArmy[i][j].getCurrentHP() / enemiesArmy[i][j].getMaxHP(), DEGREE_PRIORITY));
                }
                else {
                    result += IS_DEATH_PRIORITY * valueActions.get(enemiesArmy[i][j].getActionType());
                }
                if (army[i][j].isAlive()) {
                    result += HP_PRIORITY * Math.pow(HP_RATE, Math.pow
                            (1. + valueActions.get(army[i][j].getActionType()) *
                                    1. *  army[i][j].getCurrentHP() / army[i][j].getMaxHP(), DEGREE_PRIORITY));
                }
                else {
                    result -= IS_DEATH_PRIORITY * valueActions.get(army[i][j].getActionType());
                }
            }
        }

        result += checkGameEnding(board, field);

        return result;
    }
}
