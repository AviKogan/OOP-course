package oop.ex6.variables;
import oop.ex6.main.IllegalCodeException;
import oop.ex6.parser.RegexTool;

/**
 * This class represent a variable.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public class Variable {

    /* Represent if the variable in the global scope.  */
    private final boolean _isGlobal;

    /* The type of the variable */
    private final String _type;

    /* The name of the variable */
    private final String _name;

    /* Boolean represent if the variable is final */
    private final boolean _isFinal;

    /* Boolean represent if the variable is initialized */
    private boolean _isInitialized;

    /* ValueValidator obj represent the validatorType of the variable */
    private final ValueValidator _valueValidatorType;

    /**
     * Class constructor.
     * @param validatorType ValueValidator obj represent the validatorType of the variable
     * @param name the name of the variable
     * @param isFinal boolean represent if the variable is final
     * @param value the value of the variable
     * @param type the variable type
     * @param isGlobal the variable state - global or not.
     * @throws IllegalCodeException if the creation failed
     */
    public Variable(ValueValidator validatorType, String name, boolean isFinal, String value, String type,
                    boolean isGlobal)
            throws IllegalCodeException {

        validateName(name);
        _valueValidatorType = validatorType;
        _type = type;
        if(value == null){
            _isInitialized = false;
        }else{
            setValue(value);
            _isInitialized = true;
        }
        _name = name;
        _isFinal = isFinal;
        _isGlobal = isGlobal;
    }

    /**
     * @return return the name of the variable.
     */
    public String getName() {
        return _name;
    }

    /**
     * @return return the type of the variable.
     */
    public String getType() { return _type; }

    /**
     * @return return true if the variable in the global, otherwise false.
     */
    public boolean isGlobal() { return _isGlobal; }

    /**
     * @return true if the variable is initialized else false.
     */
    public boolean isInitialized() {
        return _isInitialized;
    }

    /**
     * make the boolean represent if the variable is initialized true.
     */
    public void setInitialized() {
        _isInitialized = true;
    }

    /**
     * Check if the variable can be modified to the varValue. if variable is not final and
     * if the value is valid according the type.
     * @param varValue the value for the variable to check
     * @throws ValueTypeException type error
     * @throws ValueOfVariableException if the value can be other variable.
     */
    public void setValue(String varValue) throws IllegalCodeException{
        if(_isFinal) // Check if this variable can be modified
            throw new ValueTypeException();
        isValidValue(varValue);
    }

    /**
     * Validate the name of the variable, check its pattern valid.
     * @param variableName the name to validate.
     * @throws VariableNameFormatException if the name not valid.
     */
    private static void validateName(String variableName) throws VariableNameFormatException {
        if (!RegexTool.isMatch(variableName, RegexTool.VAR_NAME_PATTERN)) // check format
            throw new VariableNameFormatException();
    }

    /**
     * @return true if the variable final, otherwise false.
     */
    public boolean isFinal() { return _isFinal;}

    /**
     * Check if the value valid for the variable type.
     * @param value the value to check
     * @throws ValueOfVariableException if the value can be other variable.
     * @throws ValueTypeException if the value not valid.
     */
    public void isValidValue(String value) throws IllegalCodeException {
        try{
            _valueValidatorType.validateValue(value);
        } catch(ValueTypeException e){
            if (RegexTool.isMatch(value, RegexTool.VAR_NAME_PATTERN)) throw new ValueOfVariableException();
            throw new ValueTypeException();
        }
    }

    /**
     * Check if the assigned variable type is valid for assignment to the current variable.
     * @param assignedType the assigned variable type
     * @throws ValueTypeException if the type not valid for assignment.
     */
    public void validateTypeForAssignment(String assignedType) throws ValueTypeException {
        _valueValidatorType.validateTypeForAssignment(assignedType);
    }
}
