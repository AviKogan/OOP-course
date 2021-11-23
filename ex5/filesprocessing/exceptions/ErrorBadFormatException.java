package filesprocessing.exceptions;

/**
 * This class inherit from ErrorsExceptions, when the exception called it prints to the System.err the bad
 * format error.
 *
 * @author Avi kogan
 */
public class ErrorBadFormatException extends ErrorsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor, prints to the System.err the bad format error.
     */
    public ErrorBadFormatException() { System.err.println("ERROR: Bad format of Commands File"); }
}
