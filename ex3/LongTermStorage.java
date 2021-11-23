
/**
 * Represent a long term storage in the Spaceship.
 *
 * @author Avi Kogan.
 */
public class LongTermStorage extends Storage {

    /**
     * The capacity of long time storage.
     */
    private static final int LTS_CAPACITY = 1000;

    /**
     * The LongTermStorage constructor, initialize it's capacity with LTS_CAPACITY with the super-class
     * constructor.
     */
    public LongTermStorage() { super(LTS_CAPACITY); }

    /**
     * Removes all of the mappings from this inventory.
     * The inventory will be empty after this call returns.
     */
    public void resetInventory() {
        getInventory().clear();
        setOccupiedCapacity(0);
    }
}