package heroes.player.botnikita.decisionalgorythms;

import gamecore.gamelogic.Board;
import gamecore.player.Answer;
import heroes.player.botnikita.utilityfunction.IUtilityFunction;

public interface IDecisionAlgorythm {
     Answer getAnswer(final Board board, final IUtilityFunction utilityFunction);
}
