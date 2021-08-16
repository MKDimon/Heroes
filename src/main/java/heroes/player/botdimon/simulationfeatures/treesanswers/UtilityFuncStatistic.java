package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.mathutils.Pair;
import heroes.units.General;
import heroes.units.Unit;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UtilityFuncStatistic {
    private static final String path = "src/main/resources/statistics/funcStatistic.csv";

    private static void recordBoardInfo(final Board board, final double value, final Fields field) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            StringBuilder info = new StringBuilder();
            info.append(board.getCurNumRound()).append(',');
            info.append(field.name()).append(',');
            info.append(board.getStatus()).append(',');
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
            info.append(unit.getActionType().name()).append(',');
            info.append(unit.isActive()).append(',');

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
            info.append(general.inspirationDamageBonus).append(',');
            info.append(general.inspirationArmorBonus).append(',');
            info.append(general.inspirationAccuracyBonus).append(',');
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

    public static void main(String[] args) throws IOException {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(path));
        Instances data = loader.getDataSet();//get instances object
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);//set the dataset we want to convert
        //and save as ARFF
        saver.setFile(new File("src/main/resources/statistics/funcStatistic.arff"));
        saver.writeBatch();
    }
}
