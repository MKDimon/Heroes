package heroes.player.botdimon.simulationfeatures.functions;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.units.Unit;


/**
 * Первое поколение
 * Прямая
 */
public class UtilityAnswerFuncOne implements IUtilityFunc {
    private double checkGenerals(final Board board, final Fields field) {
        double result = 0;
        if (field == Fields.PLAYER_ONE) {
            if (board.getGeneralPlayerOne().isAlive()) {
                result += 3000;
            }
            if (board.getGeneralPlayerTwo().isAlive()) {
                result -= 3000;
            }
        } else {
            if (board.getGeneralPlayerTwo().isAlive()) {
                result += 3000;
            }
            if (board.getGeneralPlayerOne().isAlive()) {
                result -= 3000;
            }
        }
        return result;
    }

    private double checkGameEnding(final Board board, final Fields field) {
        if (field == Fields.PLAYER_ONE) {
            if (board.getStatus() == GameStatus.PLAYER_ONE_WINS) {
                return 2000;
            }
        }
        if (field == Fields.PLAYER_TWO) {
            if (board.getStatus() == GameStatus.PLAYER_TWO_WINS) {
                return 2000;
            }
        }
        return 0;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        double result = 0;

        final Fields enemyField = (field == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;
        final Unit[][] army = board.getArmy(field);
        final Unit[][] enemiesArmy = board.getArmy(enemyField);

        result += checkGenerals(board, field);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (enemiesArmy[i][j].isAlive()) {
                    result -= enemiesArmy[i][j].getCurrentHP();
                } else {
                    result += 200.;
                }
                if (army[i][j].isAlive()) {
                    result += army[i][j].getCurrentHP();
                } else {
                    result -= 200;
                }
            }
        }

        result += checkGameEnding(board, field);

        return result;
    }
}
