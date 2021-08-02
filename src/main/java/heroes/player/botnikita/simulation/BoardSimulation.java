package heroes.player.botnikita.simulation;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.GameLogic;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardSimulation {
    private static final Logger logger = LoggerFactory.getLogger(BoardSimulation.class);
    public static Board simulateTurn(final Board actualBoard, final Answer answer) {
        GameLogic gl;
        Board simBoard;
        try {
            simBoard = new Board(actualBoard);
            gl = new GameLogic(simBoard);
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            return simBoard;
        } catch (UnitException | BoardException e) {
            logger.error("Cannot create GameLogic object in BoardSimulation", e);
        }
        return actualBoard;
    }
}
