package filesprocessing.exceptions;

/**
 * This class inherit from WarningsExceptions.
 * Should be thrown if : Filter name is not matched to valid filter Type (name is case-sensitive).
 *                       Bad parameter value (illegal value) in the filter.
 *
 * @author Avi Kogan.
 */
public class WarningFilterException extends WarningsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor.
     */
    public WarningFilterException(){}

}
