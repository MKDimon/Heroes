package heroes.player.botnikita;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.*;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.Visualisable;
import heroes.player.*;
import heroes.player.botnikita.simulation.BoardSimulation;
import heroes.player.botnikita.simulation.FieldsWrapper;
import heroes.player.botnikita.utilityfunction.HealerUtilityFunction;
import heroes.player.botnikita.utilityfunction.IMinMax;
import heroes.player.botnikita.utilityfunction.IUtilityFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class NikitaBot extends BaseBot implements Visualisable {
    private static int getRecLevel(final Board board) {
        return 3;
    }

    private final IUtilityFunction utilityFunction = new HealerUtilityFunction();

    private static final Logger logger = LoggerFactory.getLogger(NikitaBot.class);

    public static class NikitaBotFactory extends BaseBotFactory {
        @Override
        public NikitaBot createBot(final Fields fields) throws GameLogicException {
            return new NikitaBot(fields);
        }
    }

    public NikitaBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new TestBot(super.getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            logger.error("Cannot create army from RandomBot in NikitaBot.getArmy()", e);
        }
        return null;
    }

    private double computeWin(final GameStatus gs) {
        final GameStatus currentPlayer =
                super.getField() == Fields.PLAYER_ONE ? GameStatus.PLAYER_ONE_WINS : GameStatus.PLAYER_TWO_WINS;
        final GameStatus oppPlayer =
                super.getField() == Fields.PLAYER_ONE ? GameStatus.PLAYER_TWO_WINS : GameStatus.PLAYER_ONE_WINS;
        if (gs == currentPlayer) {
            return 100000.0;
        } else if (gs == oppPlayer) {
            return -100000.0;
        } else {
            return 0.0;
        }
    }

    private AnswerWinHolder getGreedyDecision(final List<AnswerWinHolder> answerList, final IMinMax winCalculator) {
        AnswerWinHolder bestAW = answerList.get(0);
        double bestWin = winCalculator.method(bestAW);
        for (int i = 1; i < answerList.size(); i++) {
            final AnswerWinHolder currentAW = answerList.get(i);
            final double currentWin = winCalculator.method(currentAW);
            if (currentWin > bestWin) {
                bestAW = currentAW;
                bestWin = currentWin;
            }
        }
        return bestAW;
    }

    private double getWinByGameTree(final Board board, final Answer answer, final int currentRecLevel,
                                    double alpha, double beta) throws GameLogicException {
        IMinMax minmax;
        final boolean isMax;
        final Fields playerField;
        if (board.getCurrentPlayer() == super.getField()) {
            minmax = AnswerWinHolder::getWin;
            isMax = true;
            playerField = super.getField();
        } else {
            minmax = AnswerWinHolder -> -AnswerWinHolder.getWin();
            isMax = false;
            playerField = FieldsWrapper.getOppField(super.getField());
        }

        if (board.getStatus() != GameStatus.GAME_PROCESS) {
            return computeWin(board.getStatus());
        }

        if (currentRecLevel == getRecLevel(board)) {
            return utilityFunction.evaluate(board, answer);
        }

        final List<AnswerWinHolder> answerList = new LinkedList<>();

        try {
            final GameLogic gl = new GameLogic(board);

            List<Answer> answers = gl.getAvailableMoves(playerField);

            for (Answer ans : answers) {
                if (isMax) {
                    double bestValue = Double.MIN_VALUE;
                    final Board simBoard = BoardSimulation.simulateTurn(board, ans);
                    final double win = getWinByGameTree(simBoard, ans, currentRecLevel + 1, alpha, beta);
                    bestValue = Math.max(bestValue, win);
                    if (bestValue >= beta) {
                        return bestValue;
                    }
                    alpha = Math.max(alpha, bestValue);
                    answerList.add(new AnswerWinHolder(ans, bestValue));
                } else {
                    double bestValue = Double.MAX_VALUE;
                    final Board simBoard = BoardSimulation.simulateTurn(board, ans);
                    final double win = getWinByGameTree(simBoard, ans, currentRecLevel + 1, alpha, beta);
                    bestValue = Math.min(bestValue, win);
                    if (bestValue <= alpha) {
                        return bestValue;
                    }
                    beta = Math.min(beta, bestValue);
                    answerList.add(new AnswerWinHolder(ans, bestValue));
                }
            }
        } catch (BoardException | UnitException e) {
            e.printStackTrace();
        }

        return getGreedyDecision(answerList, minmax).getWin();
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        final long start = System.currentTimeMillis();
        final int currentRecLevel = 0;
        final Fields playerField;
        if (board.getCurrentPlayer() == super.getField()) {
            playerField = super.getField();
        } else {
            playerField = FieldsWrapper.getOppField(super.getField());
        }

        final LinkedList<AnswerWinHolder> answerList = new LinkedList<>();

        try {
            final GameLogic gl = new GameLogic(board);

            final List<Answer> answers = gl.getAvailableMoves(playerField);

            for (Answer ans : answers) {
                final Board simBoard = BoardSimulation.simulateTurn(board, ans);
                final double win = getWinByGameTree(simBoard, ans, currentRecLevel + 1,
                        -Double.MAX_VALUE, Double.MIN_VALUE);
                answerList.add(new AnswerWinHolder(ans, win));
            }

        } catch (BoardException | UnitException e) {
            logger.error("Board or unit exception in NikitaBot.", e);
        }

        final Answer answer = getGreedyDecision(answerList, AnswerWinHolder::getWin).getAnswer();

        final long finish = System.currentTimeMillis();

        System.out.println("TIME = " + (finish - start));

        return answer;
    }

    @Override
    public Fields getField() {
        return super.getField();
    }
}
