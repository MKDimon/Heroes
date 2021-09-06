package heroes.player.botnikita.decisionalgorythms;

import heroes.gamelogic.Board;
import heroes.player.Answer;
import heroes.player.botnikita.utilityfunction.IUtilityFunction;

public interface IDecisionAlgorythm {
     Answer getAnswer(final Board board, final IUtilityFunction utilityFunction);
}
