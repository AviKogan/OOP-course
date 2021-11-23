package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

import java.io.File;
import java.util.ArrayList;

/**
 * An abstract class that all the DirectoryProcessor filters inherit from.
 *
 * @author Avi Kogan.
 */
public abstract class Filter {

    /**
     * The valid NOT suffix to the filter.
     */
    protected static final String NOT = "NOT";

    /**
     * Represent the sate of not, if NOT suffix didn't added to filter it's false also false by default, if
     * added true.
     */
    protected boolean _stateNot = false;

    /**
     * update the state of the _stateNot member.
     * @param stateNot the given state to update the _stateNot to.
     * @throws WarningFilterException if the given stateNot not valid.
     */
    protected void updateNotState(String stateNot) throws WarningFilterException {
        if(stateNot.equals(NOT)){
            _stateNot = true;
        }else{
            throw new WarningFilterException();
        }
    }

    /**
     * An abstract class all inherit class need to implement.
     * Filters from the given array the files matches to the suc-class filter state.
     * @param filesToFilter  the files to filter.
     * @return new array with only the matched files.
     */
    public abstract ArrayList<File> filter(ArrayList<File> filesToFilter);

}
