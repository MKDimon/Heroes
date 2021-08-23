package logparser.lineclassificator.commands;

import logparser.lineclassificator.StringType;
import logparser.lineclassificator.Token;
import logparser.utils.StringUtils;

public class ClassifyArmyCommand implements ICommand {
    private final StringType st = StringType.ARMY;
    private Token token;

    @Override
    public boolean isClassified(final String inputString) {
        if (StringUtils.calcOccurences(inputString, ",") == 7) {
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
