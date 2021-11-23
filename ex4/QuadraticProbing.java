/**
 * Represent the QuadraticProbing formula.
 * Has only one static method that calculate the QuadraticProbing index based on the given params.
 *
 * @author Avi Kogan.
 */
public class QuadraticProbing {

    /**
     * calculate the QuadraticProbing index based on the given params.
     * @param hashVal the hash value to calculate in the formula.
     * @param searchAttempt the search attempt number to calculate in the formula.
     * @param capacity the capacity to calculate in the formula.
     * @return return the QuadraticProbing index based on the given params.
     */
    public static int getIndex(int hashVal, int searchAttempt, int capacity){
        return (hashVal + (searchAttempt + searchAttempt * searchAttempt) / 2) & (capacity - 1);
    }
}
