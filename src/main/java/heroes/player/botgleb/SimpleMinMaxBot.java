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

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class SimpleMinMaxBot extends BaseBot implements Visualisable {

    private static final int maxRecLevel = 4;
    private static final UtilityFunction utilityFunction = UtilityFunctions.simpleUtilityFunction;

    private static final Logger logger = LoggerFactory.getLogger(SimpleMinMaxBot.class);

    protected final TerminalWrapper tw = null;

    /**
     * Фабрика ботов.
     **/

    public static class SimpleMinMaxBotFactory extends BaseBotFactory {
        @Override
        public SimpleMinMaxBot createBot(final Fields fields) throws GameLogicException {
            return new SimpleMinMaxBot(fields);
        }
    }

    private static final class AnswerAndWin implements Comparable<AnswerAndWin> {
        final Answer answer;
        final double win;

        private AnswerAndWin(final Answer answer, final double win){
            this.answer = answer;
            this.win = win;
        }

        @Override
        public int compareTo(final AnswerAndWin o) {
            return Double.compare(win, o.win);
        }
    }

    public SimpleMinMaxBot(final Fields field) throws GameLogicException {
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
            final GameLogic simulationGL = new GameLogic(board);
            final List<Answer> actions = simulationGL.getAvailableMoves(getField());
            final List<AnswerAndWin> awList = new ArrayList<>();
            for(final Answer answer : actions){
                final GameLogic implGL = simulationGL.simulateAction(answer);
                final double win = getWinByGameTree(implGL.getBoard(), 1);
                awList.add(new AnswerAndWin(answer, win));
            }
            return getGreedyDecision(awList, aw -> aw.win).answer;

        } catch (UnitException | BoardException | GameLogicException e) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
    }

    private double getWinByGameTree(final Board implBoard, final int recLevel) throws BoardException, UnitException, GameLogicException {
        final ToDoubleFunction<AnswerAndWin> winCalculator;
        if (implBoard.getCurrentPlayer() == getField()) {
            winCalculator = aw -> aw.win;
        } else {
            winCalculator = aw -> -aw.win;
        }
        if(implBoard.getStatus() != GameStatus.GAME_PROCESS){
            return getTerminalStateValue(implBoard);
        }
        if(recLevel >= maxRecLevel){
            return utilityFunction.compute(implBoard, implBoard.getCurrentPlayer());
        }
        final GameLogic implGL = new GameLogic(implBoard);
        final List<Answer> actions = implGL.getAvailableMoves(implBoard.getCurrentPlayer());
        final List<AnswerAndWin> awList = new ArrayList<>();
        for (final Answer answer : actions){
            final GameLogic simGL = implGL.simulateAction(answer);
            final double win = getWinByGameTree(simGL.getBoard(), recLevel+1);
            awList.add(new AnswerAndWin(answer, win));
        }
        return getGreedyDecision(awList, winCalculator).win;
    }

    private AnswerAndWin getGreedyDecision(final List<AnswerAndWin> awList,
                                                     final ToDoubleFunction<AnswerAndWin> winCalculator){
        AnswerAndWin bestAW = awList.get(0);
        double bestWin = winCalculator.applyAsDouble(bestAW);
        for(int i = 1; i < awList.size(); i++){
            final AnswerAndWin currentAW = awList.get(i);
            final double currentWin = winCalculator.applyAsDouble(currentAW);
            if(currentWin > bestWin){
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }

    private double getTerminalStateValue(final Board board) throws GameLogicException {
        if (board.getStatus() == GameStatus.GAME_PROCESS) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        if (board.getStatus() == GameStatus.NO_WINNERS) {
            return 0d;
        }
        if (board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }

}
