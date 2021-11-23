package filesprocessing.Orders;

import filesprocessing.exceptions.WarningFilterException;
import filesprocessing.exceptions.WarningOrderException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represent a order of type abs.
 *
 * @author Avi Kogan.
 */
class Abs extends Order {

    /**
     * Class comparator, compare files lexicographic based on there absolute path.
     */
    static final Comparator<File> absComparator = Comparator.comparing(File::getAbsolutePath);

    /**
     * Default constructor, create an Abs instance with default values.
     */
    public Abs() { super(); }

    /**
     * Class constructor, create a order of type abs.
     * @param orderParts the order parts from the command file separated by #.
     * @throws WarningOrderException if the orderParts length not valid or one of the orderParts not valid.
     */
    Abs(String[] orderParts) throws WarningOrderException {
        super(orderParts);
    }

    /**
     * Override super-class function, order the given ArrayList according to the order state.
     * @param listToSort the files to sort.
     */
    public void sort(ArrayList<File> listToSort) {
        if(!_reverseState){
            sorter(listToSort, absComparator);
        }else{
            sorter(listToSort, absComparator.reversed());
        }
    }
}
