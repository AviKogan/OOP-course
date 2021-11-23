import oop.ex3.spaceship.Item;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class represent storage.
 * Sub-classes need to implement the addItem method with them conditions, the inventory is protected to
 * allow sub classes manage it
 *
 * @author Avi Kogan
 */
public abstract class Storage {

    /**
     * Represent the Storage inventory, each item mapped to its capacity - a non-negative amount, if some
     * item got to 0 amount it should be removed from the HashMap.
     */
    private HashMap<String, Integer> _inventory = new HashMap<String, Integer>();

    /**
     * Represent the locker capacity.
     */
    private final int _capacity;

    /**
     * Represent the current locker's occupied capacity.
     */
    private int _occupiedCapacity = 0;

    protected final String ERROR_START_MESSAGE = "ERROR: Your request cannot be completed at this time.";

    /**
     * This method add n times of the given type to the Storage _inventory.
     * n is non-negative, it added to the current mapped value of the item if the new amount is less than
     * available amount.
     *
     * @param item the item to add to the storage inventory.
     * @param n the amount of the item to add to the inventory.
     * @return 0 if success, otherwise sub-class error indicator.
     */
    public int addItem(Item item, int n){
        int ADDED_SUCCESSFULLY = 0;
        int CANT_ADD = -1;

        if(n < 0){
            ErrorDefaultMessage();
            return CANT_ADD;
        }

        if(isAvailableCapacity(item, n)){
            int newOccupiedCapacity = getOccupiedCapacity() + item.getVolume() * n;
            setOccupiedCapacity(newOccupiedCapacity);

            int newValue = n;
            if(_inventory.containsKey(item.getType())){
                newValue += _inventory.get(item.getType());
            }
            _inventory.put(item.getType(), newValue);

            return ADDED_SUCCESSFULLY;
        }
        ErrorNoRoomMessage(item.getType(), n);
        return CANT_ADD;
    }

    /**
     * The Storage class constructor, initialize the Storage capacity.
     * @param capacity the storage capacity.
     */
    public Storage(int capacity){ _capacity = capacity; }

    /**
     * @param type the item type to check it's amount in the inventory.
     * @return 0 if the type not exist in the inventory, otherwise the amount the inventory contain the type.
     */
    public int getItemCount(String type){
        int NOT_EXIST = 0;
        if(_inventory.containsKey(type)){
            return _inventory.get(type);
        }
        return NOT_EXIST;
    }

    /**
     * @return the Storage inventory.
     */
    public Map<String, Integer> getInventory() { return _inventory; }

    /**
     * @return the Storage capacity.
     */
    public int getCapacity() { return _capacity; }

    /**
     * @return the current available capacity.
     */
    public int getAvailableCapacity() { return _capacity - _occupiedCapacity; }

    /**
     * check if there is enough available capacity to add all n items of the given type to the Inventory.
     * @param item the item need to be added.
     * @param n the amount of times the item need to be added.
     * @return true if their is enough available capacity, otherwise false.
     */
    protected boolean isAvailableCapacity(Item item, int n){
        return (item.getVolume() * n) <= getAvailableCapacity();
    }

    /**
     * The error message printed if there is no available room to add n items of itemType.
     * @param itemType The item type can't ba added to print in the error message.
     * @param n The amount tried to added and failed.
     */
    private void ErrorNoRoomMessage(String itemType, int n) {
        System.out.println(ERROR_START_MESSAGE + " Problem: no room for " + n + " items of type " +  itemType);
    }

    /**
     * The error message printed by default if not defined in instruction scenario occurred.
     */
    private void ErrorDefaultMessage(){
        System.out.println(ERROR_START_MESSAGE);
    }

    /**
     * @return the current _occupiedCapacity value.
     */
    protected int getOccupiedCapacity(){ return _occupiedCapacity; }

    /**
     * set to _occupiedCapacity new value, no validation checks.
     * @param newVal the new value to set for _occupiedCapacity.
     */
    protected void setOccupiedCapacity(int newVal){ _occupiedCapacity = newVal; }
}
