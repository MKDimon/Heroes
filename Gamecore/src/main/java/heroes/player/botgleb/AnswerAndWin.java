package heroes.player.botgleb;

import heroes.player.Answer;

public class AnswerAndWin {
    private final Answer answer;
    private final double win;

    public AnswerAndWin(final Answer answer, final double win) {
        this.answer = answer;
        this.win = win;
    }

    public Answer answer() {
        return answer;
    }

    public double win() {
        return win;
    }
}
