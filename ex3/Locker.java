
import oop.ex3.spaceship.Item;
import java.util.*;


/**
 * Represent a locker in the Spaceship.
 *
 * The locker manage it's item adding, if item of specific type take up more than 50% of the locker's
 * capacity, some of them automatically moved to the long-term Storage, the remaining amount should only
 * take up to 20% of the locker's capacity, the items added only if the move succeeded, otherwise no item
 * added.
 *
 * @author Avi Kogan
 */
public class Locker extends Storage{

    private LongTermStorage _lts;

    /**
     * Represent all the given constrains, each constrain represented in both direction, both of the pair is
     * key as Item type (String) and the second in the mapped LinkedHashMap - the second's type (Sting) is the
     * key in the LinkedHashMap and as Item in the LinkedHashMap value.
     */
    private HashMap<String, LinkedHashMap<String, Item>> _constrains = new HashMap<String, LinkedHashMap<String, Item>>();

    /**
     * helper function for initConstrains, check that constrainsKeyItem type exist as key in _constrains then
     * add constrainsValItem to its mapped LinkedHashMap if it's not exit their.
     * if the key not exist creates new key from constrainsKeyItem type and new value - a linkedHashMap with
     * constrainsValItem type mapped to constrainsValItem added it to the _constrains HashMap.
     * @param constrainsKeyItem the constrains item to add as key.
     * @param constrainsValItem the constrains item to add to as value in constrainsKey mapped linkedList.
     */
    private void addConstrains(Item constrainsKeyItem, Item constrainsValItem){
        String constrainsKey = constrainsKeyItem.getType();
        String constrainsVal = constrainsValItem.getType();
        if(_constrains.containsKey(constrainsKey)) {
                _constrains.get(constrainsKey).put(constrainsVal, constrainsValItem);
        } else{
            LinkedHashMap<String, Item> mapValue = new LinkedHashMap<String, Item>();
            mapValue.put(constrainsVal, constrainsValItem);
            _constrains.put(constrainsKey, mapValue);
        }
    }


    /**
     * Adds to the _constrains hashMap all the constrains, each constrain added once in both directions -
     * means constrain in index 0 added as key and the constrain in index 1 added as value to its mapped
     * linkedList (as String in key and Item in value), and again opposite.
     *
     * @param constrains array of pair of two items that are not allowed to reside in the locker.
     */
    private void initConstrains(Item[][] constrains) {
        for (Item[] constrainsPair : constrains) {
            Item constrain0 = constrainsPair[0];
            Item constrain1 = constrainsPair[1];
            addConstrains(constrain0, constrain1);
            addConstrains(constrain1, constrain0);
        }
    }

    /**
     * The Locker constructor, initialize Lockers capacity, lts and constrains with the given params.
     * assumes all the given parameters are valid, none of them null.
     * @param lts the spaceShip whose the locker belong LongTermStorage.
     * @param capacity the Locker's capacity.
     * @param constrains the locker constrains.
     */
    public Locker(LongTermStorage lts, int capacity, Item[][] constrains) {
        super(capacity);
        _lts = lts;
        initConstrains(constrains);
    }

    /**
     * Implement the addItem Storage method.
     * First check if the added item not make contradiction violation.
     * Adds the item if the locker available capacity is enough for the all need to add items, if to add
     * the items the specific item will take more then 50% of locker's capacity its try to move some of
     * the items to the long-term Storage and the remaining amount should take up to 20% if the inventory.
     *
     * @param item the item to add to the storage inventory.
     * @param n the amount of the item to add to the inventory.
     * @return -1 if n items cannot be added,
     *         -2 if the tried to add item causing contradiction (no item added).
     *          1 if the add succeeded but caused some items move to the lts successfully.
     *          0 if add succeeded without moving any item to the lts.
     *
     */
    public int addItem(Item item, int n){
        // return values
        int ADDED_SUCCESSFULLY = 0;
        int ADDED_BUT_ITEMS_MOVED_TO_LTS = 1;
        int CANT_ADD = -1;
        int CONSTRAINS_ADD_ERROR = -2;

        int curItemVolume = item.getVolume();
        String curItemType = item.getType();

        // validating no constrains violation.
        LinkedHashMap<String, Item> itemsConstrains = _constrains.get(item.getType());
        if(itemsConstrains != null){
            for(Map.Entry<String, Item> entry : itemsConstrains.entrySet()){
                if(getInventory().get(entry.getValue().getType()) != null){
                    errorConstrainsAdd(curItemType);
                    return CONSTRAINS_ADD_ERROR;
                }
            }
        }

        //check if n non-negative
        if(n < 0){
            System.out.println(ERROR_START_MESSAGE);
            return CANT_ADD;
        }

        //check if the new amount bigger than available capacity
        if(!isAvailableCapacity(item, n)) return super.addItem(item, n);

        //check if the new amount is more then 50% of locker's capacity.
        int newItemCapacity = (getItemCount(curItemType) + n) * curItemVolume;
        if(newItemCapacity > (getCapacity() / 2)){
            //try to move some items to lts, leave in the locker max amount that <= then 20% of capacity.
            int needToStayNumber = (getCapacity() / 5) / curItemVolume;
            int needToMoveToLtsNumber = (getItemCount(curItemType) + n) - needToStayNumber;
            if(_lts.addItem(item, needToMoveToLtsNumber) == ADDED_SUCCESSFULLY){
                removeItem(item, getItemCount(curItemType));
                if(needToStayNumber != 0){
                    addItem(item, needToStayNumber);
                }
                warningMovedItemToLtsSuccess();
                return ADDED_BUT_ITEMS_MOVED_TO_LTS;
            }else{
                return CANT_ADD;
            }

        }
        // The new amount <= then 50% of locker's capacity.
        return super.addItem(item, n);
    }

    /**
     * This method removes n Items of the item type from the locker.
     * remove only if n is not greater than actual amount in the inventory.
     * remove the map if the keys value become 0.
     *
     * @param item the item to remove from the inventory storage.
     * @param n the amount of the item to remove from the inventory.
     * @return 0 if the amount of item removed successfully,
     *         -1 if n in negative, or n is bigger than the actual item amount in the inventory.
     */
    public int removeItem(Item item, int n){
        int REMOVED_SUCCESSFULLY = 0;
        int REMOVED_FAILED = -1;
        String curItemType = item.getType();

        // validate n not negative number
        if(n < 0){
            errorNegativeNumberRemove(curItemType);
            return REMOVED_FAILED;
        }

        // check if n bigger than current amount, if so return failed.
        if(getItemCount(curItemType) < n){
            errorRemoveMoreThenAmount(curItemType, n);
            return REMOVED_FAILED;
        }

        int newAmount = getItemCount(curItemType) - n;
        //if all the item removed from the locker its ket removed from the inventory
        if(newAmount == 0){
            getInventory().remove(curItemType);
        } else{
            getInventory().put(curItemType, newAmount);
        }
        int newOccupiedCapacity = getOccupiedCapacity() - item.getVolume() * n;
        setOccupiedCapacity(newOccupiedCapacity);
        return REMOVED_SUCCESSFULLY;
    }

    /**
     * The error message printed if there is contradiction with the given itemType to add to the inventory.
     * @param itemType The item type can't ba added to print in the error message.
     */
    private void errorConstrainsAdd(String itemType) {
        System.out.println(ERROR_START_MESSAGE + " Problem: the locker cannot contain item of type " +
                           itemType + ", as it contains contradicting item");
    }

    /**
     * The error message printed if when adding item that already existed in the inventory, and the new
     * item capacity is more then 50% of the locker capacity, what caused some part of the item capacity move
     * to the lts, and the move done successfully,
     */
    private void warningMovedItemToLtsSuccess(){
        System.out.println("Warning: Action successful, but caused items to be moved to Storage");
    }

    /**
     * The error message printed if removeItem given negative parameter to remove.
     * @param itemType The item type can't ba removed to print in the error message.
     */
    private void errorNegativeNumberRemove(String itemType){
        System.out.println(ERROR_START_MESSAGE + " Problem: cannot remove a negative number of items of " +
                           "type " + itemType);
    }

    /**
     * The error message printed if tried to remove more than actual amount of itemType in the locker.
     * @param itemType The item type can't ba removed to print in the error message.
     * @param n The amount tried to remove.
     */
    private void errorRemoveMoreThenAmount(String itemType, int n){
        System.out.println(ERROR_START_MESSAGE + " Problem: The locker does not contain " + n + " items of " +
                "the type " + itemType);
    }

}
