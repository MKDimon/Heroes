package heroes.player.botnikita.decisionalgorythms;

import gamecore.auxiliaryclasses.boardexception.BoardException;
import gamecore.auxiliaryclasses.gamelogicexception.GameLogicException;
import gamecore.auxiliaryclasses.unitexception.UnitException;
import gamecore.gamelogic.Board;
import gamecore.gamelogic.Fields;
import gamecore.gamelogic.GameLogic;
import gamecore.gamelogic.GameStatus;
import gamecore.player.Answer;
import heroes.player.botnikita.AnswerWinHolder;
import heroes.player.botnikita.simulation.BoardSimulation;
import heroes.player.botnikita.simulation.FieldsWrapper;
import heroes.player.botnikita.utilityfunction.IUtilityFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class MiniMaxAlgorythm implements IDecisionAlgorythm {

    private int maxDepth = 0;
    private IUtilityFunction utilityFunction;

    private static final Logger logger = LoggerFactory.getLogger(MiniMaxAlgorythm.class);

    @Override
    public Answer getAnswer(final Board board, final IUtilityFunction utilityFunction, final Fields playerField,
                            final int maxDepth) {
        final int currentRecLevel = 0;
        this.maxDepth = maxDepth;
        this.utilityFunction = utilityFunction;
        final LinkedList<AnswerWinHolder> answerList = new LinkedList<>();

        try {
            final GameLogic gl = new GameLogic(board);

            final List<Answer> answers = gl.getAvailableMoves(playerField);

            for (Answer ans : answers) {
                final Board simBoard = BoardSimulation.simulateTurn(board, ans);
                final double win = getWinByGameTree(simBoard, playerField, ans, currentRecLevel + 1,
                        -Double.MAX_VALUE, Double.MIN_VALUE);
                answerList.add(new AnswerWinHolder(ans, win));
            }

        } catch (BoardException | UnitException | GameLogicException e) {
            logger.error("Board or unit exception in Minimax Algorythm.", e);
        }

        return getGreedyDecision(answerList, true).getAnswer();
    }

    private double getWinByGameTree(final Board board, final Fields playerField, final Answer answer,
                                    final int currentRecLevel, double alpha, double beta) throws GameLogicException {
        final boolean isMax;
        final Fields currentPlayerField;
        if (board.getCurrentPlayer() == playerField) {
            isMax = true;
            currentPlayerField = playerField;
        } else {
            isMax = false;
            currentPlayerField = FieldsWrapper.getOppField(playerField);
        }

        if (board.getStatus() != GameStatus.GAME_PROCESS) {
            return computeWin(board.getStatus(), playerField);
        }

        if (currentRecLevel == maxDepth) {
            return utilityFunction.evaluate(board, answer);
        }

        final List<AnswerWinHolder> answerList = new LinkedList<>();

        try {
            final GameLogic gl = new GameLogic(board);

            List<Answer> answers = gl.getAvailableMoves(currentPlayerField);

            for (Answer ans : answers) {
                if (isMax) {
                    double bestValue = Double.MIN_VALUE;
                    final Board simBoard = BoardSimulation.simulateTurn(board, ans);
                    final double win = getWinByGameTree(simBoard, playerField, ans,
                                            currentRecLevel + 1, alpha, beta);
                    bestValue = Math.max(bestValue, win);
                    if (bestValue >= beta) {
                        return bestValue;
                    }
                    alpha = Math.max(alpha, bestValue);
                    answerList.add(new AnswerWinHolder(ans, bestValue));
                } else {
                    double bestValue = Double.MAX_VALUE;
                    final Board simBoard = BoardSimulation.simulateTurn(board, ans);
                    final double win = getWinByGameTree(simBoard, playerField, ans,
                                            currentRecLevel + 1, alpha, beta);
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

        return getGreedyDecision(answerList, isMax).getWin();
    }

    private double computeWin(final GameStatus gs, final Fields playerField) {
        final GameStatus currentPlayer =
                playerField == Fields.PLAYER_ONE ? GameStatus.PLAYER_ONE_WINS : GameStatus.PLAYER_TWO_WINS;
        final GameStatus oppPlayer =
                playerField == Fields.PLAYER_ONE ? GameStatus.PLAYER_TWO_WINS : GameStatus.PLAYER_ONE_WINS;
        if (gs == currentPlayer) {
            return 100000.0;
        } else if (gs == oppPlayer) {
            return -100000.0;
        } else {
            return 0.0;
        }
    }

    private AnswerWinHolder getGreedyDecision(final List<AnswerWinHolder> answerList, final boolean isMax) {
        AnswerWinHolder bestAW = answerList.get(0);
        for (int i = 1; i < answerList.size(); i++) {
            final AnswerWinHolder currentAW = answerList.get(i);
            if (isMax) {
                bestAW = FindMinAnswerWin.findMax(bestAW, currentAW);
            } else {
                bestAW = FindMinAnswerWin.findMin(bestAW, currentAW);
            }
        }
        return bestAW;
    }
}
