package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;
import heroes.player.botdimon.simulationfeatures.functions.UtilityAnswerFuncFourV2;
import heroes.player.botdimon.simulationfeatures.functions.UtilityAnswerFuncNeuron;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Абстрактное дерево симуляции ответов
 *
 * Выдает все возможные ходы
 * Может менять урон юнитов на мат ожидание урона
 *
 */
public abstract class SimulationTree {
    protected final Logger logger = LoggerFactory.getLogger(SimulationTree.class);

    protected final IUtilityFunc func;
    private static final IUtilityFunc nodesFunc = new UtilityAnswerFuncNeuron();
    private static final int CLUSTERS = 10;
    protected final Fields field;
    protected final int maxHeight;
    protected final boolean clustering;
    private static final ArrayList<Attribute> attributes = new ArrayList<>();
    static {
        attributes.add(new Attribute("value"));
        attributes.add(new Attribute("A_00"));
        attributes.add(new Attribute("A_01"));
        attributes.add(new Attribute("A_02"));
        attributes.add(new Attribute("A_10"));
        attributes.add(new Attribute("A_11"));
        attributes.add(new Attribute("A_12"));
        attributes.add(new Attribute("A_ALL"));
        attributes.add(new Attribute("A_ALIVE"));
        attributes.add(new Attribute("E_00"));
        attributes.add(new Attribute("E_01"));
        attributes.add(new Attribute("E_02"));
        attributes.add(new Attribute("E_10"));
        attributes.add(new Attribute("E_11"));
        attributes.add(new Attribute("E_12"));
        attributes.add(new Attribute("E_ALL"));
        attributes.add(new Attribute("E_ALIVE"));
    }

    protected class Node {
        public final List<Node> list = new ArrayList<>();
        public final Board board;
        public final Answer answer;
        public double value;

        public Node(final Board board, final Answer answer, final double value) {
            this.board = board;
            this.answer = answer;
            this.value = value;
        }

        public Node(final Node node) throws UnitException, BoardException {
            this.list.addAll(node.list);
            this.board = new Board(node.board);
            this.answer = new Answer(node.answer);
            this.value = node.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Double.compare(node.value, value) == 0 && Objects.equals(list, node.list) && Objects.equals(board, node.board) && Objects.equals(answer, node.answer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(list, board, answer, value);
        }
    }

    public SimulationTree(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        this.func = func;
        this.field = field;
        this.maxHeight = maxHeight;
        this.clustering = clustering;
    }

    private boolean setAction(final Board board, final Answer answer, final List<Node> result)
            throws UnitException, BoardException {
        boolean returns = false;
        final GameLogic gameLogic = new GameLogic(board);
        if (gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType())) {
            final Board newBoard = gameLogic.getBoard();
            result.add(new Node(newBoard, answer, nodesFunc.getValue(board, field)));
            if (answer.getActionType() == ActionTypes.AREA_DAMAGE) { returns = true; }
        }
        return returns;
    }

    /**
     * Возвращает датасет списка узлов
     * @param list - список узлов
     * @return датасет из узлов
     */
    private Instances getDataSet(final List<Node> list) {
        final Instances dataset = new Instances("Nodes", attributes, 50);
        for (final Node item : list) {
            final Board board = item.board;
            final Unit[][] ally = (field == Fields.PLAYER_ONE) ? board.getFieldPlayerOne() : board.getFieldPlayerTwo();
            final Unit[][] enemy = (field != Fields.PLAYER_ONE) ? board.getFieldPlayerOne() : board.getFieldPlayerTwo();
            final Instance inst = new DenseInstance(attributes.size());
            inst.setValue(attributes.get(0), new UtilityAnswerFuncFourV2().getValue(item.board, field));
            double allyAll = 0, allyMax = 0;
            double enemyAll = 0, enemyMax = 0;
            int countAlly = 0;
            int countEnemy = 0;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    inst.setValue(attributes.get(j * 3 + k + 1),
                            (double) ally[j][k].getCurrentHP() / ally[j][k].getMaxHP() * 10);
                    allyAll += ally[j][k].getCurrentHP();
                    allyMax += ally[j][k].getMaxHP();
                    countAlly += ally[j][k].isAlive()? 1 : 0;
                    inst.setValue(attributes.get(j * 3 + k + 8),
                            (double) enemy[j][k].getCurrentHP() / enemy[j][k].getMaxHP() * 10);
                    enemyAll += enemy[j][k].getCurrentHP();
                    enemyMax += enemy[j][k].getMaxHP();
                    countEnemy += enemy[j][k].isAlive()? 1 : 0;
                }
            }
            inst.setValue(attributes.get(7), allyAll / allyMax);
            inst.setValue(attributes.get(8), countAlly);
            inst.setValue(attributes.get(15), enemyAll / enemyMax);
            inst.setValue(attributes.get(16), countEnemy);
            final Answer answer = item.answer;
            dataset.add(inst);
        }
        return dataset;
    }

    /**
     * Принимает список узлов размера N и возвращает список узлов размера CLUSTER
     *
     * @param list Список узлов
     * @param field поле ходившего игрока
     * @return Список узлов из каждого кластера
     * @throws Exception ошибка
     */
    protected List<Node> clustering(final List<Node> list, final Fields field) throws Exception {
        final Instances dataset = getDataSet(list);

        final SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(CLUSTERS);
        kMeans.buildClusterer(dataset);
        final List<Node> result = new ArrayList<>();
        final Map<Integer, Node> map = new HashMap<>();
        for (int i = 0; i < dataset.size(); i++) {
            int k = kMeans.clusterInstance(dataset.get(i));
            if (map.get(k) == null) {
                map.put(k, list.get(i));
            } else {
                final Node node = (field == this.field)?
                        (Double.compare(list.get(i).value,map.get(k).value) < 0) ? map.get(k) : list.get(i)
                        : (Double.compare(list.get(i).value,map.get(k).value) > 0) ? map.get(k) : list.get(i);
                map.put(k, node);
            }
        }
        for (Map.Entry<Integer, Node> entry: map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    /**
     * Меняет урон юнитов на мат ожидание урона
     * @param board состояние игры
     */
    protected void setUnitAccuracy(final Board board) {
        try {
            for (Unit[] units: board.getFieldPlayerOne()) {
                for (Unit unit: units) {
                    unit.setPower(unit.getPower() * unit.getAccuracy() / 100);
                    unit.setAccuracy(100);
                }
            }
            for (Unit[] units: board.getFieldPlayerTwo()) {
                for (Unit unit: units) {
                    unit.setPower(unit.getPower() * unit.getAccuracy() / 100);
                    unit.setAccuracy(100);
                }
            }
        } catch (UnitException e) {
            logger.error("Error set unit accuracy", e);
        }
    }

    /**
     * Возвращает все возможные варианты ходов из заданного состояния игры
     * ! В вернувшихся узлах ходы уже сделаны !
     *
     * @param root корень (узел, которому собираем потомков)
     * @param field поле игрока, который ходит
     * @return список вершин - потомков
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     * @throws GameLogicException ошибка
     */
    protected List<Node> getAllSteps(final Node root, final Fields field) throws UnitException, BoardException, GameLogicException {
        final List<Node> result = new ArrayList<>();
        final Board board = new Board(root.board);

        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Unit[][] army = board.getArmy(field);
        final Unit[][] enemies = board.getArmy(enemyField);

        final List<Position> posAttack = new ArrayList<>();
        final List<Position> posDefend = new ArrayList<>();
        final List<Position> posHealing = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (army[i][j].isActive())      { posAttack.add(new Position(i, j, field));     }
                if (army[i][j].isAlive() && army[i][j].getCurrentHP() != army[i][j].getMaxHP())
                                                { posHealing.add(new Position(i, j, field));    }
                if (enemies[i][j].isAlive())    { posDefend.add(new Position(i, j, enemyField));}
            }
        }

        for (final Position unit : posAttack) {
            final Unit un = board.getUnitByCoordinate(unit);
            final ActionTypes attackType = un.getActionType();
            final boolean isHealer = attackType.equals(ActionTypes.HEALING);

            for (final Position target : (isHealer) ? posHealing : posDefend) {
                final Answer answer = new Answer(unit, target, attackType);
                if (setAction(board, answer, result)) { break; }
            }

            final Answer defAnswer = new Answer(unit, unit, ActionTypes.DEFENSE);
            setAction(board, defAnswer, result);
        }
        if (clustering && result.size() > CLUSTERS) {
            try {
                result.retainAll(clustering(result, field));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public abstract Answer getAnswer(final Board board);
}
