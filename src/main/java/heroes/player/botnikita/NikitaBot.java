package heroes.player.botnikita;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.*;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.PlayerBot;
import heroes.player.RandomBot;
import heroes.player.botnikita.decisionalgorythms.IDecisionAlgorythm;
import heroes.player.botnikita.decisionalgorythms.MiniMaxAlgorythm;
import heroes.player.botnikita.simulation.BoardSimulation;
import heroes.player.botnikita.simulation.FieldsWrapper;
import heroes.player.botnikita.utilityfunction.HealthUtilityFunction;
import heroes.player.botnikita.utilityfunction.IMinMax;
import heroes.player.botnikita.utilityfunction.IUtilityFunction;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class NikitaBot extends BaseBot implements Visualisable {
    private final int recLevel = 1;

    private final IUtilityFunction utilityFunction = new HealthUtilityFunction();
    private final IDecisionAlgorythm decisionAlgorythm = new MiniMaxAlgorythm();

    private static final Logger logger = LoggerFactory.getLogger(NikitaBot.class);

    public static class NikitaBotFactory extends BaseBotFactory {
        @Override
        public NikitaBot createBot(final Fields fields) throws GameLogicException {
            return new NikitaBot(fields);
        }
    }

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.setTerminal(tw);
    }

    public NikitaBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new RandomBot(super.getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            logger.error("Cannot create army from RandomBot in NikitaBot.getArmy()", e);
        }
        return null;
    }

    private double computeWin(final GameStatus gs) {
        GameStatus currentPlayer =
                super.getField() == Fields.PLAYER_ONE ? GameStatus.PLAYER_ONE_WINS : GameStatus.PLAYER_TWO_WINS;
        GameStatus oppPlayer =
                super.getField() == Fields.PLAYER_ONE ? GameStatus.PLAYER_TWO_WINS : GameStatus.PLAYER_ONE_WINS;
        if (gs == currentPlayer) {
            return 10.0;
        } else if (gs == oppPlayer) {
            return -10.0;
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

    private double getWinByGameTree(final Board board, final Answer answer, final int currentRecLevel) throws GameLogicException {

        // проверка на живых юнитов, на конец игры
        // 1 - если победа, 0 - если ничья, -1 - если поражение.
        IMinMax minmax;
        if (board.getCurrentPlayer() == super.getField()) {
            minmax = AnswerWinHolder::getWin;
        } else {
            minmax = AnswerWinHolder -> -AnswerWinHolder.getWin();
        }

        if (board.getStatus() != GameStatus.GAME_PROCESS) {
            return computeWin(board.getStatus());
        }

        if (currentRecLevel == recLevel) {
            return utilityFunction.evaluate(board, answer);
        }
        final Fields playerField;
        if (board.getCurrentPlayer() == super.getField()) {
            playerField = super.getField();
        } else {
            playerField = FieldsWrapper.getOppField(super.getField());
        }
        final Fields oppField = FieldsWrapper.getOppField(playerField);

        final LinkedList<AnswerWinHolder> answerList = new LinkedList<>();

        final List<PositionUnit> playerArmyActiveUnits =
                BoardSimulation.getActiveUnits(board, playerField);

        for (PositionUnit pair : playerArmyActiveUnits) {
            final List<PositionUnit> oppArmyUnits = (pair.getUnit().getActionType() == ActionTypes.HEALING)
                    ? BoardSimulation.getAliveUnits(board, playerField)
                    : BoardSimulation.getAliveUnits(board, oppField);

            for (PositionUnit oppPair : oppArmyUnits) {
                ActionTypes actionType;
                for (int k = 0; k < 2; k++) {
                    actionType = (k == 0) ? pair.getUnit().getActionType() : ActionTypes.DEFENSE;
                    final Answer newAnswer = new Answer(pair.getPosition(),
                            oppPair.getPosition(), actionType);
                    try {
                        GameLogic gl = new GameLogic(new Board(board));
                        if (gl.action(newAnswer.getAttacker(), newAnswer.getDefender(), newAnswer.getActionType())) {
                            final Board simBoard = BoardSimulation.simulateTurn(board, newAnswer);
                            final double win = getWinByGameTree(simBoard, newAnswer, currentRecLevel + 1);
                            answerList.add(new AnswerWinHolder(newAnswer, win));
                        }
                    } catch (UnitException | BoardException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return getGreedyDecision(answerList, minmax).getWin();
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        final int currentRecLevel = 0;
        final Fields playerField;
        if (board.getCurrentPlayer() == super.getField()) {
            playerField = super.getField();
        } else {
            playerField = FieldsWrapper.getOppField(super.getField());
        }
        final Fields oppField = FieldsWrapper.getOppField(playerField);

        final LinkedList<AnswerWinHolder> answerList = new LinkedList<>();

        final List<PositionUnit> playerArmyActiveUnits =
                            BoardSimulation.getActiveUnits(board, playerField);

        for (PositionUnit pair : playerArmyActiveUnits) {
            final List<PositionUnit> oppArmyUnits = (pair.getUnit().getActionType() == ActionTypes.HEALING)
                                                ? BoardSimulation.getAliveUnits(board, playerField)
                                                : BoardSimulation.getAliveUnits(board, oppField);

            for (PositionUnit oppPair : oppArmyUnits) {
                ActionTypes actionType;
                for (int k = 0; k < 2; k++) {
                    actionType = (k == 0) ? pair.getUnit().getActionType() : ActionTypes.DEFENSE;
                    final Answer newAnswer = new Answer(pair.getPosition(),
                            oppPair.getPosition(), actionType);
                    try {
                        GameLogic gl = new GameLogic(new Board(board));
                        if (gl.action(newAnswer.getAttacker(), newAnswer.getDefender(), newAnswer.getActionType())) {
                            final Board simBoard = BoardSimulation.simulateTurn(board, newAnswer);
                            final double win = getWinByGameTree(simBoard, newAnswer, currentRecLevel + 1);
                            answerList.add(new AnswerWinHolder(newAnswer, win));
                        }
                    } catch (UnitException | BoardException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return getGreedyDecision(answerList, AnswerWinHolder::getWin).getAnswer();
    }

    @Override
    public Fields getField() {
        return super.getField();
    }
}
