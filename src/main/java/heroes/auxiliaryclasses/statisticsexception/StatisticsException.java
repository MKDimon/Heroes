package heroes.auxiliaryclasses.statisticsexception;

public class StatisticsException extends Exception {
    public StatisticsException(StatisticsExceptionTypes type){
        super(type.getErrorType());
    }

    public StatisticsException(Exception e){
        super(e);
    }
}
