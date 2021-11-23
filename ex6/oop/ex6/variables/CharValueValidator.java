package oop.ex6.variables;
import oop.ex6.parser.RegexTool;

/**
 *  Implements a ValueValidator for variables of char type.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public class CharValueValidator implements ValueValidator {

    private static final String CHAR = "char";

    /**
     * Validate the value is valid for the variable type.
     * @param varValue the given value for the variable.
     * @throws ValueTypeException if the value invalid.
     */
    @Override
    public void validateValue(String varValue) throws ValueTypeException {
        if (!RegexTool.isMatch(varValue ,RegexTool.CHAR_REGEX))
            throw new ValueTypeException();
    }

    /**
     * Check if the type of the assigned variable is valid to be assigned to the current variable.
     * @param assignedType the type of the assigned variable.
     * @throws ValueTypeException if the value invalid.
     */
    @Override
    public void validateTypeForAssignment(String assignedType) throws ValueTypeException {
        if(!assignedType.equals(CHAR)) throw new ValueTypeException();
    }
}
