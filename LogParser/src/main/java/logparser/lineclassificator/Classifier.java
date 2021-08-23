package logparser.lineclassificator;

import logparser.lineclassificator.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Classifier {
    private final List<ICommand> commandList = new ArrayList<>();
    public Classifier() {
        commandList.add(new ClassifyGameStartCommand());
        commandList.add(new ClassifyGameEndCommand());
        commandList.add(new ClassifyDateCommand());
        commandList.add(new ClassifyArmyCommand());
        commandList.add(new ClassifyResultCommand());
        commandList.add(new ClassifyReplayCommand());
    }
    public Token classify(final String inputString) {
        Token token = new Token(StringType.UNKNOWN, inputString);
        for (ICommand command : commandList) {
            if (command.isClassified(inputString)) {
                token = command.getToken();
                break;
            }
        }
        return token;
    }
}
