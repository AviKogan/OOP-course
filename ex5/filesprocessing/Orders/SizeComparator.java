package filesprocessing.Orders;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator of Order of type Size, compare files according to its size, if equal according to the Abs
 * Order.
 */
class SizeComparator implements Comparator<File> {

    /**
     * Represent that the left file size is smaller.
     */
    private static final int LEFT_SMALLER = -1;

    /**
     * Represent that the right file size is larger.
     */
    private static final int LEFT_LARGER = 1;

    /**
     * Override the Comparator interface method (of files), compare files according to its size, if equal
     * according
     * to the Abs Order.
     * @param o1 lhs file in the comparison.
     * @param o2 rhs file in the comparison
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal
     *         to, or greater than the second.
     */
    public int compare(File o1, File o2) {
        if(o1.length() == o2.length()){
            return Abs.absComparator.compare(o1, o2);
        }
        return (o1.length() < o2.length()) ? LEFT_SMALLER : LEFT_LARGER;
    }
}
