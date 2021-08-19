package heroes.player.botdimon.simulationfeatures.functions;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.mathutils.Pair;
import heroes.player.botdimon.simulationfeatures.UtilityFuncStatistic;
import heroes.units.General;
import heroes.units.Unit;
import ai.onnxruntime.OrtSession.Result;

import java.util.*;
import java.util.function.BiFunction;


/**
 * Нейронка
 */
public class UtilityAnswerFuncNeuronOnnx implements IUtilityFunc {
    private static final OrtEnvironment env = OrtEnvironment.getEnvironment();
    private static OrtSession session;

    static {
        try {
            session = env.createSession(
                    "src/main/java/heroes/player/botdimon/simulationfeatures/functions/model.onnx",
                    new OrtSession.SessionOptions());
        } catch (OrtException e) {
            e.printStackTrace();
        }
    }

    private static final ArrayList<String> attributes = new ArrayList<>();

    static {
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
        attributes.add("AG_HP");
        attributes.add("AG_TYPE");
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
        attributes.add("EG_HP");
        attributes.add("EG_TYPE");
        attributes.add("ROUND");
        attributes.add("GAME_STATUS");
    }

    private float[] getValues(final Board board, final Fields field) {
        float[] result = new float[54];
        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Map<Fields, Pair<Unit[][], General>> getArmy = new HashMap<>();
        getArmy.put(field, new Pair<>(board.getArmy(field), board.getGeneral(field)));
        getArmy.put(enemyField, new Pair<>(board.getArmy(enemyField), board.getGeneral(enemyField)));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j += 4) {
                final Unit unit = getArmy.get(field).getX()[i][j/4];
                result[i * 12 + j] = (float) unit.getCurrentHP() / unit.getMaxHP();
                result[i * 12 + j + 1] = unit.getPower();
                result[i * 12 + j + 2] = UtilityFuncStatistic.valueActions.get(unit.getActionType()).floatValue();
                result[i * 12 + j + 3] = unit.isActive()? 1f : 0f;
                final Unit enemyUnit = getArmy.get(enemyField).getX()[i][j/4];
                result[i * 12 + j + 26] = (float) enemyUnit.getCurrentHP() / enemyUnit.getMaxHP();
                result[i * 12 + j + 27] = enemyUnit.getPower();
                result[i * 12 + j + 28] = UtilityFuncStatistic.valueActions.get(enemyUnit.getActionType()).floatValue();
                result[i * 12 + j + 29] = enemyUnit.isActive()? 1f : 0f;
            }
        }
        final General general = getArmy.get(field).getY();
        result[24] =  (float) general.getCurrentHP() / general.getMaxHP();
        result[25] =  UtilityFuncStatistic.valueActions.get(general.getActionType()).floatValue();

        final General enemyGeneral = getArmy.get(enemyField).getY();
        result[50] =  (float) enemyGeneral.getCurrentHP() / enemyGeneral.getMaxHP();
        result[51] =  UtilityFuncStatistic.valueActions.get(enemyGeneral.getActionType()).floatValue();

        result[52] = board.getCurNumRound();

        float status = 0; // WIN : 1 ; NO WINNERS / GAME_PROCESS : 0 ; DEFEAT : -1
        if (board.getStatus() != GameStatus.GAME_PROCESS) {
            status = (board.getStatus() == GameStatus.PLAYER_ONE_WINS && field == Fields.PLAYER_ONE ||
                    board.getStatus() == GameStatus.PLAYER_TWO_WINS && field == Fields.PLAYER_TWO)?
                    1 : -1;
        }
        result[53] = status;

        return result;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        try {
            Map<String, OnnxTensor> inputMap = new HashMap<>();
            Set<String> requestedOutputs = new HashSet<>();
            float[] values = getValues(board, field);

            BiFunction<Result, String, Float> unwrapFunc =
                    (r, s) -> {
                        try {
                            return ((float[]) r.get(s).get().getValue())[0];
                        } catch (OrtException e) {
                            return Float.NaN;
                        }
                    };
            
            for (int i = 0; i < attributes.size(); i++) {
                inputMap.put(attributes.get(i), OnnxTensor.createTensor(env, new float[] {values[i]}));
            }

            try (Result r = session.run(inputMap, requestedOutputs)) {
                float abVal = unwrapFunc.apply(r, "VALUE");
                System.out.println(abVal);
                return abVal;
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
