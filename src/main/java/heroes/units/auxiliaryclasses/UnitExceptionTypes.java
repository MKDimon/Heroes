package heroes.units.auxiliaryclasses;

public enum UnitExceptionTypes {
    NULL_POINTER("Null pointer"),
    INCORRECT_HP("Incorrect hp"),
    INCORRECT_ARMOR("Incorrect armor"),
    INCORRECT_POWER("Incorrect power"),
    INCORRECT_ACCURACY("Incorrect accuracy");

    private String errorType;
    UnitExceptionTypes(String errorType){
        this.errorType = errorType;
    }

    public String getErrorType(){
        return errorType;
    }
}
