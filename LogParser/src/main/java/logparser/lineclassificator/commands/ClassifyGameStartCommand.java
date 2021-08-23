package logparser.lineclassificator.commands;

import logparser.lineclassificator.StringType;
import logparser.lineclassificator.Token;

public class ClassifyGameStartCommand implements ICommand {
    private final StringType st = StringType.GAME_START;
    private Token token;

    @Override
    public boolean isClassified(final String inputString) {
        if (inputString.equals("GAME START")) {
            token = new Token(st, inputString);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Token getToken() {
        return token;
    }
}
