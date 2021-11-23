package filesprocessing.Filters;

import filesprocessing.exceptions.WarningFilterException;

import java.io.File;
import java.util.ArrayList;

class Smaller_than extends Filter{

    /**
     * The size to filter.
     */
    private double _size;

    /**
     * The index the size should appear in the array of Stings that represent given filter.
     */
    private static final short SIZE_INDEX = 1;

    /**
     * The index the NOT suffix should appear in the array of Stings that represent the given filter.
     */
    private static final short NOT_INDEX = 2;

    /**
     * The factor to convert bytes and k-bytes.
     */
    private static final int FACTOR_BYTES_TO_KB = 1024;

    private void updateSize(String sizeAsString) throws WarningFilterException {
        try{
            _size = Double.parseDouble(sizeAsString);
            if(_size < 0) throw new WarningFilterException(); //validate non negative
        }catch (NumberFormatException e){
            throw new WarningFilterException();
        }
    }

    /**
     * Class constructor, create a filter of type smaller_than.
     * @param filterParts the filter parts from the command file separated by #.
     * @throws WarningFilterException if the filterParts length not valid or one of the filter parts not
     *                                valid.
     */
    public Smaller_than(String[] filterParts) throws WarningFilterException {
        switch (filterParts.length){
            case 2:
                updateSize(filterParts[SIZE_INDEX]);
                break;
            case 3:
                updateSize(filterParts[SIZE_INDEX]);
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

        for(File file : filesToFilter){
            double fileSizeInKb =  (double)file.length() / FACTOR_BYTES_TO_KB;
            if(!_stateNot && fileSizeInKb < _size || _stateNot && fileSizeInKb >= _size){
                //check whether the file size is strictly smaller than _size and no NOT suffix added,
                // or if NOT suffix added and the file greater or equal than _size
                filteredFiles.add(file);
            }
        }

        filteredFiles.trimToSize();
        return filteredFiles;
    }
}
