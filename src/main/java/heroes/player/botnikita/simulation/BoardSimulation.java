package heroes.player.botnikita.simulation;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.player.botnikita.PositionUnit;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class BoardSimulation {
    private static final Logger logger = LoggerFactory.getLogger(BoardSimulation.class);
    public static Board simulateTurn(final Board actualBoard, final Answer answer) {
        final GameLogic gl;
        try {
            gl = new GameLogic(actualBoard);
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
            return gl.getBoard();
        } catch (UnitException | BoardException e) {
            logger.error("Cannot create GameLogic object in BoardSimulation", e);
        }
        return actualBoard;
    }

    public static List<PositionUnit> getActiveUnits(final Board board, final Fields field) {
        final List<PositionUnit> positions = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                final Unit unit = board.getUnitByCoordinate(new Position(i, j, field));
                if (unit.isActive() && unit.isAlive()) {
                    final PositionUnit pair = new PositionUnit(new Position(i,j, field), unit);
                    positions.add(pair);
                }
            }
        }
        return positions;
    }
}
