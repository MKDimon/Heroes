package logparser.parser.statemachineparser;

import logparser.dto.DataTransferObject;
import logparser.lineclassificator.Token;
import logparser.parser.statemachineparser.smcommands.*;

import java.util.HashMap;
import java.util.Map;

public class ParserCommandFactory {
    private final Map<States, IParseCommand> parseCommandMap;

    public ParserCommandFactory() {
        parseCommandMap = new HashMap<>();
    }

    private void fillMapWithContext(final Token token, final DataTransferObject.DTOBuilder dtoBuilder) {
        parseCommandMap.put(States.armyTokenState, new ParseArmyCommand(token, dtoBuilder));
        parseCommandMap.put(States.dateTokenState, new ParseDateCommand(token, dtoBuilder));
        parseCommandMap.put(States.resultTokenState, new ParseResultCommand(token, dtoBuilder));
        parseCommandMap.put(States.startState, new ParseStartLogCommand(dtoBuilder));
        parseCommandMap.put(States.finishState, new ParseEndLogCommand(dtoBuilder));
        parseCommandMap.put(States.errorState, new ParseEndLogCommand(dtoBuilder));
    }

    public IParseCommand getCommand(final States state, final Token token,
                                    final DataTransferObject.DTOBuilder dtoBuilder) {
        fillMapWithContext(token, dtoBuilder);
        return parseCommandMap.getOrDefault(state, new DoNothingCommand());
    }
}
