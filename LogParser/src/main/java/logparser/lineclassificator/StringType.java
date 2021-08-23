package logparser.lineclassificator;

public enum StringType {
    GAME_START("GAME START"),
    GAME_END("GAME END"),
    ARMY("ARMY"),
    DATE("DATE"),
    REPLAY("REPLAY"),
    RESULT("RESULT"),
    UNKNOWN("UNKNOWN")
    ;
    private final String str;
    StringType(final String str){
        this.str = str;
    }
    public String getStr() {return str;}
}
