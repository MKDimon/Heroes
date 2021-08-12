package heroes.player.botnikita.utilityfunction;

import heroes.gamelogic.Board;
import heroes.player.Answer;

public interface IUtilityFunction {
    double evaluate(final Board actualBoard, final Answer answer);
}
