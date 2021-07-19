package heroes.statistics;

public enum StatisticsExceptionTypes {
    NULL_POINTER("Null pointer"),
    INCORRECT_PARAMS("Incorrect parameters"),
    ;
    private String errorType;
    StatisticsExceptionTypes(String errorType){
        this.errorType = errorType;
    }
    public String getErrorType(){
        return errorType;
    }
}
