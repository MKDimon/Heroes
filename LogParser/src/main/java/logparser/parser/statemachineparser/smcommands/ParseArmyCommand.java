package logparser.parser.statemachineparser.smcommands;

import logparser.dto.Army;
import logparser.dto.DataTransferObject;
import logparser.lineclassificator.Token;

import java.util.Arrays;
import java.util.List;

public class ParseArmyCommand implements IParseCommand {

    final Token context;
    final DataTransferObject.DTOBuilder dtoBuilder;

    public ParseArmyCommand(final Token context, final DataTransferObject.DTOBuilder dto) {
        this.context = context;
        this.dtoBuilder = dto;
    }

    @Override
    public void execute() {
        final List<String> parsedContextList = Arrays.asList(context.getLogString().split(","));
        final String playerName = parsedContextList.get(0);
        final String botType = parsedContextList.get(1);
        final List<String> armyList = parsedContextList.subList(2, parsedContextList.size());
        if (!dtoBuilder.build().isFirstArmySet()) {
            dtoBuilder.setFirstArmy(new Army(playerName, botType, armyList));
            dtoBuilder.setIsFirstArmySet();
        } else {
            dtoBuilder.setSecondArmy(new Army(playerName, botType, armyList));
        }
    }
}
