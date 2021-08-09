package heroes.player.botnikita.utilityfunction;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.player.botnikita.PositionUnit;
import heroes.player.botnikita.simulation.BoardSimulation;
import heroes.units.Unit;

public class HealerUtilityFunction implements IUtilityFunction {
    @Override
    public double evaluate(final Board actualBoard, final Answer answer) {
        int evaluation = 1;
        PositionUnit minimalHPUnitMax = new PositionUnit(new Position(0,0, Fields.PLAYER_ONE),
                actualBoard.getUnitByCoordinate(new Position(0,0, Fields.PLAYER_ONE)));
        PositionUnit minimalHPUnitMin = new PositionUnit(new Position(0,0, Fields.PLAYER_TWO),
                actualBoard.getUnitByCoordinate(new Position(0,0, Fields.PLAYER_TWO)));;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Unit unit = actualBoard.getUnitByCoordinate(new Position(i, j, Fields.PLAYER_ONE));
                if (!unit.isAlive()) {
                    evaluation -= 1000;
                }

                if (unit.getCurrentHP() < minimalHPUnitMax.getUnit().getCurrentHP()) {
                    minimalHPUnitMax = new PositionUnit(new Position(i, j, Fields.PLAYER_ONE), unit);
                }

                unit = actualBoard.getUnitByCoordinate(new Position(i, j, Fields.PLAYER_TWO));
                if (!unit.isAlive()) {
                    evaluation += 2000;
                }

                if (unit.getDefenseArmor() >= 0) {
                    evaluation -= 50000;
                }

                if (unit.getCurrentHP() < minimalHPUnitMin.getUnit().getCurrentHP()) {
                    minimalHPUnitMax = new PositionUnit(new Position(i, j, Fields.PLAYER_TWO), unit);
                }


            }
        }

        final Board simBoard = BoardSimulation.simulateTurn(actualBoard, answer);
        if (!simBoard.getUnitByCoordinate(minimalHPUnitMax.getPosition()).isAlive()) {
            evaluation -= 20000;
        }

        if (!simBoard.getUnitByCoordinate(minimalHPUnitMax.getPosition()).isAlive()) {
            evaluation += 30000;
        }

        if (simBoard.getUnitByCoordinate(minimalHPUnitMax.getPosition()).getCurrentHP() >
                actualBoard.getUnitByCoordinate(minimalHPUnitMax.getPosition()).getCurrentHP()) {
            evaluation *= 3;
        }

        if (simBoard.getUnitByCoordinate(minimalHPUnitMin.getPosition()).getCurrentHP() >
                actualBoard.getUnitByCoordinate(minimalHPUnitMin.getPosition()).getCurrentHP()) {
            evaluation -= 10000;
        }

        return evaluation;
    }
}
