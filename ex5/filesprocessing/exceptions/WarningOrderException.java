package filesprocessing.exceptions;

/**
 * This class inherit from WarningsExceptions.
 * Should be thrown if : Order name is not matched to valid order Type (name is case-sensitive).
 *                       Bad parameter value (illegal value) in the filter.
 *
 * @author Avi Kogan.
 */
public class WarningOrderException extends WarningsExceptions{

    /**
     * Class serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class constructor.
     */
    public WarningOrderException(){}

}
