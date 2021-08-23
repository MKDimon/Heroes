package logparser.filereader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

public class FileReader implements IReader, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(FileReader.class);
    private final BufferedReader br;
    private String nextLine;
    private String currentLine;
    private boolean setUp = false;

    /**
     * Constructs an object of FileReader from any input stream.
     *
     * @param in      is an input stream
     * @param charset is a Charset of input stream symbols.
     */
    public FileReader(final FileInputStream in, final Charset charset) {
        br = new BufferedReader(new InputStreamReader(in, charset));
    }

    private void setUp() {
        try {
            currentLine = br.readLine();
            nextLine = currentLine;
            setUp = true;
        } catch (IOException e) {
            logger.error("Cannot read from file", e);
        }
    }

    @Override
    public String readLine() {
        if (!setUp) {
            setUp();
        }
        try {
            currentLine = nextLine;
            nextLine = br.readLine();
        } catch (IOException e) {
            logger.error("Cannot read line from file", e);
        }
        return currentLine;
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            logger.error("Cannot close file", e);
        }
    }

}
