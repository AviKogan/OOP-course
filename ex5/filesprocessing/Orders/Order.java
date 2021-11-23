package filesprocessing.Orders;

import filesprocessing.exceptions.WarningOrderException;
import java.io.File;
import java.util.*;

/**
 * An abstract class that all the DirectoryProcessor order types inherit from.
 *
 * @author Avi Kogan.
 */
public abstract class Order {

    /**
     * The reverse suffix can be added to the order.
     */
    private static final String ORDER_REVERSE_STATE = "REVERSE";

    /**
     * The index the reverse suffix optionally appear.
     */
    private static final short REVERSE_INDEX = 1;

    /**
     * The length of the order when REVERSE suffix didn't added.
     */
    private static final short WITHOUT_REVERSE_SUFFIX_LENGTH = 1;

    /**
     * The length of the order when REVERSE suffix added.
     */
    private static final short WITH_REVERSE_SUFFIX_LENGTH = 2;

    /**
     * The state of the reverse suffix, by default false (no reverse added).
     */
    protected boolean _reverseState = false;

    /**
     * Used for the quick sort function, indicate the comparison result.
     */
    private static final int SMALLER = 0;

    /**
     * Called by sub-classes to init the class members.
     * @param orderParts the order parts from the command file separated by #.
     * @throws WarningOrderException if the orderParts length not valid or one of the orderParts not valid.
     */
    protected Order(String[] orderParts) throws WarningOrderException {
        switch (orderParts.length){
            case WITHOUT_REVERSE_SUFFIX_LENGTH:
                break;
            case WITH_REVERSE_SUFFIX_LENGTH:
                if(orderParts[REVERSE_INDEX].equals(ORDER_REVERSE_STATE)) _reverseState = true;
                break;
            default:
                throw new WarningOrderException();
        }
    }

    /**
     * Default constructor, called by sub-classes to init class members as default.
     */
    protected Order() {}

    /**
     * Sort the list according to the order type.
     * @param listToSort the list to sort.
     */
    public abstract void sort(ArrayList<File> listToSort);

    /**
     * Implementation of the quickSort algorithm, using the given constructor to compare between the files.
     * @param list the list to sort.
     * @param comparator the comparator to use when compering files.
     * @param begin the index to begin sort from.
     * @param end the index to end sort in.
     */
    private static void quickSort(List<File> list, Comparator<File> comparator, int begin, int end){
        if(end <= begin) return;
        int pivot = partition(list, comparator, begin, end);
        quickSort(list, comparator, begin, pivot - 1);
        quickSort(list, comparator, pivot + 1, end);
    }

    /**
     * Implementation of the quickSort partition method.
     * @param list the list to make the partition on.
     * @param comparator the comparator to use in the partition process.
     * @param begin the index to begin partition from.
     * @param end the index to end partition in.
     * @return the pivot index of the partition.
     */
    private static int partition(List<File> list, Comparator<File> comparator, int begin, int end) {
        int pivot = end;
        int counter = begin;

        for (int i = begin; i < end; i++) {
        if (comparator.compare(list.get(i), list.get(pivot)) < SMALLER) {
                File temp = list.get(counter);
                list.set(counter, list.get(i));
                list.set(i, temp);
                counter++;
            }
        }
        File temp = list.get(pivot);
        list.set(pivot, list.get(counter));
        list.set(counter, temp);

        return counter;
    }

    /**
     * Sub-classes delegate there sort method to this super-class method.
     * @param list the list to sort.
     * @param comparator the comparator to sort according to match to the sub-class order type.
     */
    protected void sorter(List<File> list, Comparator<File> comparator){
        quickSort(list, comparator, 0, list.size() - 1);
    }
}
