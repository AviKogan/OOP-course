import oop.ex3.searchengine.Hotel;
import java.util.Comparator;

/**
 * This Comparator compare hotels based on their proximity to given coordinate, in ascending order, the
 * same proximity calculated by Euclidean distance formula, if two hotels have the proximity the hotels POI
 * compared, comparing to support decreasing order in this comparison.
 *
 * @author Avi Kogan
 */
public class HotelsByProximityComparator implements Comparator<Hotel> {

    /**
     * The compared to coordinate latitude.
     */
    private final double _latitude;

    /**
     * The compared to coordinate longitude.
     */
    private final double _longitude;

    private static final int O1_GREATER = 1;
    private static final int O2_GREATER = -1;
    private static final int EQUAL = 0;

    /**
     * class constructor, assumes both params are valid for Euclidean distance calculations.
     * @param latitude the coordinate latitude in range [-90 , 90].
     * @param longitude the coordinate longitude in range [-180, 180].
     */
    public HotelsByProximityComparator(double latitude, double longitude){
        _latitude = latitude;
        _longitude = longitude;
    }

    /**
     * calculates the Euclidean distance of the given hotel from the class coordinate.
     * @param hotel the hotel to calculate it's Euclidean distance from the class coordinate.
     * @return the Euclidean distance of the given hotel from the class coordinate.
     */
    private double getDistanceOfHotel(Hotel hotel){
        return Math.pow(_latitude - hotel.getLatitude(), 2) + Math.pow(_longitude - hotel.getLongitude(), 2);
    }

    /**
     * Override Comparator method,
     * compare the item according described in the class description.
     * @return if o1 and o2 proximity equal: 1 if o1's POI number is less than o2 POI number
     *                                       -1 if o1's POI number is greater than o2 POI number
     *                                       0 if both equal.
     * ,otherwise o1 and o2 proximity not equal : 1 if o1 proximity is greater than o2 proximity
     *                                            -1 otherwise.
     */
    public int compare(Hotel o1, Hotel o2) {
        double distance1 = getDistanceOfHotel(o1);
        double distance2 = getDistanceOfHotel(o2);
        if(distance1 == distance2){
            if(o1.getNumPOI() > o2.getNumPOI()) return O2_GREATER; // to get decreasing order
            if(o1.getNumPOI() < o2.getNumPOI()) return O1_GREATER;
            return EQUAL;
        }
        return (distance1 > distance2) ? O1_GREATER : O2_GREATER;
    }
}
