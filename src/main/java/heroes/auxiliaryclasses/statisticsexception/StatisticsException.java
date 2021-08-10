package heroes.auxiliaryclasses.statisticsexception;

public class StatisticsException extends Exception {
    public StatisticsException(final StatisticsExceptionTypes type){
        super(type.getErrorType());
    }

    public StatisticsException(final Exception e){
        super(e);
    }
}