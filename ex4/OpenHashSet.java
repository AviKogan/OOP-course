/**
 * Implementation of OpenHashSet.
 * implements SimpleSet because extending SimpleHashSet
 *
 * @see SimpleHashSet
 * @see SimpleSet
 * @author Avi Kogan.
 */
public class OpenHashSet extends SimpleHashSet{

    /**
     * A default constructor. Constructs a new, empty table with default initial capacity (16), upper load
     * factor (0.75) and lower load factor (0.25).
     */
    public OpenHashSet(){}

    /**
     * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     * @param upperLoadFactor The upper load factor of the hash table.
     * @param lowerLoadFactor The lower load factor of the hash table.
     */
    public OpenHashSet(float upperLoadFactor, float lowerLoadFactor){
        super(upperLoadFactor, lowerLoadFactor);
    }

    /**
     * Data constructor - builds the hash set by adding the elements one by one. Duplicate values should be
     * ignored. The new table has the default values of initial capacity (16), upper load factor (0.75),
     * and lower load factor (0.25).
     * @param data Values to add to the set.
     */
    public OpenHashSet(java.lang.String[] data){
        for(String newVal : data){
            add(newVal);
        }
    }

    /**
     * Add a specified element to the set if it's not already in it.
     * @param newValue New value to add to the set
     * @return False iff newValue already exists in the set
     */
    //Using chaining to handle collisions.
    public boolean add(String newValue) {
        if(contains(newValue)) return false;

        _size++; //update the size to first of all enlarge if needed
        if(needEnlarge()){
            resize(capacity() * CAPACITY_FACTOR);
        }
        int hashTableIndex = clamp(newValue.hashCode());
        if(_hashTable[hashTableIndex] == null){
            _hashTable[hashTableIndex] = new LinkedListString();
        }
        _hashTable[hashTableIndex].addFirst(newValue);
        return true;
    }

    /**
     * Look for a specified value in the set.
     * @param searchVal Value to search for
     * @return True iff searchVal is found in the set
     */
    public boolean contains(String searchVal) {
        int hashTableIndex = clamp(searchVal.hashCode());
        if(_hashTable[hashTableIndex] != null){
            return _hashTable[hashTableIndex].contains(searchVal);
        }
        return false;
    }

    /**
     * Remove the input element from the set.
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    //Remove the value from the linkedList in the clamped index.
    public boolean delete(String toDelete) {
        int hashTableIndex = clamp(toDelete.hashCode());
        if(_hashTable[hashTableIndex] != null && _hashTable[hashTableIndex].remove(toDelete)){
            _size--;
            if(needReduce()){
                int newSize = Math.max(MIN_CAPACITY, capacity() / CAPACITY_FACTOR);
                resize(newSize);
            }
            return true;
        }
        return false;
    }

    /**
     * Creates new array of given newSize, than re-hashing all the elements from the arrayToResize to the
     * new array, re-hashing with.
     * @param newSize the size to create the new resized array with.
     */
    private void resize(int newSize) {
        LinkedListString[] arrayToResize = _hashTable;
        _hashTable = new LinkedListString[newSize];
        for (LinkedListString linkedListString : arrayToResize) {
            if (linkedListString != null) {
                moveToNewTable(linkedListString);
            }
        }
    }

    /**
     * Re-hashing all the elements from the listToMove to the current _hashTable, no check for duplicated.
     * @param listToMove the list to re-hash its values to the current _hashTable.
     */
    private void moveToNewTable(LinkedListString listToMove){
        while (listToMove.size() != 0){ //Assumes the listToMove is not null.
            String cur = listToMove.removeFirst();
            int indOfCur = QuadraticProbing.getIndex(cur.hashCode(), 0, _hashTable.length);
            if(_hashTable[indOfCur] == null){
                _hashTable[indOfCur] = new LinkedListString();
            }
            _hashTable[indOfCur].addFirst(cur);
        }
    }
}
