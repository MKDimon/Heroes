package heroes.player.botgleb;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.*;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MinMaxBot extends BaseBot implements Visualisable {

    private static final int maxRecLevel = 1;
    private static final UtilityFunction utilityFunction = UtilityFunctions.simpleUtilityFunction;

    private static final Logger logger = LoggerFactory.getLogger(SimulationBot.class);

    protected final TerminalWrapper tw = null;

    /**
     * Фабрика ботов.
     **/

    public static class MinMaxBotFactory extends BaseBotFactory {
        @Override
        public MinMaxBot createBot(final Fields fields) throws GameLogicException {
            return new MinMaxBot(fields);
        }
    }

    private class Decomposition {
        private final Map<Answer, Double> answerToWinMap;

        private Decomposition(final Board board) throws BoardException, UnitException, GameLogicException {
            answerToWinMap = makeDecomposition(board);
        }

        private void addToRootDecomposition(final Decomposition decomposition, final Answer rootAnswer) {
            if (rootAnswer.getAttacker().F() != getField()) {
                answerToWinMap.put(rootAnswer, decomposition.answerToWinMap.get(decomposition.findMax()));
            } else {
                answerToWinMap.put(rootAnswer, decomposition.answerToWinMap.get(decomposition.findMin()));
            }
        }

        private Answer findMin() {
            Answer result = null;
            double minUtilityFunctionValue = 100000000d;
            for (final Answer answer : answerToWinMap.keySet()) {
                if (answerToWinMap.get(answer).compareTo(minUtilityFunctionValue) < 0) {
                    result = answer;
                    minUtilityFunctionValue = answerToWinMap.get(answer);
                }
            }
            return result;
        }

        private Answer findMax() {
            Answer result = null;
            double maxUtilityFunctionValue = -100000000d;
            for (final Answer answer : answerToWinMap.keySet()) {
                if (answerToWinMap.get(answer).compareTo(maxUtilityFunctionValue) > 0) {
                    result = answer;
                    maxUtilityFunctionValue = answerToWinMap.get(answer);
                }
            }
            return result;
        }

    }

    public MinMaxBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.tw = tw;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new RandomBot(getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        try {
            return computeTree(board, 0).findMax();
        } catch (final BoardException | UnitException e) {
            logger.error("Error getting answer by MinMaxBot", e);
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    private Decomposition computeTree(final Board board, final int recLevel)
            throws BoardException, UnitException, GameLogicException {
        if (recLevel >= maxRecLevel) {
            return new Decomposition(board);
        }
        final Decomposition rootDecomposition = new Decomposition(board);
        final GameLogic gl = new GameLogic(board);
        for (final Answer answer : rootDecomposition.answerToWinMap.keySet()) {
            final GameLogic implGL = gl.simulateAction(answer);
            if (!implGL.isGameBegun()) {
                rootDecomposition.answerToWinMap.put(answer, getTerminalStateValue(implGL));
            } else {
                rootDecomposition.addToRootDecomposition(computeTree(implGL.getBoard(), recLevel + 1), answer);
            }
        }
        return rootDecomposition;
    }

    private double getTerminalStateValue(final GameLogic gl) throws GameLogicException {
        if (gl.getBoard().getStatus() == GameStatus.GAME_PROCESS) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        if (gl.getBoard().getStatus() == GameStatus.NO_WINNERS) {
            return 0d;
        }
        if (gl.getBoard().getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                gl.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }

    private Map<Answer, Double> makeDecomposition(final Board board) throws BoardException, UnitException, GameLogicException {
        final Map<Answer, Double> result = new HashMap<>();
        final GameLogic gl = new GameLogic(board);
        for (final Answer answer : gl.getAvailableMoves(board.getCurrentPlayer())) {
            final GameLogic currentGL = gl.simulateAction(answer);
            if (!currentGL.isGameBegun()) {
                result.put(answer, getTerminalStateValue(currentGL));
            } else {
                double win = utilityFunction.compute(currentGL.getBoard(), board.getCurrentPlayer());
                if (gl.getBoard().getCurrentPlayer() != getField()) {
                    win = -win;
                }
                result.put(answer, win);
            }
        }
        return result;
    }

}
