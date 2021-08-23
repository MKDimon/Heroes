package logparser;

import logparser.filereader.FileReader;
import logparser.lineclassificator.Classifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        final Classifier classifier = new Classifier();
            try (FileReader fr = new FileReader(new FileInputStream("src/main/resources/gameStatistics0.csv"),
                    StandardCharsets.UTF_8)) {
                System.out.println("File opened");
                do {
                    System.out.println(classifier.classify(fr.readLine()).toString());
                } while (fr.hasNext());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

}
