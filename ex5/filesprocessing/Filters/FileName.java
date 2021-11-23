package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a filter of type file, match if the value to filter equals to the file name
 * (excluding the path).
 *
 * @author Avi Kogan.
 */
class FileName extends StringFilters{

    /**
     * Class constructor, create a filter of type file.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    public FileName(String[] filterParts) throws WarningFilterException {
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
            if(!_stateNot && file.getName().equals(_valueToFilter) ||
                _stateNot && !file.getName().equals(_valueToFilter)){
                // check whether the file name equal and no NOT suffix added,
                // or NOT suffix added and the file not equal.
                filteredFiles.add(file);
            }
        }

        filteredFiles.trimToSize();
        return filteredFiles;
    }
}
