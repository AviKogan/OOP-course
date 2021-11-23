package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a filter of type between, match all the files in the range of the given lower and
 * upper bound or equal to them.
 *
 * @author Avi Kogan.
 */
class Between extends Filter{

    /**
     * The lower bound to be greater or equal to.
     */
    private double _lowerBound;

    /**
     * The upper bound to be smaller or equal to.
     */
    private double _upperBound;

    /**
     * The length of the array of filter parts if no NOT suffix added.
     */
    private static final short NO_SUFFIX_LEN = 3;

    /**
     * The length of the array of filter parts if NOT suffix added.
     */
    private static final short WITH_SUFFIX_LEN = 4;

    /**
     * The index where the lower bound should be.
     */
    private static final short LOWER_BOUND_INDEX = 1;

    /**
     * The index where the upper bound should be.
     */
    private static final short UPPER_BOUND_INDEX = 2;

    /**
     * The index where the NOT suffix optionally added.
     */
    private static final int FACTOR_BYTES_TO_KB = 1024;

    /**
     * The index the NOT suffix optionally appear in the array of Stings that represent the filter.
     */
    private static final short NOT_INDEX = 3;

    /**
     * Update the instance members according to the given params.
     *
     * @param lowerBound the lower bound to update with.
     * @param upperBound the upper bound to update with.
     * @throws WarningFilterException if one or both of the given bounds not valid.
     */
    private void _updateBounds(String lowerBound, String upperBound) throws WarningFilterException {
        try{
            _lowerBound = Double.parseDouble(lowerBound);
            _upperBound = Double.parseDouble(upperBound);
            if(_lowerBound < 0 || _upperBound < 0 || _lowerBound > _upperBound){
                throw new WarningFilterException();
            }
        }catch (NumberFormatException | WarningFilterException e){
            throw new WarningFilterException();
        }
    }

    /**
     * Class constructor, create a filter of type between.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    public Between(String[] filterParts) throws WarningFilterException {
        switch (filterParts.length){
            case NO_SUFFIX_LEN:
                _updateBounds(filterParts[LOWER_BOUND_INDEX], filterParts[UPPER_BOUND_INDEX]);
                break;
            case WITH_SUFFIX_LEN:
                _updateBounds(filterParts[LOWER_BOUND_INDEX], filterParts[UPPER_BOUND_INDEX]);
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
        ArrayList<File> filteredFiles = new ArrayList<>(filesToFilter.size());
        for(File file : filesToFilter) {
            double fileSizeInKb = (double)file.length() / FACTOR_BYTES_TO_KB;
            if (!_stateNot && _lowerBound <= fileSizeInKb && fileSizeInKb <= _upperBound ||
                 _stateNot && (fileSizeInKb < _lowerBound || fileSizeInKb > _upperBound)) {
                //check whether the file between bound and no NOT suffix added or
                // NOT suffix added and the file not between the bound
                filteredFiles.add(file);
            }
        }

        filteredFiles.trimToSize();
        return filteredFiles;
    }
}

