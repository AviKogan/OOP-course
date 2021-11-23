package filesprocessing.exceptions;

/**
 * This class inherit from ErrorsExceptions, when the exception called it prints to the System.err the bad
 * sub-section name error.
 *
 * @author Avi kogan
 */
public class ErrorBadSubSectionNameException extends ErrorsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    public ErrorBadSubSectionNameException() { System.err.println( "ERROR: Bad subsection name"); }
}
