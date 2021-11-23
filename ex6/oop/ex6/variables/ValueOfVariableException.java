package oop.ex6.variables;

import oop.ex6.main.IllegalCodeException;

/**
 * Exception thrown when trying to create new variable failed because the value didn't match to the type
 * available values, but the value can be other variable name, used to mark that need to check ig the value
 * is other declared variable.
 *
 * @author Avi Kogan.
 * @author Carnml Gadot.
 */
public class ValueOfVariableException extends IllegalCodeException {

    private static final long serialVersionUID = 1L;
}
