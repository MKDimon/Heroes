package heroes.auxiliaryclasses.statisticsexception;

public enum StatisticsExceptionTypes {
    NULL_POINTER("Null pointer"),
    INCORRECT_PARAMS("Incorrect parameters"),
    ;
    private final  String errorType;
    StatisticsExceptionTypes(final String errorType){
        this.errorType = errorType;
    }
    public String getErrorType(){
        return errorType;
    }
}
