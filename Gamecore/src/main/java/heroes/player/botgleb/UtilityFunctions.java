package heroes.player.botgleb;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gamelogic.GameStatus;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import heroes.player.botgleb.fann.BoardToCSVRecorder;
import heroes.player.botgleb.fann.FANN;
import heroes.units.General;
import heroes.units.Unit;

import java.util.Arrays;
import java.util.Map;

public class UtilityFunctions {

    public static final double MAX_VALUE = 9999999999d;
    public static final double MIN_VALUE = -9999999999d;
    public static final double DRAW_VALUE = -1000000d;

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

    private static double HPOnePlayerUtilityFunction(final Board board, final Fields player) {
        final Unit[][] playerArmy = board.getArmy(player);
        final General playerGen = board.getGeneral(player);
        double result = 0;
        for (final Unit[] units : playerArmy) {
            for (final Unit unit : units) {
                if (unit.isAlive()) {
                    result += unit.getCurrentHP() + ((double) unit.getPower() * (double) unit.getAccuracy() / 100)
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

    public static final UtilityFunction MonteCarloUtilityFunction = (board, player) -> {
        try {
            final Fields enemy = player == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;
            final BaseBot botPlayer = new RandomBot(player);
            final BaseBot botEnemy = new RandomBot(enemy);
            final Map<Fields, BaseBot> getPlayer = Map.of(
                    player, botPlayer,
                    enemy, botEnemy
            );
            final int iterations = 1000;
            int result = 0;
            for (int i = 0; i < iterations; i++) {
                final GameLogic simulationGL = new GameLogic(board);
                while (simulationGL.isGameBegun()) {
                    final Answer answer = getPlayer.get(simulationGL.getBoard().getCurrentPlayer()).
                            getAnswer(simulationGL.getBoard());
                    simulationGL.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
                }
                if (simulationGL.getBoard().getStatus() == GameStatus.PLAYER_ONE_WINS && player == Fields.PLAYER_ONE
                        || simulationGL.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS
                        && player == Fields.PLAYER_TWO) {
                    result ++;
                }
            }
            return (double) result / iterations;
        } catch (final GameLogicException | UnitException | BoardException e) {
            return 0;
        }
    };

    public static final UtilityFunction FANN_UtilityFunction = (board, player) ->
        FANN.getPred(BoardToCSVRecorder.boardToVector(board,player))[0];

}