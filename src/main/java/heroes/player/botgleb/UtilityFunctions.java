package heroes.player.botgleb;

import heroes.gamelogic.Fields;
import heroes.units.Unit;

import java.util.Arrays;

public class UtilityFunctions {

    public static final double MAX_VALUE = 999999d;
    public static final double MIN_VALUE = -999999d;

    /**
     * Простая функция полезности. Получает доску и игрока, для которого вычисляется
     * величина, равная playersHP * armor - enemiesHP * armor
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

        return playersAverageArmor/6d + playersHealth - enemiesAverageArmor /6d - enemiesHealth;
    };
}
