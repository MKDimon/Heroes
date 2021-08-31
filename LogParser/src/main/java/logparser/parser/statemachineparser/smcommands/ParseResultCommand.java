package logparser.parser.statemachineparser.smcommands;

import logparser.dto.Army;
import logparser.dto.DataTransferObject;
import logparser.dto.Result;
import logparser.lineclassificator.Token;

import java.util.Arrays;
import java.util.List;

public class ParseResultCommand implements IParseCommand {

    final Token context;
    final DataTransferObject.DTOBuilder dtoBuilder;

    public ParseResultCommand(final Token context, final DataTransferObject.DTOBuilder dto) {
        this.context = context;
        this.dtoBuilder = dto;
    }

    @Override
    public void execute() {
        final List<String> parsedContextList = Arrays.asList(context.getLogString().split(","));
        final Integer endRoundName = Integer.parseInt(parsedContextList.get(0));
        final String result = parsedContextList.get(1);
        dtoBuilder.setResult(new Result(endRoundName, result));
    }
}
