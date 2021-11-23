package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

/**
 * Create Filter objects.
 *
 * @author Avi Kogan
 */
public abstract class FilterFactory {

    /**
     * Represent the filter of type greater_than in the command file
     */
    private static final String GREATER_THAN = "greater_than";

    /**
     * Represent the filter of type between in the command file
     */
    private static final String BETWEEN = "between";

    /**
     * Represent the filter of type smaller_than in the command file
     */
    private static final String SMALLER_THAN = "smaller_than";

    /**
     * Represent the filter of type file in the command file
     */
    private static final String FILE = "file";

    /**
     * Represent the filter of type contains in the command file
     */
    private static final String CONTAINS = "contains";

    /**
     * Represent the filter of type prefix in the command file
     */
    private static final String PREFIX = "prefix";

    /**
     * Represent the filter of type suffix in the command file
     */
    private static final String SUFFIX = "suffix";

    /**
     * Represent the filter of type writable in the command file
     */
    private static final String WRITABLE = "writable";

    /**
     * Represent the filter of type executable in the command file
     */
    private static final String EXECUTABLE = "executable";

    /**
     * Represent the filter of type hidden in the command file
     */
    private static final String HIDDEN = "hidden";

    /**
     * Represent the filter of type all in the command file
     */
    private static final String ALL = "all";

    /**
     * The splitter between the filter parts in the command file.
     */
    private static final String FILTER_SPLITTER = "#";

    /**
     * @param filterLine the filter line from the command file
     * @return new filter object according to the given filterLine
     * @throws WarningFilterException if one of the parts of the filterLine not valid.
     */
    public static Filter getFilter(String filterLine) throws WarningFilterException {

        String[] filterParts = filterLine.split(FILTER_SPLITTER);
        if (filterParts.length == 0) throw new WarningFilterException();

        switch (filterParts[0]){
            case GREATER_THAN:
                return new Greater_than(filterParts);
            case BETWEEN:
                return new Between(filterParts);
            case SMALLER_THAN:
                return new Smaller_than(filterParts);
            case FILE:
                return new FileName(filterParts);
            case CONTAINS:
                return new Contains(filterParts);
            case PREFIX:
                return new Prefix(filterParts);
            case SUFFIX:
                return new Suffix(filterParts);
            case WRITABLE:
                return new Writable(filterParts);
            case EXECUTABLE:
                return new Executable(filterParts);
            case HIDDEN:
                return new Hidden(filterParts);
            case ALL:
                return new All(filterParts);
            default:
                throw new WarningFilterException();
        }
    }

    /**
     * @return new object of the default filter type - Abs filter.
     */
    public static Filter getDefault() { return new All(); }
}
