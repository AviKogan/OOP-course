package filesprocessing.exceptions;

/**
 * This class inherit from ErrorsExceptions, when the exception called it prints to the System.err the
 * wrong usage error.
 *
 * @author Avi kogan
 */
public class ErrorUsageException extends ErrorsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor, prints to the System.err the wrong usage error.
     */
    public ErrorUsageException(){ System.err.println("ERROR: Wrong usage. Should receive 2 arguments"); }
}
