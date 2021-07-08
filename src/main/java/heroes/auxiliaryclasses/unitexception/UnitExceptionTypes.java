package heroes.auxiliaryclasses.unitexception;

public enum UnitExceptionTypes {
    NULL_POINTER("Null pointer"),
    INCORRECT_HP("Incorrect hp"),
    INCORRECT_ARMOR("Incorrect armor"),
    INCORRECT_POWER("Incorrect power"),
    INCORRECT_ACCURACY("Incorrect accuracy"),
    UNIT_CANT_STEP("Step is impossible"),
    ;

    private final String errorType;

    UnitExceptionTypes(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }
}
