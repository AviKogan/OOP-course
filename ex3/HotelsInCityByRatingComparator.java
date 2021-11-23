import oop.ex3.searchengine.Hotel;
import java.util.Comparator;

/**
 * This Comparator compare hotels based on their StarRating, in ascending order, if two hotels have
 * the same StarRating the hotels PropertyName compared by alphabet order.
 *
 * @author Avi Kogan
 */
public class HotelsInCityByRatingComparator implements Comparator<Hotel>{

    private static final int O1_GREATER = 1;
    private static final int EQUAL = 0;
    private static final int O2_GREATER = -1;


    /**
     * Override Comparator method,
     * compare the item according described in the class description.
     * @return if o1 and o2 StarRating equal: 1 if o1's PropertyName alphabet greater than o2 PropertyName.
     *                                       -1 if o1's PropertyName alphabet less than o2 PropertyName.
     *                                        0 if both PropertyName equal.
     * ,otherwise o1 and o2 StarRating not equal : 1 if o1 StarRating is greater than o2 StarRating
     *                                            -1 otherwise.
     */
    public int compare(Hotel o1, Hotel o2) {
        if(o1.getStarRating() == o2.getStarRating()){
            int propertyComparedVal = o1.getPropertyName().compareTo(o2.getPropertyName());
            if(propertyComparedVal > 0) return O1_GREATER;
            if(propertyComparedVal < 0) return O2_GREATER;
            return EQUAL;
        }
        return o1.getStarRating() > o2.getStarRating() ? O1_GREATER : O2_GREATER;
    }
}
