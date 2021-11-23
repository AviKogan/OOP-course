package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;

/**
 * Exception thrown if method called in the global scope.
 * Created to allow specific error if will be needed later.
 *
 * @author Avi Kogan.
 * @author Carnml Gadot.
 */
public class MethodCallInGlobalException extends IllegalCodeException {

    private static final long serialVersionUID = 1L;
}
