package oop.ex6.scopes;

import oop.ex6.main.IllegalCodeException;

/**
 * Exception thrown when detected that more than one variable in the same scope declared with the same name.
 * Created to allow specific error if will be needed later.
 *
 * @author Avi Kogan.
 * @author Carnml Gadot.
 */
class DuplicateVariableDeclarationException extends IllegalCodeException {

    private static final long serialVersionUID = 1L;
}
