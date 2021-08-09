package heroes.player.botgleb;


import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SimulationBot extends BaseBot implements Visualisable {
    private static final Logger logger = LoggerFactory.getLogger(SimulationBot.class);
    private static final UtilityFunction utilityFunction = UtilityFunctions.simpleUtilityFunction;

    protected final TerminalWrapper tw = null;

    /**
     * Фабрика ботов.
     **/

    public static class SimulationBotFactory extends BaseBotFactory {
        @Override
        public SimulationBot createBot(final Fields fields) throws GameLogicException {
            return new SimulationBot(fields);
        }
    }

    public SimulationBot(Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.tw = tw;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new TestBot(getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            logger.error("Error army creating", e);
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        try {
            final Map<Answer, Double> decomposition = new HashMap<>();
            for (final Answer answer : board.getPossibleMoves()) {
                final Board simulationBoard = board.copy(answer);
                if (simulationBoard.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                        simulationBoard.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
                    return answer;
                }
                decomposition.put(answer,
                        utilityFunction.compute(simulationBoard, getField()));

            }
            return getBestAnswer(decomposition);
        } catch (GameLogicException e) {
            logger.error("Error getting answer by simulation bot", e);
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    private Answer getBestAnswer(final Map<Answer, Double> decomposition) {
        Answer result = null;
        double maxUtilityFunctionValue = UtilityFunctions.MIN_VALUE;
        for (final Answer answer : decomposition.keySet()) {
            if (decomposition.get(answer).compareTo(maxUtilityFunctionValue) > 0) {
                result = answer;
                maxUtilityFunctionValue = decomposition.get(answer);
            }
        }
        return result;
    }

}