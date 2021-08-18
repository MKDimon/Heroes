package heroes.player.botdimon.simulationfeatures.functions;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.mathutils.Pair;
import heroes.units.General;
import heroes.units.Unit;
import weka.classifiers.AbstractClassifier;
import weka.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static heroes.auxiliaryclasses.ActionTypes.*;


/**
 * Нейронка
 */
public class UtilityAnswerFuncNeuronOnnx implements IUtilityFunc {
    private static final ArrayList<String> attributes = new ArrayList<>();
    private static final Map<ActionTypes, Double> valueActions = new HashMap<>();
    private static final Map<GameStatus, Double> statuses = new HashMap<>();

    static {
        valueActions.put(ActionTypes.HEALING, 2.0);
        valueActions.put(ActionTypes.CLOSE_COMBAT, 1.2);
        valueActions.put(ActionTypes.RANGE_COMBAT, 1.5);
        valueActions.put(ActionTypes.AREA_DAMAGE, 3.3);

        attributes.add("A_00_HP");
        attributes.add("A_00_POWER");
        attributes.add("A_00_TYPE");
        attributes.add("A_00_ACTIVE");
        attributes.add("A_01_HP");
        attributes.add("A_01_POWER");
        attributes.add("A_01_TYPE");
        attributes.add("A_01_ACTIVE");
        attributes.add("A_02_HP");
        attributes.add("A_02_POWER");
        attributes.add("A_02_TYPE");
        attributes.add("A_02_ACTIVE");
        attributes.add("A_10_HP");
        attributes.add("A_10_POWER");
        attributes.add("A_10_TYPE");
        attributes.add("A_10_ACTIVE");
        attributes.add("A_11_HP");
        attributes.add("A_11_POWER");
        attributes.add("A_11_TYPE");
        attributes.add("A_11_ACTIVE");
        attributes.add("A_12_HP");
        attributes.add("A_12_POWER");
        attributes.add("A_12_TYPE");
        attributes.add("A_12_ACTIVE");
        attributes.add("AG_INSIPE_DAMAGE");
        attributes.add("AG_INSIPE_DEFENSE");
        attributes.add("AG_INSIPE_ACCURACY");
        attributes.add("E_00_HP");
        attributes.add("E_00_POWER");
        attributes.add("E_00_TYPE");
        attributes.add("E_00_ACTIVE");
        attributes.add("E_01_HP");
        attributes.add("E_01_POWER");
        attributes.add("E_01_TYPE");
        attributes.add("E_01_ACTIVE");
        attributes.add("E_02_HP");
        attributes.add("E_02_POWER");
        attributes.add("E_02_TYPE");
        attributes.add("E_02_ACTIVE");
        attributes.add("E_10_HP");
        attributes.add("E_10_POWER");
        attributes.add("E_10_TYPE");
        attributes.add("E_10_ACTIVE");
        attributes.add("E_11_HP");
        attributes.add("E_11_POWER");
        attributes.add("E_11_TYPE");
        attributes.add("E_11_ACTIVE");
        attributes.add("E_12_HP");
        attributes.add("E_12_POWER");
        attributes.add("E_12_TYPE");
        attributes.add("E_12_ACTIVE");
        attributes.add("EG_INSIPE_DAMAGE");
        attributes.add("EG_INSIPE_DEFENSE");
        attributes.add("EG_INSIPE_ACCURACY");
        attributes.add("ROUND");
        attributes.add("FIELD");
        attributes.add("GAME_STATUS");
        attributes.add("VALUE");
    }


    @Override
    public double getValue(final Board board, final Fields field) {

        return 0;
    }
}
