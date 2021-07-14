package heroes.auxiliaryclasses.unitexception;

public class UnitException extends Exception {

    public UnitException(UnitExceptionTypes errorType) {
        super(errorType.getErrorType());
    }
}
