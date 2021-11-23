package filesprocessing.Filters;
import filesprocessing.exceptions.WarningFilterException;

import java.io.File;
import java.util.ArrayList;

/**
 * This class inherit from FileStateFilters, match to hidden files.
 *
 * @author Avi kogan
 */
class Hidden extends FileStateFilters{

    /**
     * The first char of hidden file, excluding its path.
     */
    private static final char HIDDEN_START_CHAR = '.';

    /**
     * Class constructor, create a filter of type hidden.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    protected Hidden(String[] filterParts) throws WarningFilterException {
        super(filterParts);
    }

    /**
     * Override super-class function, filter from the given array the files match to the filter state.
     * @param filesToFilter the files to filter.
     * @return new array with only the matched files.
     */
    public ArrayList<File> filter(ArrayList<File> filesToFilter) {
        ArrayList<File> filteredFiles = new ArrayList<>(filesToFilter.size());

        for(File file : filesToFilter){
            if( file.getName().charAt(0) == HIDDEN_START_CHAR && _stateYes && !_stateNot ||
                file.getName().charAt(0) == HIDDEN_START_CHAR && !_stateYes && _stateNot ||
                !(file.getName().charAt(0) == HIDDEN_START_CHAR) && _stateYes && _stateNot ||
                !(file.getName().charAt(0) == HIDDEN_START_CHAR) && !_stateYes && !_stateNot ) {

                filteredFiles.add(file);
            }
        }

        filteredFiles.trimToSize();
        return filteredFiles;
    }
}
