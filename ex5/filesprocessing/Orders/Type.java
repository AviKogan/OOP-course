package filesprocessing.Orders;

import filesprocessing.exceptions.WarningOrderException;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represent a order of type type.
 *
 * @author Avi Kogan.
 */
class Type extends Order {

    /**
     * Class comparator, compare files lexicographic based on there type, if equal according to Abs order as
     * default.
     */
    private static final TypeComparator typeComparator = new TypeComparator();

    /**
     * Class constructor, create a order of type 'type'.
     * @param orderParts the order parts from the command file separated by #.
     * @throws WarningOrderException if the orderParts length not valid or one of the orderParts not valid.
     */
    protected Type(String[] orderParts) throws WarningOrderException {
        super(orderParts);
    }

    /**
     * @param file the file to check.
     * @return the file type.
     */
    static String getType(File file) {
        String type = "";

        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            type = file.getName().substring(i + 1);
        }
        return type;
    }

    /**
     * Override super-class function, order the given ArrayList according to the order state.
     * @param listToSort the files to sort.
     */
    public void sort(ArrayList<File> listToSort) {
        if(!_reverseState){
            sorter(listToSort, typeComparator);
        }else{
            sorter(listToSort, typeComparator.reversed());
        }
    }
}
