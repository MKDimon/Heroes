package logparser.parser.statemachineparser.smcommands;

import logparser.dto.DataTransferObject;

public class ParseEndLogCommand implements IParseCommand {
    final DataTransferObject.DTOBuilder dtoBuilder;


    public ParseEndLogCommand(final DataTransferObject.DTOBuilder dto) {
        this.dtoBuilder = dto;
    }

    @Override
    public void execute() {
        dtoBuilder.setIsFormed();
    }
}
