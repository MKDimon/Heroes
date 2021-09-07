package logparser.parser.statemachineparser.smcommands;

import logparser.dto.DataTransferObject;

public class ParseStartLogCommand implements IParseCommand {
    final DataTransferObject.DTOBuilder dtoBuilder;


    public ParseStartLogCommand(final DataTransferObject.DTOBuilder dto) {
        this.dtoBuilder = dto;
    }

    @Override
    public void execute() {
        dtoBuilder.abort();
    }
}
