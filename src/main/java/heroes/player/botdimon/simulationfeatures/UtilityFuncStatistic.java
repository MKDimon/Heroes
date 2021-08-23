package heroes.player.botdimon.simulationfeatures;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.mathutils.Pair;
import heroes.units.General;
import heroes.units.Unit;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UtilityFuncStatistic {
    private static final String path = "src/main/resources/statistics/funcStatistic.arff";

    public static final Map<ActionTypes, Double> valueActions = new HashMap<>();

    static {
        valueActions.put(ActionTypes.HEALING, 2.0);
        valueActions.put(ActionTypes.CLOSE_COMBAT, 1.2);
        valueActions.put(ActionTypes.RANGE_COMBAT, 1.5);
        valueActions.put(ActionTypes.AREA_DAMAGE, 3.3);
    }


    private static void recordBoardInfo(final Board board, final double value, final Fields field) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            StringBuilder info = new StringBuilder();
            info.append(board.getCurNumRound()).append(',');

            double status = 0; // WIN : 1 ; NO WINNERS / GAME_PROCESS : 0 ; DEFEAT : -1
            if (board.getStatus() != GameStatus.GAME_PROCESS) {
                status = (board.getStatus() == GameStatus.PLAYER_ONE_WINS && field == Fields.PLAYER_ONE ||
                        board.getStatus() == GameStatus.PLAYER_TWO_WINS && field == Fields.PLAYER_TWO)?
                        1 : -1;
            }

            info.append(status).append(',');
            info.append(String.format("%.2f",value).replace(',','.')).append('\n');

            writer.write(info.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recordUnit(final Unit unit) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            StringBuilder info = new StringBuilder();
            info.append((double) unit.getCurrentHP() / unit.getMaxHP()).append(',');
            info.append(unit.getPower()).append(',');
            info.append(valueActions.get(unit.getActionType())).append(',');
            info.append(unit.isActive()).append(',');
            info.append(unit.isAlive()).append(',');

            writer.write(info.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recordArmy(final Unit[][] units, final General general) {
        for (Unit[] unit : units) {
            for (Unit value : unit) {
                recordUnit(value);
            }
        }
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            StringBuilder info = new StringBuilder();
            info.append((double) general.getCurrentHP() / general.getMaxHP()).append(',');
            info.append(valueActions.get(general.getActionType())).append(',');
            info.append(general.isAlive()).append(',');

            writer.write(info.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void record(final Board board, final double value, final Fields field) {
        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Map<Fields, Pair<Unit[][], General>> getArmy = new HashMap<>();
        getArmy.put(field, new Pair<>(board.getArmy(field), board.getGeneral(field)));
        getArmy.put(enemyField, new Pair<>(board.getArmy(enemyField), board.getGeneral(enemyField)));

        recordArmy(getArmy.get(field).getX(), getArmy.get(field).getY());
        recordArmy(getArmy.get(enemyField).getX(), getArmy.get(enemyField).getY());

        recordBoardInfo(board, value, field);
    }

    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource("funcStatistic.arff");
        Instances dataset = source.getDataSet();
        //set class index to the last attribute
        dataset.setClassIndex(dataset.numAttributes()-1);

        //build model
        MultilayerPerceptron lr = new MultilayerPerceptron();
        lr.setOptions(args);
        lr.buildClassifier(dataset);
        SerializationHelper.write("utilitymodel.model", lr);
        //output model
        System.out.println(lr);
    }
}
