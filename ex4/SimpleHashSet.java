
/**
 * A superclass for implementations of hash-sets implementing the SimpleSet interface.
 *
 * @see SimpleSet
 * @author avikogan
 */
public abstract class SimpleHashSet implements SimpleSet{

    /**
     * Describes the higher load factor of a newly created hash set.
     */
    protected static float DEFAULT_HIGHER_CAPACITY = 0.75f;

    /**
     * Describes the lower load factor of a newly created hash set.
     */
    protected static float DEFAULT_LOWER_CAPACITY = 0.25f;

    /**
     * Describes the capacity of a newly created hash set.
     */
    protected static int INITIAL_CAPACITY = 16;

    /**
     * Represent the change done in the capacity when need to resize it, keep the capacity size a power of 2.
     */
    protected static final int CAPACITY_FACTOR = 2;

    /**
     * Describes the minimal capacity of hash set.
     */
    protected static int MIN_CAPACITY = 1;

    /**
     * Represents the size of empty hash table.
     */
    protected static final int EMPTY_HASH_TABLE = 0;

    /**
     * Represent the value of i at the first attempt in the QuadraticProbing formula.
     */
    protected static final int FIRST_ATTEMPT = 0;

    /**
     * Represent the value of upperLoadFactor
     */
    protected static final int NOT_INCLUSIVE_UPPER_LOAD_FACTOR = 1;

    /**
     * The upper load factor before rehashing
     */
    private final float _upperLoadFactor;

    /**
     * The lower load factor before rehashing
     */
    private final float _lowerLoadFactor;

    /**
     * Represent the current number of elements in the set.
     */
    protected int _size = EMPTY_HASH_TABLE;

    /**
     * The SimpleHashSet hashTable contains in each cell a LinkedListString.
     */
    protected LinkedListString[] _hashTable = new LinkedListString[INITIAL_CAPACITY];

    /**
     * Constructs a new hash set with the default capacities given in DEFAULT_LOWER_CAPACITY and
     * DEFAULT_HIGHER_CAPACITY.
     */
    protected SimpleHashSet(){
        _upperLoadFactor = DEFAULT_HIGHER_CAPACITY;
        _lowerLoadFactor = DEFAULT_LOWER_CAPACITY;
    }

    /**
     * Constructs a new hash set with capacity INITIAL_CAPACITY.
     * @param upperLoadFactor the upper load factor before rehashing
     * @param lowerLoadFactor the lower load factor before rehashing
     */
    protected SimpleHashSet(float upperLoadFactor, float lowerLoadFactor){
        _lowerLoadFactor = lowerLoadFactor;
        _upperLoadFactor = upperLoadFactor;
    }

    /**
     * @return The current capacity (number of cells) of the table.
     */
    public int capacity(){return _hashTable.length;}

    /**
     * Clamps hashing indices to fit within the current table capacity (see the exercise description for
     * details)
     * @param index the index before clamping.
     * @return an index properly clamped.
     */
    protected int clamp (int index){
        return QuadraticProbing.getIndex(index, FIRST_ATTEMPT, capacity());
    }

    /**
     * @return The lower load factor of the table.
     */
    protected float getLowerLoadFactor(){ return _lowerLoadFactor; }

    /**
     * @return The higher load factor of the table.
     */
    protected float getUpperLoadFactor(){ return _upperLoadFactor; }

    /**
     * @return The number of elements currently in the set
     */
    public int size() { return _size; }

    /**
     * @return true if current load factor is bigger then hashSet UpperLoadFactor.
     */
    protected boolean needEnlarge(){
        return (float) size() / capacity() > _upperLoadFactor;
    }

    /**
     * @return true if current load factor is smaller then hashSet LowerLoadFactor.
     */
    protected boolean needReduce(){
        return (float) size() / capacity() < _lowerLoadFactor;
    }
}
