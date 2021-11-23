package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

/**
 * An abstract class of filters that there filter value is String, and the filter line in the command file
 * is of the type NAME#VALUE or optional NAME#VALUE#NOT_SUFFIX.
 */
abstract class StringFilters extends Filter{

    /**
     * The index of the value to filter.
     */
    private static final short VALUE_INDEX = 1;

    /**
     * The index where the NOT suffix optionally added.
     */
    private static final short NOT_INDEX = 2;

    /**
     * Represent the empty string as value to filter.
     */
    private static final String EMPTY_VALUE = "";

    /**
     * The value to filter from files.
     */
    protected String _valueToFilter;

    /**
     * Called by sub-classes to init the class members.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    protected StringFilters(String[] filterParts) throws WarningFilterException {
        switch (filterParts.length){
            case 1:
                _valueToFilter = EMPTY_VALUE;
                break;
            case 2:
                _valueToFilter = filterParts[VALUE_INDEX];
                break;
            case 3:
                _valueToFilter = filterParts[VALUE_INDEX];
                updateNotState(filterParts[NOT_INDEX]); //todo need try catch?
                break;
            default:
                throw new WarningFilterException();
        }
    }
}
