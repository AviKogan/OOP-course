/**
 * Implementation of ClosedHashSet.
 * implements SimpleSet because extending SimpleHashSet
 *
 * @see SimpleHashSet
 * @see SimpleSet
 * @author Avi Kogan.
 */
public class ClosedHashSet extends SimpleHashSet{

    /**
     * Represent the size of LinkListString if there was value that deleted, mark for contain search.
     */
    private final int DELETED_CELL = 0;

    /**
     * A default constructor. Constructs a new, empty table with default initial capacity (16), upper load
     * factor (0.75) and lower load factor (0.25).
     */
    public ClosedHashSet(){}

    /**
     * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     * @param upperLoadFactor The upper load factor of the hash table.
     * @param lowerLoadFactor The lower load factor of the hash table.
     */
    public ClosedHashSet(float upperLoadFactor, float lowerLoadFactor){
        super(upperLoadFactor, lowerLoadFactor);
    }

    /**
     * Data constructor - builds the hash set by adding the elements one by one. Duplicate values should be
     * ignored. The new table has the default values of initial capacity (16), upper load factor (0.75),
     * and lower load factor (0.25).
     * @param data Values to add to the set.
     */
    public ClosedHashSet(java.lang.String[] data){
        for(String newVal : data){
            add(newVal);
        }
    };

    /**
     * Add a specified element to the set if it's not already in it.
     * @param newValue New value to add to the set
     * @return False iff newValue already exists in the set
     */
    //Using quadratic prohibiting to handle collisions.
    //In ClosedHashSet _hashTable empty cell marked null, deleted cell marked as empty LinkedListString.
    public boolean add(String newValue) {
        if(contains(newValue)) return false;

        _size++; //update the size to enlarge if needed before adding.
        if(needEnlarge()){
            resize(capacity() * CAPACITY_FACTOR);
        }

        int searchAttempt = FIRST_ATTEMPT;
        int curIndToCheck = QuadraticProbing.getIndex(newValue.hashCode(), searchAttempt, capacity());
        while(true){
            if(_hashTable[curIndToCheck] == null){
                _hashTable[curIndToCheck] = new LinkedListString();
                _hashTable[curIndToCheck].addFirst(newValue);
                return true;
            } else if(_hashTable[curIndToCheck].size() == DELETED_CELL){
                _hashTable[curIndToCheck].addFirst(newValue);
                return true;
            }
            searchAttempt++;
            curIndToCheck = QuadraticProbing.getIndex(newValue.hashCode(), searchAttempt, capacity());
        }
    }

    /**
     * Look for a specified value in the set.
     * @param searchVal Value to search for
     * @return True iff searchVal is found in the set
     */
    //In ClosedHashSet _hashTable empty cell marked null, deleted cell marked as empty LinkedListString.
    public boolean contains(String searchVal) {
        int searchAttempt = FIRST_ATTEMPT;
        int curIndToCheck = QuadraticProbing.getIndex(searchVal.hashCode(), searchAttempt, capacity());

        //iterate with the returned indexes from QuadraticProbing until get to null cell
        int maxIterations = capacity();
        while(_hashTable[curIndToCheck] != null  && maxIterations > 0){
            if(_hashTable[curIndToCheck].contains(searchVal)) return true;

            searchAttempt++;
            curIndToCheck = QuadraticProbing.getIndex(searchVal.hashCode(), searchAttempt, capacity());
            maxIterations--;
        }
        return false;
    }

    /**
     * Remove the input element from the set.
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    public boolean delete(String toDelete) {
        int searchAttempt = FIRST_ATTEMPT;
        int curIndToCheck = QuadraticProbing.getIndex(toDelete.hashCode(), searchAttempt, capacity());

        //iterate over the returned indexes from QuadraticProbing until get to null cell (empty cell).
        while(_hashTable[curIndToCheck] != null){
            if(_hashTable[curIndToCheck].contains(toDelete)) {
                _hashTable[curIndToCheck].remove(toDelete);
                _size--;
                if(needReduce()){
                    int newSize = Math.max(MIN_CAPACITY, capacity() / CAPACITY_FACTOR);
                    resize(newSize);
                }
                return true;
            }

            searchAttempt++;
            curIndToCheck = QuadraticProbing.getIndex(toDelete.hashCode(), searchAttempt, capacity());
        }
        return false;
    }

    /**
     * Creates new array of given newSize, than re-hashing all the elements from the _hashTable to the
     * new array, add the values without check for duplicated. to handle collisions using the
     * QuadraticProhibiting like in the class 'add' method.
     *
     * @param newSize the size to create the new resized array with.
     */
    private void resize(int newSize) {
        LinkedListString[] arrayToResize = _hashTable;
        _hashTable = new LinkedListString[newSize];
        for (LinkedListString linkedListString : arrayToResize) {
            if (linkedListString != null && linkedListString.size() != DELETED_CELL) {
                String curValue = linkedListString.getFirst();
                int searchAttempt = FIRST_ATTEMPT;
                int curIndToCheck = QuadraticProbing.getIndex(curValue.hashCode(), searchAttempt, capacity());
                while(true){
                    if(_hashTable[curIndToCheck] == null){
                        _hashTable[curIndToCheck] = new LinkedListString();
                        _hashTable[curIndToCheck].addFirst(curValue);
                        break;
                    } else if(_hashTable[curIndToCheck].size() == DELETED_CELL){
                        _hashTable[curIndToCheck].addFirst(curValue);
                        break;
                    }
                    searchAttempt++;
                    curIndToCheck = QuadraticProbing.getIndex(curValue.hashCode(), searchAttempt, capacity());
                }
            }
        }
    }
}
