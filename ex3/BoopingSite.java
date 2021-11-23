import oop.ex3.searchengine.Hotel;
import oop.ex3.searchengine.HotelDataset;

import java.util.*;

/**
 * Implement BoopingSite methods.
 *
 * @author Avi Kogan
 */
public class BoopingSite {

    private String _FILE_NAME;

    /**
     * Contain the the returned array from HotelDataset.getHotels, initialized in the class cinstructor.
     */
    private Hotel[] HOTELS_DATA_SET;

    private HotelsInCityByRatingComparator _byRatingComparator = new HotelsInCityByRatingComparator();

    /**
     * HashMap that it's key is the hotel city and value is LinkedList with all the Hotel objects with the
     * same city as their city.
     */
    private HashMap<String, LinkedList<Hotel>> _mapByCity = new HashMap<String, LinkedList<Hotel>>();

    private static final int MIN_LATITUDE_VAL = -90;

    private static final int MAX_LATITUDE_VAL = 90;

    private static final int MIN_LONGITUDE_VAL = -180;

    private static final int MAX_LONGITUDE_VAL = 180;

    private static final Hotel[] EMPTY_HOTEL_ARRAY = new Hotel[0];

    /**
     * validate that the latitude in the range [-90, 90] and the longitude in the range [-180, 180]
     * @param latitude the latitude to validate.
     * @param longitude the longitude to validate.
     * @return true if both valid, otherwise false.
     */
    private boolean isValidParam(double latitude, double longitude){
        if(latitude < MIN_LATITUDE_VAL || latitude > MAX_LATITUDE_VAL ||
                longitude < MIN_LONGITUDE_VAL || longitude > MAX_LONGITUDE_VAL){
            return false;
        }
        return true;
    }

    /**
     * get list of hotels, sort it with by using the HotelsByProximityComparator.
     * @param hotelsList the list of hotels to sort.
     * @param latitude the latitude to sort according to.
     * @param longitude the longitude to sort according to.
     * @return the sorted List converted to array.
     */
    private Hotel[] sortListByProximity(List<Hotel> hotelsList, double latitude, double longitude){
        Collections.sort(hotelsList, new HotelsByProximityComparator(latitude, longitude));
        Hotel[] sortedArray = new Hotel[hotelsList.size()];
        sortedArray = hotelsList.toArray(sortedArray);
        return sortedArray;
    }

    /**
     * need to be called after HOTEL_DATA_SET initialized, update the _mapByCity data member - insert each
     * city from the HOTELS_DATA_SET as ket in the map and add to the mapped LinkedList all the hotel in
     * this city.
     */
    private void initMapByCity(){
        for(Hotel hotel: HOTELS_DATA_SET){
            if(_mapByCity.containsKey(hotel.getCity())){
                _mapByCity.get(hotel.getCity()).add(hotel);
            } else{
                LinkedList<Hotel> newLinkedList = new LinkedList<Hotel>();
                newLinkedList.add(hotel);
                _mapByCity.put(hotel.getCity(), newLinkedList);
            }
        }
    }

    /**
     * class constructor, init the the data Structures that contain the given dataSet based on the given name.
     * @param name the file name of the data set.
     */
    public BoopingSite(String name){
        _FILE_NAME = name;
        HOTELS_DATA_SET = HotelDataset.getHotels(_FILE_NAME);
        initMapByCity();
    }

    /**
     * This method returns an array of hotels located in the given city, sorted from the highest star-rating
     * to the lowest, Hotels at have the same rating will be organized according to their property name
     * alphabet order,
     * @param city the city to rate the hotels in it.
     * @return empty array if no there are no hotels in the given city, otherwise sorted Hotels array as
     *         explained.
     */
    public Hotel[] getHotelsInCityByRating(String city){
        List<Hotel> cityHotels = _mapByCity.get(city);
        if (cityHotels != null) {
            Collections.sort(_mapByCity.get(city), _byRatingComparator);
            Hotel[] sortedArray = new Hotel[_mapByCity.get(city).size()];
            sortedArray = _mapByCity.get(city).toArray(sortedArray);
            return sortedArray;
        }
        return EMPTY_HOTEL_ARRAY;
    }

    /**
     * This method returns an array of hotels sorted according to their Euclidean distance from the given
     * geographic location, in ascending order. Hotels that are at the same distance from the given
     * location are organized according to the number of points-of-interest for which they are closed to
     * (in a decreasing order).
     * @param latitude the latitude value.
     * @param longitude the longitude value.
     * @return empty array in case of illegal input, otherwise sorted Hotels array as explained.
     */
    public Hotel[] getHotelsByProximity(double latitude, double longitude){
        if(isValidParam(latitude, longitude)){
            List<Hotel> dataSetAsList = Arrays.asList(HOTELS_DATA_SET.clone());
            return sortListByProximity(dataSetAsList, latitude, longitude);
        }
        return EMPTY_HOTEL_ARRAY;
    }

    /**
     * This method returns an array of hotels in the given city, sorted according to their Euclidean distance
     * from the given geographic location, in ascending order. Hotels that are at the same distance from
     * the given location are organized according to the number of points-of-interest for which they are
     * closed to (in a decreasing order).
     * @param city the city sort the Hotels in it by proximity.
     * @param latitude the latitude value.
     * @param longitude the longitude value.
     * @return empty array in case of illegal input, otherwise sorted Hotels array as explained.
     */
    public Hotel[] getHotelsInCityByProximity(String city, double latitude, double longitude){
        List<Hotel> cityHotels = _mapByCity.get(city);
        if(isValidParam(latitude, longitude) && cityHotels != null){
            return sortListByProximity(cityHotels, latitude, longitude);
        }
        return EMPTY_HOTEL_ARRAY;
    }

}
