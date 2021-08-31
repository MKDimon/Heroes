package logparser.lineclassificator.commands;

import logparser.lineclassificator.Token;

public interface IClassifyCommand {
    boolean isClassified(final String inputString);
    Token getToken();
}
