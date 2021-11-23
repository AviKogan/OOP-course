package filesprocessing.Orders;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator of Order of type Type, compare files according to its type, if equal according to the Abs Order.
 */
class TypeComparator implements Comparator<File> {

    /**
     * Override the Comparator interface method (of files), compare files according to its type, if equal
     * according to the Abs Order.
     * @param o1 lhs file in the comparison.
     * @param o2 rhs file in the comparison
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal
     *         to, or greater than the second.
     */
    public int compare(File o1, File o2) {
        if(Type.getType(o1).equals(Type.getType(o2))){
            return Abs.absComparator.compare(o1, o2);
        }
        return Type.getType(o1).compareTo(Type.getType(o2));
    }
}
