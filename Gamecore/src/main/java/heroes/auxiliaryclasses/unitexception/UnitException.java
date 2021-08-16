package heroes.auxiliaryclasses.unitexception;

public class UnitException extends Exception {

    public UnitException(final UnitExceptionTypes errorType) {
        super(errorType.getErrorType());
    }
}
