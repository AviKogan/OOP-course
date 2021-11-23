package filesprocessing.Orders;

import filesprocessing.exceptions.WarningOrderException;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a order of type size.
 *
 * @author Avi Kogan.
 */
class Size extends Order {

    /**
     * Class comparator, compare files based on there size, if equal according to Abs order as default.
     */
    private static final SizeComparator sizeComparator = new SizeComparator();

    /**
     * Class constructor, create a order of type size.
     * @param orderParts the order parts from the command file separated by #.
     * @throws WarningOrderException if the orderParts length not valid or one of the orderParts not valid.
     */
    protected Size(String[] orderParts) throws WarningOrderException {
        super(orderParts);
    }

    /**
     * Override super-class function, order the given ArrayList according to the order state.
     * @param listToSort the files to sort.
     */
    public void sort(ArrayList<File> listToSort) {
        if(!_reverseState){
            sorter(listToSort, sizeComparator);
        }else{
            sorter(listToSort, sizeComparator.reversed());
        }
    }
}
