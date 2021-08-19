package heroes.player.botgleb.fann;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.statistics.StatisticsCollector;
import heroes.units.General;
import heroes.units.Unit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardToCSVRecorder {

    public static final String trainData = "trainData.csv";

    private static final Map<String, Float> unitToNumMap = Map.of(
            "SWORDSMAN", 1f,
            "HEALER", 2f,
            "BOWMAN", 3f,
            "MAGE", 4f,
            "ARCHMAGE", 5f,
            "COMMANDER", 6f,
            "SNIPER", 7f);

    public static float[] boardToVector(final Board board, final Fields player) {
        final Fields enemy = player == Fields.PLAYER_ONE ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;
        final Unit[][] playersArmy = board.getArmy(player);
        final General playersGen = board.getGeneral(player);
        final Unit[][] enemiesArmy = board.getArmy(enemy);
        final General enemiesGen = board.getGeneral(enemy);
        final List<Float> result = armyToVector(playersArmy, playersGen);
        result.addAll(armyToVector(enemiesArmy, enemiesGen));
        final float[] resultArr = new float[result.size()];
        for (int i = 0; i < resultArr.length; i++) {
            resultArr[i] = result.get(i);
        }
        return resultArr;
    }

    private static List<Float> armyToVector(final Unit[][] army, final General general) {
        final List<Float> result = new ArrayList<>();
        for(final Unit[] units : army) {
            for (final Unit unit : units) {
                if (unit == general) {
                    result.add(unitToNumMap.get(
                            StatisticsCollector.actToGeneralMap.get(unit.getActionType())));
                } else {
                    result.add(unitToNumMap.get(
                            StatisticsCollector.actToUnitMap.get(unit.getActionType())));
                }
                result.add((float) unit.getCurrentHP());
                result.add((float) unit.getArmor());
                result.add((float) unit.getPower());
                result.add((float) unit.getAccuracy());
            }
        }
        return result;
    }

    public static void boardToCSV(final Board board, final Fields player,
                                  final double utilityFunctionValue) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(trainData, true))) {
           final StringBuilder record = new StringBuilder();
            for (final float item : boardToVector(board, player)) {
               record.append(item).append(",");
           }
           writer.write(record.append(utilityFunctionValue).append("\n").toString());
           writer.flush();
        } catch (final IOException e) {

        }
    }
}
