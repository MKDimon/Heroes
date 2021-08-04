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
     * величина, равная playersHP + armor/6 - enemiesHP - armor/6
     **/

    public static final UtilityFunction simpleUtilityFunction = (board, player) -> {
        final Fields defField = player == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;

        double playersHealth = Arrays.stream(board.getArmy(player)).mapToInt(line -> Arrays.stream(line).
                mapToInt(Unit::getCurrentHP).sum()).sum();

        double playersAverageArmor = Arrays.stream(board.getArmy(player)).
                mapToInt(line -> Arrays.stream(line).
                        filter(unit -> unit.isAlive()).mapToInt(Unit::getArmor).sum()).sum();

        double enemiesHealth = Arrays.stream(board.getArmy(defField)).mapToInt(line -> Arrays.stream(line).
                mapToInt(Unit::getCurrentHP).sum()).sum();

        double enemiesAverageArmor = Arrays.stream(board.getArmy(defField)).
                mapToInt(line -> Arrays.stream(line).
                        filter(unit -> unit.isAlive()).mapToInt(Unit::getArmor).sum()).sum();

        return playersAverageArmor / 6d + playersHealth - enemiesAverageArmor / 6d - enemiesHealth;
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
                    result += unit.getCurrentHP() + unit.getPower();
                    if (unit.getActionType() == ActionTypes.HEALING) {
                        result += 300;
                    }
                    if (unit.getActionType() == ActionTypes.CLOSE_COMBAT) {
                        result += 100;
                    }
                    if (unit.getActionType() == ActionTypes.RANGE_COMBAT) {
                        result += 200;
                    }
                }
                if (playerGen.isAlive()) {
                    result += 1000;
                }
            }
        }
        return result;
    }
}
