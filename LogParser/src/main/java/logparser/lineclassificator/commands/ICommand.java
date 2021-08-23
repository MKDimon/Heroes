package logparser.lineclassificator.commands;

import logparser.lineclassificator.Token;

public interface ICommand {
    boolean isClassified(final String inputString);
    Token getToken();
}
