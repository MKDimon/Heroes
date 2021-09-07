package logparser;

import logparser.database.DBMSConnection;
import logparser.database.DTODataBaseParser;
import logparser.dto.DataTransferObject;
import logparser.filereader.FileReader;
import logparser.lineclassificator.Classifier;
import logparser.lineclassificator.Token;
import logparser.parser.statemachineparser.ParserCommandFactory;
import logparser.parser.statemachineparser.State;
import logparser.parser.statemachineparser.States;
import logparser.parser.statemachineparser.TokenStateTransitionMap;
import logparser.parser.statemachineparser.smcommands.IParseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        final DBMSConnection dbmsConnection = new DBMSConnection();
        final Connection connection = dbmsConnection.getConnection();
        final Classifier classifier = new Classifier();
        final TokenStateTransitionMap tokenStateMap = new TokenStateTransitionMap();
        States currentState = tokenStateMap.getStartState();
            try (FileReader fr = new FileReader(new FileInputStream("game_statistics/gameStatistics0.csv"),
                    StandardCharsets.UTF_8)) {
                final DataTransferObject.DTOBuilder dtoBuilder = DataTransferObject.newBuilder();
                do {
                    final Token token = classifier.classify(fr.readLine());
                    currentState = tokenStateMap.getNextState(currentState, token.getSt().getStr());
                    final ParserCommandFactory parserCommandFactory = new ParserCommandFactory();
                    final IParseCommand command = parserCommandFactory.getCommand(currentState, token, dtoBuilder);
                    command.execute();
                    System.out.println(currentState.getStr());
                    if (dtoBuilder.build().isFormed()) {
                        final DTODataBaseParser dataBaseParser = new DTODataBaseParser(dbmsConnection);
                        dataBaseParser.insertDTO(dtoBuilder.build());
                    }
                } while (fr.hasNext());
                final DataTransferObject dto = dtoBuilder.build();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

}
