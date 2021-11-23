package filesprocessing.exceptions;

/**
 * This class inherit from ErrorsExceptions, when the exception called it prints to the System.err the
 * source directory not exist error.
 *
 * @author Avi kogan
 */
public class ErrorDirectoryNotExistException extends ErrorsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor, prints to the System.err the source directory not exist error.
     */
    public ErrorDirectoryNotExistException(){ System.err.println("ERROR: sourcedir not exist");}
}
