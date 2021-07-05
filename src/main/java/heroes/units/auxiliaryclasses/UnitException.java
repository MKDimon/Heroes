package heroes.units.auxiliaryclasses;

public class UnitException extends Exception {

    public UnitException(UnitExceptionTypes errorType){
        super(errorType.getErrorType());
    }
}
