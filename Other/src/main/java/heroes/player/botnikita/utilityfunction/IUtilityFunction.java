package heroes.player.botnikita.utilityfunction;

import gamecore.gamelogic.Board;
import gamecore.player.Answer;

public interface IUtilityFunction {
    double evaluate(final Board actualBoard, final Answer answer);
}
