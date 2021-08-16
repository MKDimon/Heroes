package heroes.player.botdimon.simulationfeatures.functions;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.mathutils.Pair;
import heroes.units.General;
import heroes.units.Unit;
import weka.classifiers.functions.SMOreg;
import weka.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static heroes.auxiliaryclasses.ActionTypes.*;


/**
 * Нейронка
 */
public class UtilityAnswerFuncNeuron implements IUtilityFunc {
    private static final ArrayList<Attribute> attributes = new ArrayList<>();
    static {
        ArrayList<String> list = new ArrayList<>();
        list.add(AREA_DAMAGE.name()); list.add(CLOSE_COMBAT.name()); list.add(HEALING.name());
        list.add(RANGE_COMBAT.name()); list.add(DEFENSE.name());
        ArrayList<String> listBool = new ArrayList<>();
        listBool.add("true"); listBool.add("false");
        ArrayList<String> listPlayers = new ArrayList<>();
        listPlayers.add("PLAYER_ONE"); listPlayers.add("PLAYER_TWO");
        ArrayList<String> listGameStatuses = new ArrayList<>();
        listGameStatuses.add("GAME_PROCESS"); listGameStatuses.add("NO_WINNERS");
        listGameStatuses.add("PLAYER_ONE_WINS"); listGameStatuses.add("PLAYER_TWO_WINS");
        attributes.add(new Attribute("A_00_HP"));
        attributes.add(new Attribute("A_00_POWER"));
        attributes.add(new Attribute("A_00_TYPE", list));
        attributes.add(new Attribute("A_00_ACTIVE", listBool));
        attributes.add(new Attribute("A_01_HP"));
        attributes.add(new Attribute("A_01_POWER"));
        attributes.add(new Attribute("A_01_TYPE", list));
        attributes.add(new Attribute("A_01_ACTIVE", listBool));
        attributes.add(new Attribute("A_02_HP"));
        attributes.add(new Attribute("A_02_POWER"));
        attributes.add(new Attribute("A_02_TYPE", list));
        attributes.add(new Attribute("A_02_ACTIVE", listBool));
        attributes.add(new Attribute("A_10_HP"));
        attributes.add(new Attribute("A_10_POWER"));
        attributes.add(new Attribute("A_10_TYPE", list));
        attributes.add(new Attribute("A_10_ACTIVE", listBool));
        attributes.add(new Attribute("A_11_HP"));
        attributes.add(new Attribute("A_11_POWER"));
        attributes.add(new Attribute("A_11_TYPE", list));
        attributes.add(new Attribute("A_11_ACTIVE", listBool));
        attributes.add(new Attribute("A_12_HP"));
        attributes.add(new Attribute("A_12_POWER"));
        attributes.add(new Attribute("A_12_TYPE", list));
        attributes.add(new Attribute("A_12_ACTIVE", listBool));
        attributes.add(new Attribute("AG_INSIPE_DAMAGE"));
        attributes.add(new Attribute("AG_INSIPE_DEFENSE"));
        attributes.add(new Attribute("AG_INSIPE_ACCURACY"));
        attributes.add(new Attribute("E_00_HP"));
        attributes.add(new Attribute("E_00_POWER"));
        attributes.add(new Attribute("E_00_TYPE", list));
        attributes.add(new Attribute("E_00_ACTIVE", listBool));
        attributes.add(new Attribute("E_01_HP"));
        attributes.add(new Attribute("E_01_POWER"));
        attributes.add(new Attribute("E_01_TYPE", list));
        attributes.add(new Attribute("E_01_ACTIVE", listBool));
        attributes.add(new Attribute("E_02_HP"));
        attributes.add(new Attribute("E_02_POWER"));
        attributes.add(new Attribute("E_02_TYPE", list));
        attributes.add(new Attribute("E_02_ACTIVE", listBool));
        attributes.add(new Attribute("E_10_HP"));
        attributes.add(new Attribute("E_10_POWER"));
        attributes.add(new Attribute("E_10_TYPE", list));
        attributes.add(new Attribute("E_10_ACTIVE", listBool));
        attributes.add(new Attribute("E_11_HP"));
        attributes.add(new Attribute("E_11_POWER"));
        attributes.add(new Attribute("E_11_TYPE", list));
        attributes.add(new Attribute("E_11_ACTIVE", listBool));
        attributes.add(new Attribute("E_12_HP"));
        attributes.add(new Attribute("E_12_POWER"));
        attributes.add(new Attribute("E_12_TYPE", list));
        attributes.add(new Attribute("E_12_ACTIVE", listBool));
        attributes.add(new Attribute("EG_INSIPE_DAMAGE"));
        attributes.add(new Attribute("EG_INSIPE_DEFENSE"));
        attributes.add(new Attribute("EG_INSIPE_ACCURACY"));
        attributes.add(new Attribute("ROUND"));
        attributes.add(new Attribute("FIELD", listPlayers));
        attributes.add(new Attribute("GAME_STATUS", listGameStatuses));
        attributes.add(new Attribute("VALUE", listGameStatuses));
    }

    private Instances b2i(final Board board, final Fields field) {
        Instances data = new Instances("NAME", attributes, 60);
        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Map<Fields, Pair<Unit[][], General>> getArmy = new HashMap<>();
        getArmy.put(field, new Pair<>(board.getArmy(field), board.getGeneral(field)));
        getArmy.put(enemyField, new Pair<>(board.getArmy(enemyField), board.getGeneral(enemyField)));

        Instance inst = new DenseInstance(58);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 12; j += 4) {
                final Unit unit = getArmy.get(field).getX()[i][j/4];
                inst.setValue(attributes.get(i * 12 + j), (double) unit.getCurrentHP() / unit.getMaxHP());
                inst.setValue(attributes.get(i * 12 + j + 1), unit.getPower());
                inst.setValue(attributes.get(i * 12 + j + 2), unit.getActionType().name());
                inst.setValue(attributes.get(i * 12 + j + 3), unit.isActive()? "true" : "false");
                final Unit enemyUnit = getArmy.get(enemyField).getX()[i][j/4];
                inst.setValue(attributes.get(i * 12 + j + 27), (double) enemyUnit.getCurrentHP() / enemyUnit.getMaxHP());
                inst.setValue(attributes.get(i * 12 + j + 28), enemyUnit.getPower());
                inst.setValue(attributes.get(i * 12 + j + 29), enemyUnit.getActionType().name());
                inst.setValue(attributes.get(i * 12 + j + 30), enemyUnit.isActive()? "true" : "false");
            }
        }
        final General general = getArmy.get(field).getY();
        inst.setValue(attributes.get(24), general.inspirationDamageBonus);
        inst.setValue(attributes.get(25), general.inspirationArmorBonus);
        inst.setValue(attributes.get(26), general.inspirationAccuracyBonus);

        final General enemyGeneral = getArmy.get(enemyField).getY();
        inst.setValue(attributes.get(51), enemyGeneral.inspirationDamageBonus);
        inst.setValue(attributes.get(52), enemyGeneral.inspirationArmorBonus);
        inst.setValue(attributes.get(53), enemyGeneral.inspirationAccuracyBonus);

        inst.setValue(attributes.get(54), board.getCurNumRound());
        inst.setValue(attributes.get(55), field.name());
        inst.setValue(attributes.get(56), board.getStatus().name());
        inst.setValue(attributes.get(57), board.getStatus().name());
        data.add(inst);
        return data;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        try {
            SMOreg smo = (SMOreg) SerializationHelper.read("src/main/java/heroes/player/botdimon/simulationfeatures/functions/utilitymodel.model");

            double result = smo.classifyInstance(b2i(board, field).get(0));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
