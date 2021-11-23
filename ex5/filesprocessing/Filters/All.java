package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a filter of type all.
 *
 * @author Avi Kogan.
 */
class All extends Filter{

    /**
     * The index where the NOT suffix optionally added.
     */
    private static final short NOT_INDEX = 1;

    /**
     * Default constructor, will create All filter with default NOT suffix state (false).
     */
    public All(){}

    /**
     * Class constructor, create a filter of type all.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    public All(String[] filterParts) throws WarningFilterException {
        switch (filterParts.length){
            case 1:
                break;
            case 2:
                updateNotState(filterParts[NOT_INDEX]);
                break;
            default:
                throw new WarningFilterException();
        }
    }

    /**
     * Override super-class function, filter from the given array the files match to the filter state.
     * @param filesToFilter the files to filter.
     * @return new array with only the matched files.
     */
    public ArrayList<File> filter(ArrayList<File> filesToFilter) {
        return (_stateNot) ? new ArrayList<>() : filesToFilter;
    }
}
