package oop.ex6.parser;

import oop.ex6.main.IllegalCodeException;

/**
 * Exception thrown when there is mismatch with the number of open and closed bracket.
 * Created to allow specific error if will be needed later.
 *
 * @author Avi Kogan.
 * @author Carnml Gadot.
 */
class MisMatchOpenCloseBracketsException extends IllegalCodeException {

    private static final long serialVersionUID = 1L;
}
