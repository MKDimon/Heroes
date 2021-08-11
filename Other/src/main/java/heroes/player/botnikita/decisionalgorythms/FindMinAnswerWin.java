package heroes.player.botnikita.decisionalgorythms;

import heroes.player.botnikita.AnswerWinHolder;

public class FindMinAnswerWin {
    public static AnswerWinHolder findMin(final AnswerWinHolder bestAW, final AnswerWinHolder currentAW) {
        if (currentAW.getWin() < bestAW.getWin()) {
            return currentAW;
        } else {
            return bestAW;
        }
    }

    public static AnswerWinHolder findMax(final AnswerWinHolder bestAW, final AnswerWinHolder currentAW) {
        if (currentAW.getWin() > bestAW.getWin()) {
            return currentAW;
        } else {
            return bestAW;
        }
    }
}
