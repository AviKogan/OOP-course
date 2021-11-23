import oop.ex3.searchengine.Hotel;
import oop.ex3.searchengine.HotelDataset;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Test class for the BoopingSite class.
 *
 * @author Avi Kogan
 */
public class BoopingSiteTest{

    private static Hotel[] HOTELS_DATASET = HotelDataset.getHotels("hotels_dataset.txt");
    private static Hotel[] SMALL_HOTEL_DATASET = HotelDataset.getHotels("hotels_tst1.txt");
    private static Hotel[] EMPTY_HOTEL_DATASET = HotelDataset.getHotels("hotels_tst2.txt");

    private static Hotel[] EMPTY_HOTEL_ARRAY = new Hotel[0];

    private static final int EQUAL = 0;
    private static final int NOT_EQUAL = 1;

    private static final int MIN_LATITUDE_VAL = -90;
    private static final int MAX_LATITUDE_VAL = 90;
    private static final int MIN_LONGITUDE_VAL = -180;
    private static final int MAX_LONGITUDE_VAL = 180;

    /**
     * calculates the Euclidean distance of the given hotel from the class coordinate.
     * @param hotel the hotel to calculate it's Euclidean distance from the class coordinate.
     * @param latitude the latitude to calculate the proximity from.
     * @param longitude the longitude to calculate the proximity from.
     * @return the Euclidean distance of the given hotel from the class coordinate.
     */
    private double getProximity(Hotel hotel, double latitude, double longitude){
        return Math.pow(latitude - hotel.getLatitude(), 2) + Math.pow(longitude - hotel.getLongitude(), 2);
    }

    /**
     * Check for the given array if in some index the Hotel StarRating is equal to the prev index hotel
     * StarRating the PropertyName of the hotel in lexicographic compared not greater than the PropertyName
     * in the index before.
     * @param arr the array to check.
     * @return true if the array is valid for its propertyName order, otherwise false.
     */
    private boolean checkIfPropertyNameOrderValid(Hotel[] arr){
        if(arr.length > 1){
            for(int i = 1; i < arr.length; ++i){
                if(arr[i].getStarRating() == arr[i - 1].getStarRating() &&
                   arr[i].getPropertyName().compareTo(arr[i - 1].getPropertyName()) < 0){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * compare that in both array the the StarRating is the same in each index and if so check that in each
     * index the StarRating is greater or equal compared to the hotel in the prev index.
     * @param arr1 first array for the comparison.
     * @param arr2 second array for the comparison.
     * @return true if the array equal by comparing the StarRating in each array, and the StarRating in
     *         ascending order.
     */
    private boolean checkIfArraysEqualByRating(Hotel[] arr1, Hotel[] arr2){
        if(arr1.length != arr2.length) return false;
        if(arr1.length == 0){
            return true;
        } else{
            if(arr1[0].getStarRating() != arr1[0].getStarRating()) return false;
        }
        for(int i = 1; i < arr1.length; ++i){
            if(arr1[i].getStarRating() != arr1[i].getStarRating() ||
               arr1[i].getStarRating() < arr1[i-1].getStarRating()){
                return false;
            }
        }
        return true;
    }

    /**
     * Compares that in both given array in each index the proximity of the hotel at the index is equal to
     * the other one. assumes both not null and the latitude and longitude valid.
     * @param arr1 first array for the comparison.
     * @param arr2 second array for comparison.
     * @param latitude the latitude to calculate the proximity from.
     * @param longitude the longitude to calculate the proximity from.
     * @return true if the arrays equal, otherwise false.
     */
    private boolean checkIfArraysEqualByProximity(Hotel[] arr1, Hotel[] arr2, double latitude,
                                                  double longitude){
        if(arr1.length != arr2.length) return false;
        if(arr1.length == 0){
            return true;
        }
        if(getProximity(arr1[0], latitude, longitude) != getProximity(arr2[0], latitude, longitude)) {
            return false;
        }

        for(int i = 1; i < arr1.length; ++i){
            if(getProximity(arr1[i], latitude, longitude) != getProximity(arr2[i], latitude, longitude) ||
               getProximity(arr1[i], latitude, longitude) < getProximity(arr1[i - 1], latitude, longitude)){
                return false;
            }
        }
        return true;
    }


    /**
     * Check for the given array if in some index the Hotel proximity is equal to the prev index hotel
     * proximity, if so the POI number of the hotel validated that is not greater than the POI number in the
     * index before.
     * @param arr the array to check.
     * @param latitude the latitude to calculate the proximity from.
     * @param longitude the longitude to calculate the proximity from.
     * @return true if the POI order in the array valid, otherwise false.
     */
    private boolean checkIfPoiOrderValid(Hotel[] arr, double latitude, double longitude){
        if(arr.length > 1){
            for(int i = 1; i < arr.length; ++i){
                if(getProximity(arr[i], latitude, longitude) == getProximity(arr[i - 1], latitude, longitude) &&
                   arr[i].getNumPOI() > arr[i - 1].getNumPOI()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * test with "hotels_dataset.txt"  (The data member HOTELS_DATASET)ֿ
     * @param latitude the latitude to calculate the proximity from.
     * @param longitude the longitude to calculate the proximity from.
     */
    private void testProximityWithValidParams(double latitude, double longitude){
        BoopingSite boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        ArrayList<Hotel> arrayListOfHotelsInCity = new ArrayList<Hotel>(Arrays.asList(HOTELS_DATASET));
        Collections.sort(arrayListOfHotelsInCity, new HotelsByProximityComparator(latitude, longitude));
        Hotel[] testSortedArray = new Hotel[arrayListOfHotelsInCity.size()];
        testSortedArray = arrayListOfHotelsInCity.toArray(testSortedArray);

        Hotel[] boopingSortedArray = boopingWithBigData.getHotelsByProximity(latitude, longitude);
        checkIfArraysEqualByProximity(testSortedArray, boopingSortedArray, latitude, longitude);
        checkIfPoiOrderValid(testSortedArray, latitude, longitude);
        checkIfPoiOrderValid(boopingSortedArray, latitude, longitude);
    }

    @Test
    public void testConstructor(){
        BoopingSite call1 = new BoopingSite("hotels_dataset.txt");
        assertNotNull(call1);
        BoopingSite callWithEmptyHotels = new BoopingSite("hotels_tst2.txt");
        assertNotNull(callWithEmptyHotels);
    }

    @Test
    // The method get the hotels from the needed dataset by iterate over the dataSet and add to ArrayList
    // the hotels with the needed city, then sort it with Collection.sort using the
    // HotelsInCityByRatingComparator, then compare the sorted result to the returned array from the tested
    // method.
    public void testGetHotelsInCityByRating(){

        //test with big dataSet, choose random cities to check.
        BoopingSite boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        ArrayList<Hotel> arrayOfHotelsInCity = new ArrayList<Hotel>();
        String[] citiesToCheck = {HOTELS_DATASET[0].getCity(),
                                  HOTELS_DATASET[HOTELS_DATASET.length / 2].getCity(),
                                  HOTELS_DATASET[HOTELS_DATASET.length - 1].getCity()};

        for(String cityToSort : citiesToCheck){
            for(Hotel hotel : HOTELS_DATASET){
                if(hotel.getCity().equals(cityToSort)){
                    arrayOfHotelsInCity.add(hotel);
                }
            }
            Collections.sort(arrayOfHotelsInCity, new HotelsInCityByRatingComparator());
            Hotel[] testSortedArray = new Hotel[arrayOfHotelsInCity.size()];
            testSortedArray = arrayOfHotelsInCity.toArray(testSortedArray);
            Hotel[] boopingSortedArray = boopingWithBigData.getHotelsInCityByRating(cityToSort);
            assertTrue(checkIfArraysEqualByRating(boopingSortedArray, testSortedArray));
            assertTrue(checkIfPropertyNameOrderValid(testSortedArray));
            assertTrue(checkIfPropertyNameOrderValid(boopingSortedArray));
            arrayOfHotelsInCity.clear();
        }

        //test with empty dataSet
        BoopingSite callWithEmptyHotels = new BoopingSite("hotels_tst2.txt");
        Hotel[] boopingSortedArray = callWithEmptyHotels.getHotelsInCityByRating(citiesToCheck[1]);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);
    }

    @Test
    public void testGetHotelsByProximity(){
        //test with empty dataSet with valid params.
        BoopingSite boopingWithEmptyHotels = new BoopingSite("hotels_tst2.txt");
        Hotel[] boopingSortedArray = boopingWithEmptyHotels.getHotelsByProximity(10, 8);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid latitude negative
        BoopingSite boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsByProximity(MIN_LATITUDE_VAL - 0.01, 8);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid longitude negative
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsByProximity(10, MIN_LONGITUDE_VAL - 0.01);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid latitude positive
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsByProximity(MAX_LATITUDE_VAL + 0.01, 0);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid longitude positive
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsByProximity(1, MAX_LONGITUDE_VAL + 0.01);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        // -- test sorting correct --

        //test max parameter value
        testProximityWithValidParams(MAX_LATITUDE_VAL, MAX_LONGITUDE_VAL);

        //test min parameter value
        testProximityWithValidParams(MIN_LATITUDE_VAL, MIN_LONGITUDE_VAL);

        //test with 0 both
        testProximityWithValidParams(0, 0);
    }

    @Test
    public void testGetHotelsInCityByProximity(){
        //test with empty dataSet with valid params.
        BoopingSite boopingWithEmptyHotels = new BoopingSite("hotels_tst2.txt");
        Hotel[] boopingSortedArray = boopingWithEmptyHotels.getHotelsInCityByProximity("someSome",10, 8);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid latitude negative
        BoopingSite boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsInCityByProximity(HOTELS_DATASET[1].getCity(),
                MIN_LATITUDE_VAL - 0.01, 8);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid longitude negative
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsInCityByProximity(HOTELS_DATASET[0].getCity(),10,
                MIN_LONGITUDE_VAL - 0.01);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid latitude positive
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsInCityByProximity(HOTELS_DATASET[0].getCity(),
                MAX_LATITUDE_VAL + 0.01, 0);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        //test with non-empty dataSet with invalid longitude positive
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        boopingSortedArray = boopingWithBigData.getHotelsInCityByProximity(HOTELS_DATASET[0].getCity(),1,
                MAX_LONGITUDE_VAL + 0.01);
        assertArrayEquals(boopingSortedArray, EMPTY_HOTEL_ARRAY);

        // -- test sorting correct --
        //test with some cities -> returned sorted array.
        ArrayList<Hotel> arrayOfHotelsInCity = new ArrayList<Hotel>();
        boopingWithBigData = new BoopingSite("hotels_dataset.txt");
        String[] citiesToCheck = {HOTELS_DATASET[0].getCity(),
                HOTELS_DATASET[HOTELS_DATASET.length / 2].getCity(),
                HOTELS_DATASET[HOTELS_DATASET.length - 1].getCity()};

        double[][] arrayOfValidLatitudeLongitude = {{89, -2},{14, 66},{-74, 172}};
        int i = 0;
        Hotel[] testSortedArray;
        for(String cityToSort : citiesToCheck) {
            for (Hotel hotel : HOTELS_DATASET) {
                if (hotel.getCity().equals(cityToSort)) {
                    arrayOfHotelsInCity.add(hotel);
                }
            }
            Collections.sort(arrayOfHotelsInCity,
                    new HotelsByProximityComparator(arrayOfValidLatitudeLongitude[i][0],
                                                    arrayOfValidLatitudeLongitude[i][1]));

            testSortedArray = new Hotel[arrayOfHotelsInCity.size()];
            testSortedArray = arrayOfHotelsInCity.toArray(testSortedArray);

            boopingSortedArray = boopingWithBigData.getHotelsInCityByProximity(cityToSort,
                    arrayOfValidLatitudeLongitude[i][0], arrayOfValidLatitudeLongitude[i][1]);

            assertTrue(checkIfArraysEqualByProximity(boopingSortedArray, testSortedArray, arrayOfValidLatitudeLongitude[i][0],
                       arrayOfValidLatitudeLongitude[i][1]));

            assertTrue(checkIfPoiOrderValid(boopingSortedArray, arrayOfValidLatitudeLongitude[i][0],
                       arrayOfValidLatitudeLongitude[i][1]));
            assertTrue(checkIfPoiOrderValid(testSortedArray, arrayOfValidLatitudeLongitude[i][0],
                       arrayOfValidLatitudeLongitude[i][1]));

            arrayOfHotelsInCity.clear();
            i++;
        }

    }
}


