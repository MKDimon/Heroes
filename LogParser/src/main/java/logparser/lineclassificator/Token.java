package logparser.lineclassificator;

public class Token {
    private final StringType st;
    private final String inputString;

    public Token(final StringType st, final String inputString) {
        this.st = st;
        this.inputString = inputString;
    }

    public StringType getSt() {
        return st;
    }

    public String getLogString() {
        return inputString;
    }

    @Override
    public String toString() {
        return st.getStr() + " , " + inputString + "\n";

    }
}
