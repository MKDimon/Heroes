package heroes.player.botgleb;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;

@FunctionalInterface
public interface UtilityFunction {
    double compute(final Board board, final Fields player);
}
