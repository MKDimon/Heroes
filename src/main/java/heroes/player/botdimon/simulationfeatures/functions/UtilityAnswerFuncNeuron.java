package heroes.player.botdimon.simulationfeatures.functions;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.mathutils.Pair;
import heroes.player.botdimon.simulationfeatures.UtilityFuncStatistic;
import heroes.units.General;
import heroes.units.Unit;
import weka.classifiers.AbstractClassifier;
import weka.core.*;

import java.util.*;

/**
 * Нейронка
 */
public class UtilityAnswerFuncNeuron implements IUtilityFunc {
    private static AbstractClassifier smo;
    private static final ArrayList<Attribute> attributes = new ArrayList<>();
    private static final Map<ActionTypes, Double> valueActions = new HashMap<>();

    static {
        try {
            smo = (AbstractClassifier) SerializationHelper.read
                    ("src/main/java/heroes/player/botdimon/simulationfeatures/functions/utilitymodel.model");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> listBool = new ArrayList<>();
        listBool.add("true"); listBool.add("false");
        attributes.add(new Attribute("A_00_HP"));
        attributes.add(new Attribute("A_00_POWER"));
        attributes.add(new Attribute("A_00_TYPE"));
        attributes.add(new Attribute("A_00_ACTIVE", listBool));
        attributes.add(new Attribute("A_01_HP"));
        attributes.add(new Attribute("A_01_POWER"));
        attributes.add(new Attribute("A_01_TYPE"));
        attributes.add(new Attribute("A_01_ACTIVE", listBool));
        attributes.add(new Attribute("A_02_HP"));
        attributes.add(new Attribute("A_02_POWER"));
        attributes.add(new Attribute("A_02_TYPE"));
        attributes.add(new Attribute("A_02_ACTIVE", listBool));
        attributes.add(new Attribute("A_10_HP"));
        attributes.add(new Attribute("A_10_POWER"));
        attributes.add(new Attribute("A_10_TYPE"));
        attributes.add(new Attribute("A_10_ACTIVE", listBool));
        attributes.add(new Attribute("A_11_HP"));
        attributes.add(new Attribute("A_11_POWER"));
        attributes.add(new Attribute("A_11_TYPE"));
        attributes.add(new Attribute("A_11_ACTIVE", listBool));
        attributes.add(new Attribute("A_12_HP"));
        attributes.add(new Attribute("A_12_POWER"));
        attributes.add(new Attribute("A_12_TYPE"));
        attributes.add(new Attribute("A_12_ACTIVE", listBool));
        attributes.add(new Attribute("AG_HP"));
        attributes.add(new Attribute("AG_TYPE"));
        attributes.add(new Attribute("E_00_HP"));
        attributes.add(new Attribute("E_00_POWER"));
        attributes.add(new Attribute("E_00_TYPE"));
        attributes.add(new Attribute("E_00_ACTIVE", listBool));
        attributes.add(new Attribute("E_01_HP"));
        attributes.add(new Attribute("E_01_POWER"));
        attributes.add(new Attribute("E_01_TYPE"));
        attributes.add(new Attribute("E_01_ACTIVE", listBool));
        attributes.add(new Attribute("E_02_HP"));
        attributes.add(new Attribute("E_02_POWER"));
        attributes.add(new Attribute("E_02_TYPE"));
        attributes.add(new Attribute("E_02_ACTIVE", listBool));
        attributes.add(new Attribute("E_10_HP"));
        attributes.add(new Attribute("E_10_POWER"));
        attributes.add(new Attribute("E_10_TYPE"));
        attributes.add(new Attribute("E_10_ACTIVE", listBool));
        attributes.add(new Attribute("E_11_HP"));
        attributes.add(new Attribute("E_11_POWER"));
        attributes.add(new Attribute("E_11_TYPE"));
        attributes.add(new Attribute("E_11_ACTIVE", listBool));
        attributes.add(new Attribute("E_12_HP"));
        attributes.add(new Attribute("E_12_POWER"));
        attributes.add(new Attribute("E_12_TYPE"));
        attributes.add(new Attribute("E_12_ACTIVE", listBool));
        attributes.add(new Attribute("EG_HP"));
        attributes.add(new Attribute("EG_TYPE"));
        attributes.add(new Attribute("ROUND"));
        attributes.add(new Attribute("GAME_STATUS"));
        attributes.add(new Attribute("VALUE"));
    }

    private Instances b2i(final Board board, final Fields field) {
        Instances data = new Instances("NAME", attributes, 58);
        data.setClassIndex(data.numAttributes()-1);
        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Map<Fields, Pair<Unit[][], General>> getArmy = new HashMap<>();
        getArmy.put(field, new Pair<>(board.getArmy(field), board.getGeneral(field)));
        getArmy.put(enemyField, new Pair<>(board.getArmy(enemyField), board.getGeneral(enemyField)));

        Instance inst = new DenseInstance(55);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j += 4) {
                final Unit unit = getArmy.get(field).getX()[i][j/4];
                inst.setValue(attributes.get(i * 12 + j), (double) unit.getCurrentHP() / unit.getMaxHP());
                inst.setValue(attributes.get(i * 12 + j + 1), unit.getPower());
                inst.setValue(attributes.get(i * 12 + j + 2), UtilityFuncStatistic.valueActions.get(unit.getActionType()));
                inst.setValue(attributes.get(i * 12 + j + 3), unit.isActive()? "false" : "true");
                final Unit enemyUnit = getArmy.get(enemyField).getX()[i][j/4];
                inst.setValue(attributes.get(i * 12 + j + 26), (double) enemyUnit.getCurrentHP() / enemyUnit.getMaxHP());
                inst.setValue(attributes.get(i * 12 + j + 27), enemyUnit.getPower());
                inst.setValue(attributes.get(i * 12 + j + 28), UtilityFuncStatistic.valueActions.get(enemyUnit.getActionType()));
                inst.setValue(attributes.get(i * 12 + j + 29), enemyUnit.isActive()? "false" : "true");
            }
        }
        final General general = getArmy.get(field).getY();
        inst.setValue(attributes.get(24), (double) general.getCurrentHP() / general.getMaxHP());
        inst.setValue(attributes.get(25), UtilityFuncStatistic.valueActions.get(general.getActionType()));

        final General enemyGeneral = getArmy.get(enemyField).getY();
        inst.setValue(attributes.get(50), (double) enemyGeneral.getCurrentHP() / enemyGeneral.getMaxHP());
        inst.setValue(attributes.get(51), UtilityFuncStatistic.valueActions.get(enemyGeneral.getActionType()));

        inst.setValue(attributes.get(52), board.getCurNumRound());
        float status = 0; // WIN : 1 ; NO WINNERS / GAME_PROCESS : 0 ; DEFEAT : -1
        if (board.getStatus() != GameStatus.GAME_PROCESS) {
            status = (board.getStatus() == GameStatus.PLAYER_ONE_WINS && field == Fields.PLAYER_ONE ||
                    board.getStatus() == GameStatus.PLAYER_TWO_WINS && field == Fields.PLAYER_TWO)?
                    1 : -1;
        }
        inst.setValue(attributes.get(53), status);
        inst.setValue(attributes.get(54), 0);
        data.add(inst);
        return data;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        try {
            double result = smo.classifyInstance(b2i(board, field).get(0));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
