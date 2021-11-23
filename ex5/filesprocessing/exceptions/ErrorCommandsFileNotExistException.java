package filesprocessing.exceptions;

/**
 * This class inherit from ErrorsExceptions, when the exception called it prints to the System.err the
 * command file not exist error.
 *
 * @author Avi kogan
 */
public class ErrorCommandsFileNotExistException extends ErrorsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor, prints to the System.err the command file not exist error.
     */
    public ErrorCommandsFileNotExistException(){ System.err.println("ERROR: The command file not exist"); }
}
