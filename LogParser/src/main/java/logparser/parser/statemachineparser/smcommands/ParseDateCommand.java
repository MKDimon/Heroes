package logparser.parser.statemachineparser.smcommands;

import logparser.dto.Army;
import logparser.dto.DataTransferObject;
import logparser.dto.LogDate;
import logparser.lineclassificator.Token;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ParseDateCommand implements IParseCommand {

    final Token context;
    final DataTransferObject.DTOBuilder dtoBuilder;

    public ParseDateCommand(final Token context, final DataTransferObject.DTOBuilder dto) {
        this.context = context;
        this.dtoBuilder = dto;
    }

    @Override
    public void execute() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            final Date date = dateFormat.parse(context.getLogString());
            if (!dtoBuilder.build().isBeginDateSet()) {
                dtoBuilder.setBeginDate(new LogDate(date));
                dtoBuilder.setIsBeginDateSet();
            } else {
                dtoBuilder.setEndDate(new LogDate(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
