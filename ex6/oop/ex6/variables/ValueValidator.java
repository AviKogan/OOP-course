package oop.ex6.variables;

/**
 *  Represent a ValueValidator for variables according to their type.
 *
 * @author Avi Kogan.
 * @author Carmel Gadot.
 */
public interface ValueValidator {

    /**
     * Validate the value is valid for the variable type.
     * @param varValue the given value for the variable.
     * @throws ValueTypeException if the value invalid.
     */
    void validateValue(String varValue) throws ValueTypeException;

    /**
     * Check if the type of the assigned variable is valid to be assigned to the current variable.
     * @param assignedType the type of the assigned variable.
     * @throws ValueTypeException if the value invalid.
     */
    void validateTypeForAssignment(String assignedType) throws ValueTypeException;
}
