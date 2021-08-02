package heroes.player.botnikita.utilityfunction;

import heroes.gamelogic.Board;
import heroes.player.Answer;
import heroes.player.botnikita.simulation.BoardSimulation;

public class HealthUtilityFunction implements IUtilityFunction {
    @Override
    public double evaluate(final Board actualBoard, final Answer answer) {
        double hpSumOfFirstArmy = 0;
        double hpSumOfFirstArmySim = 0;
        double hpSumOfSecondArmy = 0;
        double hpSumOfSecondArmySim = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                hpSumOfFirstArmy += actualBoard.getFieldPlayerOne()[i][j].getCurrentHP();
                hpSumOfSecondArmy += actualBoard.getFieldPlayerOne()[i][j].getCurrentHP();
            }
        }

        final Board simBoard = BoardSimulation.simulateTurn(actualBoard, answer);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                hpSumOfFirstArmySim += simBoard.getFieldPlayerOne()[i][j].getCurrentHP();
                hpSumOfSecondArmySim += simBoard.getFieldPlayerOne()[i][j].getCurrentHP();
            }
        }

        if (hpSumOfFirstArmy > hpSumOfFirstArmySim || hpSumOfSecondArmy < hpSumOfSecondArmySim) {
            return 1.0;
        } else if (hpSumOfFirstArmy == hpSumOfFirstArmySim) {
            return 1.0;
        } else {
            return -1.0;
        }
    }
}
