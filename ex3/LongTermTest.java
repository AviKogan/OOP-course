import oop.ex3.spaceship.Item;
import oop.ex3.spaceship.ItemFactory;
import org.junit.*;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for the LongTermStorage class.
 *
 * @author Avi Kogan.
 */
public class LongTermTest {
    static Item[] ALL_LEGAL_ITEMS;
    static Item[] SMALL_LEGAL_ITEM_TO_CHECK;

    private static final int LTS_CAPACITY = 1000;


    @BeforeClass
    public static void createTestObjects(){
        ALL_LEGAL_ITEMS = ItemFactory.createAllLegalItems();
        SMALL_LEGAL_ITEM_TO_CHECK = new Item[3];
        SMALL_LEGAL_ITEM_TO_CHECK[0] = ALL_LEGAL_ITEMS[0];
        SMALL_LEGAL_ITEM_TO_CHECK[1] = ALL_LEGAL_ITEMS[ALL_LEGAL_ITEMS.length / 2];
        SMALL_LEGAL_ITEM_TO_CHECK[2] = ALL_LEGAL_ITEMS[ALL_LEGAL_ITEMS.length - 1];
    }

    @Test
    public void testConstructor(){
        LongTermStorage lts = new LongTermStorage();
        assertNotNull(lts);
        assertEquals(lts.getCapacity(), LTS_CAPACITY);
        assertNotNull(lts.getInventory());
        assertEquals(lts.getInventory().size(), 0);
    }

    @Test
    public void testGetCapacity(){
        LongTermStorage lts = new LongTermStorage();

        // try adding all the legal items
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() <= lts.getAvailableCapacity()){
                lts.addItem(item, 1);
                //test no change in capacity after adding item.
                assertEquals(lts.getCapacity(), LTS_CAPACITY);
            }
        }

        // add amount bigger than LTS_CAPACITY when the inventory not empty not change the capacity.
        addMoreThanLtsCapacity(lts);
        assertEquals(lts.getCapacity(), LTS_CAPACITY);

        lts.resetInventory();
        // add more then LTS_CAPACITY when the inventory empty doesn't change the capacity
        addMoreThanLtsCapacity(lts);
        assertEquals(lts.getCapacity(), LTS_CAPACITY);
    }

    @Test
    public void testGetAvailableCapacity(){
        LongTermStorage lts = new LongTermStorage();
        assertEquals(lts.getAvailableCapacity(), LTS_CAPACITY);

        // Test add various type of item, check if the available capacity updated accordingly.
        int curAvailableCapacity = LTS_CAPACITY;
        for(int i = 0; i < ALL_LEGAL_ITEMS.length; ++i){
            if(ALL_LEGAL_ITEMS[i].getVolume() * i <= curAvailableCapacity){
                curAvailableCapacity -= ALL_LEGAL_ITEMS[i].getVolume() * i;
            }
            lts.addItem(ALL_LEGAL_ITEMS[i], i);
            assertEquals(lts.getAvailableCapacity(), curAvailableCapacity);
        }

        // Test add more then capacity, when the inventory not empty not change the capacity.
        curAvailableCapacity = lts.getAvailableCapacity();
        addMoreThanLtsCapacity(lts);
        assertEquals(curAvailableCapacity, lts.getAvailableCapacity());

        lts.resetInventory(); //prints error message also
        curAvailableCapacity = LTS_CAPACITY;
        // add more then LTS_CAPACITY when the inventory empty doesn't change the capacity
        addMoreThanLtsCapacity(lts); //prints error message also
        assertEquals(lts.getAvailableCapacity(), curAvailableCapacity);
    }

    @Test
    public void testAddItem(){
        int ADDED_SUCCESSFULLY = 0;
        int CANT_ADD = -1;

        LongTermStorage lts = new LongTermStorage();

        // Add all the legal types to the lts, checking if only one can be added to empty lts, if can't
        // because it's too big validating it didn't added.
        int curAvailableCapacity;
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() <= lts.getCapacity()){
                assertEquals(lts.addItem(item, 1), ADDED_SUCCESSFULLY);

                curAvailableCapacity = LTS_CAPACITY - item.getVolume();
                assertEquals(lts.getAvailableCapacity(), curAvailableCapacity);
                assertEquals(lts.getItemCount(item.getType()), 1);
                lts.resetInventory();
            } else {
                assertEquals(lts.addItem(item, 1), CANT_ADD);

                assertEquals(lts.getAvailableCapacity(), LTS_CAPACITY);
                assertEquals(lts.getItemCount(item.getType()), 0);
            }
        }

        //test adding max number of elements that possible than add some more.
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0){
                lts.resetInventory();
                addMaxAvailable(lts, item);

                //test adding item that can't be added because it's volume bigger then available.
                int curAmountOfItem = lts.getItemCount(item.getType());
                assertEquals(lts.addItem(item, 1), CANT_ADD);
                assertEquals(lts.getItemCount(item.getType()), curAmountOfItem);
                assertEquals(lts.addItem(item, 10), CANT_ADD);
                assertEquals(lts.getItemCount(item.getType()), curAmountOfItem);
            }


            //test adding max number of elements, each time one item.
            lts.resetInventory();
            int maxNumberOfItemCapacity = lts.getAvailableCapacity() / item.getVolume();
            for(int i = 0; i < maxNumberOfItemCapacity; ++i){
                assertEquals(lts.addItem(item, 1), ADDED_SUCCESSFULLY);
            }
            //test adding one more of the item
            assertEquals(lts.addItem(item, 1), CANT_ADD);
        }

        lts.resetInventory();
        //test add zero times item to empty lts
        assertEquals(lts.addItem(ALL_LEGAL_ITEMS[0], 0), ADDED_SUCCESSFULLY);
        assertEquals(lts.getItemCount(ALL_LEGAL_ITEMS[0].getType()), 0);

        //test adding various types of items
        curAvailableCapacity = LTS_CAPACITY;
        for(int i = 0; i < SMALL_LEGAL_ITEM_TO_CHECK.length; ++i){
            if((SMALL_LEGAL_ITEM_TO_CHECK[i].getVolume() * i) <= curAvailableCapacity){
                assertEquals(lts.addItem(SMALL_LEGAL_ITEM_TO_CHECK[i], i), ADDED_SUCCESSFULLY);
                assertEquals(lts.getItemCount(SMALL_LEGAL_ITEM_TO_CHECK[i].getType()), i);
                curAvailableCapacity -= SMALL_LEGAL_ITEM_TO_CHECK[i].getVolume() * i;
                assertEquals(lts.getAvailableCapacity(), curAvailableCapacity);
            } else{
                assertEquals(lts.addItem(SMALL_LEGAL_ITEM_TO_CHECK[i], i), CANT_ADD);
                //test no change in capacity
                assertEquals(lts.getAvailableCapacity(), curAvailableCapacity);
                //test no item of this type added at all
                assertEquals(lts.getItemCount(SMALL_LEGAL_ITEM_TO_CHECK[i].getType()), 0);
            }
        }
        //after various types in the lts try add more then possible
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0){
                int numOfAvailableToAdd = lts.getAvailableCapacity() / item.getVolume();
                assertEquals(lts.addItem(item, numOfAvailableToAdd + 1), CANT_ADD);
            }
        }

        lts.resetInventory();
        //test add negative number of elements
        assertEquals(lts.addItem(ALL_LEGAL_ITEMS[0], -1), CANT_ADD);
        assertEquals(lts.addItem(ALL_LEGAL_ITEMS[0], -9), CANT_ADD);
    }

    @Test
    public void testGetItemCount(){
        LongTermStorage lts = new LongTermStorage();
        for(int i = 0; i < ALL_LEGAL_ITEMS.length; ++i){
            if((ALL_LEGAL_ITEMS[i].getVolume() * i) <= lts.getAvailableCapacity()){
                lts.addItem(ALL_LEGAL_ITEMS[i], i);
                assertEquals(lts.getItemCount(ALL_LEGAL_ITEMS[i].getType()), i);
            }
        }

        //not exist item -> return value 0.
        lts.resetInventory();
        assertEquals(lts.getItemCount(ALL_LEGAL_ITEMS[0].getType()), 0);
    }

    @Test
    public void testGetInventory(){
        LongTermStorage lts = new LongTermStorage();

        Map<String, Integer> ltsInventory = lts.getInventory();
        assertNotEquals(ltsInventory, null);
        assertTrue(ltsInventory.isEmpty());

        // test add items to the lts then check if the inventory contain the same amount of keys and the same
        // amount of value for each key.
        int numberOfKeys = 0;
        for(int i = 0; i < SMALL_LEGAL_ITEM_TO_CHECK.length; ++i){
            if((SMALL_LEGAL_ITEM_TO_CHECK[i].getVolume() * i) <= lts.getAvailableCapacity()){
                lts.addItem(SMALL_LEGAL_ITEM_TO_CHECK[i], i);
                assertEquals(ltsInventory.get(SMALL_LEGAL_ITEM_TO_CHECK[i].getType()), Integer.valueOf(i));
                numberOfKeys++;
            }
        }
        //assert the number of keys as needed after adding items.
        assertEquals(lts.getInventory().size(), numberOfKeys);
        //added before 0 times.
        assertEquals(ltsInventory.get(SMALL_LEGAL_ITEM_TO_CHECK[0].getType()), Integer.valueOf(0));
    }

    @Test
    public void testResetInventory(){
        LongTermStorage lts = new LongTermStorage();
        for(Item item : ALL_LEGAL_ITEMS){
            lts.addItem(item, 1);
        }
        //if no item added for some reason (too big items), adding new map manually.
        if(lts.getInventory().size() == 0){
            lts.getInventory().put("testString", 1);
        }

        lts.resetInventory();

        // test the lts actually restated.
        assertEquals(lts.getAvailableCapacity(), lts.getCapacity());
        assertEquals(lts.getInventory().size(), 0);
    }

    /**
     * add more than available in one time
     * @param lts the lts to add for.
     */
    private void addMoreThanLtsCapacity(LongTermStorage lts){
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK){
            if(item.getVolume() > 0){
                int MaxElementsInLtsCapacity = lts.getAvailableCapacity() / item.getVolume();
                lts.addItem(item,MaxElementsInLtsCapacity + 1);
                break;
            }
        }
    }

    /**
     * add the max available elements of type itemToAdd that can added to the lts.
     * @param lts the lts to add the elements to.
     * @param itemToAdd the item to add to the lts.
     */
    private void addMaxAvailable(LongTermStorage lts, Item itemToAdd) {
        int MaxElementsInLtsCapacity = lts.getAvailableCapacity() / itemToAdd.getVolume();
        lts.addItem(itemToAdd, MaxElementsInLtsCapacity);
    }
}
