package logparser.parser.statemachineparser;

public enum States {
    defaultState("DEFAULT"),
    startState("GAME START"),
    errorState("ERROR"),
    armyTokenState("ARMY"),
    dateTokenState("DATE"),
    replayTokenState("REPLAY"),
    resultTokenState("RESULT"),
    unknownState("UNKNOWN"),
    finishState("GAME END")
    ;
    private final String str;
    States(final String str){
        this.str = str;
    }
    public String getStr() {return str;}
}
