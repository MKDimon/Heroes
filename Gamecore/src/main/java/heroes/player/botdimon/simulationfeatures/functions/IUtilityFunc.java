package heroes.player.botdimon.simulationfeatures.functions;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;

public interface IUtilityFunc {
    double getValue(final Board board, final Fields field);
}
