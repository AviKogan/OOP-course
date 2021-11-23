package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

import java.io.File;
import java.util.ArrayList;

/**
 * This class inherit from FileStateFilters, match to executables files.
 *
 * @author Avi kogan
 */
class Executable extends FileStateFilters{

    /**
     * Class constructor, create a filter of type executable.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    public Executable(String[] filterParts) throws WarningFilterException {
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
            if(file.canExecute()  && _stateYes && !_stateNot ||
               file.canExecute() && !_stateYes && _stateNot ||
               !file.canExecute() && _stateYes && _stateNot ||
               !file.canExecute() && !_stateYes && !_stateNot ) {

                filteredFiles.add(file);
            }
        }

        filteredFiles.trimToSize();
        return filteredFiles;
    }
}
