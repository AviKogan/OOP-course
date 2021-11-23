package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a filter of type file, match if the value to filter equals to the file suffix
 * (excluding the path).
 *
 * @author Avi Kogan.
 */
class Suffix extends StringFilters{

    /**
     * The value of empty suffix, used for comparison.
     */
    private static final String EMPTY_SUFFIX = "\\.";

    /**
     * The delimiter between the file name and it's suffix.
     */
    private static final String SUFFIX_DELIMITER = "\\.";

    /**
     * Class constructor, create a filter of type suffix.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    public Suffix(String[] filterParts) throws WarningFilterException {
        super(filterParts);
    }

    /**
     * @param file the file to check.
     * @return true if the file suffix match tio the class _valueToFilter suffix, otherwise false.
     */
    private boolean isFileSuffixMatch(File file){
        String[] filterParts = file.getName().split(SUFFIX_DELIMITER);
        if(filterParts.length == 2 && file.getName().charAt(0) == '.' || filterParts.length == 1) {
            //check if hidden file Or file contain only name and no dot (delimiter)
            //if one of them -> check if the suffix is the empty suffix
            return _valueToFilter.equals(EMPTY_SUFFIX);
        }
        return file.getName().endsWith(_valueToFilter);
    }

    /**
     * Override super-class function, filter from the given array the files match to the filter state.
     * @param filesToFilter the files to filter.
     * @return new array with only the matched files.
     */
    public ArrayList<File> filter(ArrayList<File> filesToFilter) {
        ArrayList<File> filteredFiles = new ArrayList<>(filesToFilter.size());
        if(!_stateNot){
            for(File file : filesToFilter){
                if(isFileSuffixMatch(file)) filteredFiles.add(file);
            }
        }else{
            for(File file : filesToFilter){
                if(!isFileSuffixMatch(file)) filteredFiles.add(file);
            }
        }
        return filteredFiles;
    }
}
