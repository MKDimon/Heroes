package logparser.lineclassificator;

import logparser.lineclassificator.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Classifier {
    private final List<IClassifyCommand> commandList = new ArrayList<>();
    public Classifier() {
        commandList.add(new ClassifyGameStartClassifyCommand());
        commandList.add(new ClassifyGameEndClassifyCommand());
        commandList.add(new ClassifyDateClassifyCommand());
        commandList.add(new ClassifyArmyClassifyCommand());
        commandList.add(new ClassifyResultClassifyCommand());
        commandList.add(new ClassifyReplayClassifyCommand());
    }
    public Token classify(final String inputString) {
        Token token = new Token(StringType.UNKNOWN, inputString);
        for (IClassifyCommand command : commandList) {
            if (command.isClassified(inputString)) {
                token = command.getToken();
                break;
            }
        }
        return token;
    }
}
