package heroes.player.botgleb;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.units.General;
import heroes.units.Unit;

import java.util.Arrays;

public class UtilityFunctions {

    public static final double MAX_VALUE = 9999999999d;
    public static final double MIN_VALUE = -9999999999d;

    /**
     * Простая функция полезности. Получает доску и игрока, для которого вычисляется
     * величина, равная playersHP - enemiesHP
     **/

    public static final UtilityFunction simpleUtilityFunction = (board, player) -> {
        final Fields defField = player == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;

        double playersHealth = Arrays.stream(board.getArmy(player)).mapToInt(line -> Arrays.stream(line).
                mapToInt(Unit::getCurrentHP).sum()).sum();

        double enemiesHealth = Arrays.stream(board.getArmy(defField)).mapToInt(line -> Arrays.stream(line).
                mapToInt(Unit::getCurrentHP).sum()).sum();

        return playersHealth - enemiesHealth;
    };

    public static final UtilityFunction HPUtilityFunction = (board, player) -> {
        final Fields defField = player == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;
        return HPOnePlayerUtilityFunction(board, player) - HPOnePlayerUtilityFunction(board, defField);
    };

    private static final double HPOnePlayerUtilityFunction(final Board board, final Fields player) {
        final Unit[][] playerArmy = board.getArmy(player);
        final General playerGen = board.getGeneral(player);
        double result = 0;
        for (final Unit[] units : playerArmy) {
            for (final Unit unit : units) {
                if (unit.isAlive()) {
                    result += unit.getCurrentHP() + ( (double) unit.getPower() * (double) unit.getAccuracy()/100)
                            - unit.getDefenseArmor();
                    if (unit.getActionType() == ActionTypes.HEALING) {
                        result += 500;
                    }
                    if (unit.getActionType() == ActionTypes.RANGE_COMBAT) {
                        result += 300;
                    }
                } else {
                    result -= 1000;
                }
                if (playerGen.isAlive()) {
                    result += 1000;
                }
            }
        }
        return result;
    }
}
