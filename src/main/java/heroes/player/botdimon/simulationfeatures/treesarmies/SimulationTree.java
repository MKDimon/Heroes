package heroes.player.botdimon.simulationfeatures.treesarmies;

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
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
    protected final Fields field;
    protected final int maxHeight;

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
    }

    public SimulationTree(final IUtilityFunc func, final Fields field, final int maxHeight) {
        this.func = func;
        this.field = field;
        this.maxHeight = maxHeight;
    }

    private boolean setAction(final GameLogic gameLogic, final Answer answer)
            throws UnitException {

        return gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
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
     *
     * @param root корень (узел, которому собираем потомков)
     * @param field поле игрока, который ходит
     * @return список вершин - потомков
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     * @throws GameLogicException ошибка
     */
    protected List<Node> getAllSteps(final Node root, final Fields field) throws UnitException, BoardException, GameLogicException {
        List<Node> result = new ArrayList<>();
        final Board board = new Board(root.board);

        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        final Unit[][] army = board.getArmy(field);
        final Unit[][] enemies = board.getArmy(enemyField);

        List<Position> posAttack = new ArrayList<>();
        List<Position> posDefend = new ArrayList<>();
        List<Position> posHealing = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (army[i][j].isActive()) {
                    posAttack.add(new Position(i, j, field));
                }
                if (army[i][j].isAlive()) {
                    posHealing.add(new Position(i, j, field));
                }
                if (enemies[i][j].isAlive()) {
                    posDefend.add(new Position(i, j, enemyField));
                }
            }
        }

        for (final Position unit : posAttack) {
            final Unit un = board.getUnitByCoordinate(unit);
            final ActionTypes attackType = un.getActionType();
            final boolean isHealer = attackType.equals(ActionTypes.HEALING);

            for (final Position target : (isHealer) ? posHealing : posDefend) {
                final GameLogic gameLogic = new GameLogic(board);
                final Answer answer = new Answer(unit, target, attackType);
                if (setAction(gameLogic, answer)) {
                    final Board newBoard = gameLogic.getBoard();
                    result.add(new Node(newBoard, answer, func.getValue(newBoard, field)));
                }
            }

            final GameLogic defGL = new GameLogic(board);
            final Board defBoard = defGL.getBoard();
            final Answer defAnswer = new Answer(unit, unit, ActionTypes.DEFENSE);
            if (setAction(defGL, defAnswer)) {
                result.add(new Node(defBoard, defAnswer, func.getValue(defBoard, field)));
            }
        }
        return result;
    }

    public abstract Answer getAnswer(final Board board);

}
