package heroes.player.botgleb;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.units.Unit;

import java.util.Arrays;

public class UtilityFunctions {

    /**
     * Простая функция полезности. Получает доску и игрока, для которого вычисляется
     * величина, равная playersHP * armor - enemiesHP * armor
     **/

    public static final UtilityFunction simpleUtilityFunction = (board, player) -> {
        final Fields defField = player == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;

        double playersHealth = Arrays.stream(board.getArmy(player)).mapToInt(line -> Arrays.stream(line).
                mapToInt(Unit::getCurrentHP).sum()).sum();

        double playersAverageArmor = ((double)Arrays.stream(board.getArmy(player)).
                mapToInt(line -> Arrays.stream(line).
                filter(unit -> unit.isAlive()).mapToInt(Unit::getArmor).sum()).sum() /
                Board.activeCount(board.getArmy(defField)));

        if(playersHealth == 0){
            return -10000;
        }

        double enemiesHealth = Arrays.stream(board.getArmy(defField)).mapToInt(line -> Arrays.stream(line).
                mapToInt(Unit::getCurrentHP).sum()).sum();

        double enemiesAverageArmor = ((double)Arrays.stream(board.getArmy(defField)).
                mapToInt(line -> Arrays.stream(line).
                filter(unit -> unit.isAlive()).mapToInt(Unit::getArmor).sum()).sum() /
                Board.activeCount(board.getArmy(defField)));
        if(enemiesHealth == 0){
            return 10000;
        }

        return playersAverageArmor/100 * playersHealth - enemiesAverageArmor/100 * enemiesHealth;
    };
}
