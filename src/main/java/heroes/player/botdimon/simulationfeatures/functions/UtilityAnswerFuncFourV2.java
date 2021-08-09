package heroes.player.botdimon.simulationfeatures.functions;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Четвертое поколение
 * Экспоненциальная
 * Второй вариант
 */
public class UtilityAnswerFuncFourV2 implements IUtilityFunc {
    private final Logger logger = LoggerFactory.getLogger(UtilityAnswerFuncFourV2.class);

    private static final double HP_PRIORITY = 2;
    private static final double HP_RATE = Math.exp(1);
    private static final double IS_DEATH_PRIORITY = 1200;
    private static final double DEGREE_PRIORITY = Math.exp(-1);

    private static final Map<ActionTypes, Double> valueActions = new HashMap<>();
    static {
        valueActions.put(ActionTypes.HEALING, 2.0);
        valueActions.put(ActionTypes.CLOSE_COMBAT, 1.2);
        valueActions.put(ActionTypes.RANGE_COMBAT, 1.5);
        valueActions.put(ActionTypes.AREA_DAMAGE, 3.3);
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
                result += 2000.;
            }
            if (enemyGeneral.isAlive()) {
                result -= 2000.;
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
        if (board.getStatus() == GameStatus.NO_WINNERS) {
            return 0;
        }
        return Double.MIN_VALUE;
    }

    private double getModify(final Unit unit, final boolean isGeneral) throws UnitException {
        final double result = (isGeneral)? 1.4: 1.;
        return 1. * unit.getPower() / 10
                * result
                * valueActions.get(unit.getActionType());
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        double result = 0;

        try {
            final Fields enemyField = (field == Fields.PLAYER_ONE)? Fields.PLAYER_TWO: Fields.PLAYER_ONE;
            final Unit[][] army = board.getArmy(field);
            final Unit[][] enemiesArmy = board.getArmy(enemyField);

            final Position general = board.getGeneralPosition(field);
            final Position enemyGeneral = board.getGeneralPosition(enemyField);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    final boolean isEnemyGeneral = enemyGeneral.X() == i && enemyGeneral.Y() == j;
                    if (enemiesArmy[i][j].isAlive()) {
                        result -= HP_PRIORITY * Math.pow(HP_RATE, 1. + getModify(enemiesArmy[i][j], isEnemyGeneral) * Math.pow
                                (1. * enemiesArmy[i][j].getCurrentHP() / enemiesArmy[i][j].getMaxHP(), DEGREE_PRIORITY));
                    }
                    else {
                        result += IS_DEATH_PRIORITY * getModify(enemiesArmy[i][j], isEnemyGeneral);
                    }
                    final boolean isGeneral = general.X() == i && general.Y() == j;
                    if (army[i][j].isAlive()) {
                        result += HP_PRIORITY * Math.pow(HP_RATE, 1 + getModify(army[i][j], isGeneral) * Math.pow
                                (1. * army[i][j].getCurrentHP() / army[i][j].getMaxHP(), DEGREE_PRIORITY));
                    }
                    else {
                        result -= IS_DEATH_PRIORITY * getModify(army[i][j], isGeneral);
                    }
                }
            }

        } catch (UnitException ignore) {}

        result += checkGameEnding(board, field);

        return result;
    }
}
