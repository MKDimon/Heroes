package logparser;

import logparser.filereader.FileReader;
import logparser.lineclassificator.Classifier;
import logparser.lineclassificator.Token;
import logparser.parser.statemachineparser.State;
import logparser.parser.statemachineparser.TokenStateMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        final Classifier classifier = new Classifier();
        final TokenStateMap tokenStateMap = new TokenStateMap();
        State currentState = tokenStateMap.getStartState();
            try (FileReader fr = new FileReader(new FileInputStream("game_statistics/gameStatistics0.csv"),
                    StandardCharsets.UTF_8)) {
                do {
                    final Token token = classifier.classify(fr.readLine());
                    currentState = tokenStateMap.getNextState(currentState, token.getSt().getStr());
                    System.out.println(currentState.toString());
                } while (fr.hasNext());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

}
