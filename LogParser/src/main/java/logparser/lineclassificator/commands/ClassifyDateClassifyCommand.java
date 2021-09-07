package logparser.lineclassificator.commands;

import logparser.lineclassificator.StringType;
import logparser.lineclassificator.Token;

public class ClassifyDateClassifyCommand implements IClassifyCommand {
    private final StringType st = StringType.DATE;
    private Token token;

    @Override
    public boolean isClassified(final String inputString) {
        if (inputString.contains("/")) {
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
