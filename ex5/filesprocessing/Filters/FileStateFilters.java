package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

/**
 * An abstract class, filters of file state inherit from.
 */
abstract class FileStateFilters extends Filter{

    /**
     * Represent if need to filter files match to the filter type or the other files, if true need to
     * filter the matched files.
     */
    protected boolean _stateYes;

    /**
     * The index where the filter state appear.
     */
    private static final short STATE_INDEX = 1;

    /**
     * The index where the NOT suffix optionally added.
     */
    private static final short NOT_INDEX = 2;

    /**
     * The length of the filter when NOT suffix didn't added.
     */
    private static final short WITHOUT_NOT_SUFFIX_LENGTH = 2;

    /**
     * The length of the filter when NOT suffix added.
     */
    private static final short WITH_NOT_SUFFIX_LENGTH = 3;

    /**
     * The String represent the filter state is YES.
     */
    private static final String YES_STATE = "YES";

    /**
     * The String represent the filter state is NO.
     */
    private static final String NO_STATE = "NO";

    /**
     * Called by sub-classes to init the class members.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    protected FileStateFilters(String[] filterParts) throws WarningFilterException {
        switch (filterParts.length){
            case WITHOUT_NOT_SUFFIX_LENGTH:
                updateFilterState(filterParts[STATE_INDEX]);
                break;
            case WITH_NOT_SUFFIX_LENGTH:
                updateFilterState(filterParts[STATE_INDEX]);
                updateNotState(filterParts[NOT_INDEX]);
                break;
            default:
                throw new WarningFilterException();
        }
    }

    /**
     * Update the filter _stateYes according to the given state.
     * @param state the state to update the state _stateYes member according to.
     * @throws WarningFilterException if the given state not valid.
     */
    private void updateFilterState(String state) throws WarningFilterException {
        switch (state){
            case YES_STATE:
                _stateYes = true;
                break;
            case NO_STATE:
                _stateYes = false;
                break;
            default:
                throw new WarningFilterException();
        }
    }
}
