import oop.ex3.spaceship.Item;
import oop.ex3.spaceship.ItemFactory;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test class for the Locker class.
 *
 * @author Avi Kogan.
 */
public class LockerTest {
    static Item[] ALL_LEGAL_ITEMS;
    static Item[][] CONSTRAINS_PAIRS;
    static Item[][] EMPTY_CONSTRAINS_PAIRS = new Item[0][0];
    static Item[] SMALL_LEGAL_ITEM_TO_CHECK;

    static final int LTS_CAPACITY = 1000;
    static final int BIG_CAPACITY = 100000;
    static final int SMALL_CAPACITY = 10;
    static final int EMPTY_CAPACITY = 0;
    
    @BeforeClass
    public static void createTestObjects(){
        ALL_LEGAL_ITEMS = ItemFactory.createAllLegalItems();
        CONSTRAINS_PAIRS = ItemFactory.getConstraintPairs();
        SMALL_LEGAL_ITEM_TO_CHECK = new Item[3];
        SMALL_LEGAL_ITEM_TO_CHECK[0] = ALL_LEGAL_ITEMS[0];
        SMALL_LEGAL_ITEM_TO_CHECK[1] = ALL_LEGAL_ITEMS[ALL_LEGAL_ITEMS.length / 2];
        SMALL_LEGAL_ITEM_TO_CHECK[2] = ALL_LEGAL_ITEMS[ALL_LEGAL_ITEMS.length - 1];
    }

    @Test
    public void testConstructor(){
        LongTermStorage lts = new LongTermStorage();
        // not empty capacity and not empty constrains
        Locker locker = new Locker(lts, BIG_CAPACITY, CONSTRAINS_PAIRS);
        assertNotNull(locker);
        assertEquals(locker.getCapacity(), BIG_CAPACITY);
        assertEquals(locker.getCapacity(), locker.getAvailableCapacity());
        assertNotNull(locker.getInventory());
        assertEquals(locker.getInventory().size(), 0);

        // not empty capacity and empty constrains
        locker = new Locker(lts, EMPTY_CAPACITY, CONSTRAINS_PAIRS);
        assertNotNull(locker);
        assertEquals(locker.getCapacity(), EMPTY_CAPACITY);
        assertEquals(locker.getCapacity(), locker.getAvailableCapacity());
        assertNotNull(locker.getInventory());
        assertEquals(locker.getInventory().size(), 0);

        // not empty capacity and and empty constrains
        locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        assertNotNull(locker);
        assertEquals(locker.getCapacity(), BIG_CAPACITY);
        assertEquals(locker.getCapacity(), locker.getAvailableCapacity());
        assertNotNull(locker.getInventory());
        assertEquals(locker.getInventory().size(), 0);

        // not empty capacity and and empty constrains
        locker = new Locker(lts, EMPTY_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        assertNotNull(locker);
        assertEquals(locker.getCapacity(), EMPTY_CAPACITY);
        assertEquals(locker.getCapacity(), locker.getAvailableCapacity());
        assertNotNull(locker.getInventory());
        assertEquals(locker.getInventory().size(), 0);
    }

    @Test
    public void testGetCapacity(){
        LongTermStorage lts = new LongTermStorage();
        Locker locker = new Locker(lts, BIG_CAPACITY, CONSTRAINS_PAIRS);
        assertEquals(locker.getCapacity(), BIG_CAPACITY);

        // try adding all the legal items
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() <= locker.getAvailableCapacity()){
                locker.addItem(item, 1);
                //test no change in capacity after adding item.
                assertEquals(locker.getCapacity(), BIG_CAPACITY);
                locker.removeItem(item, 1);
                //test no change in capacity after removing item.
                assertEquals(locker.getCapacity(), BIG_CAPACITY);
            }
        }

        // check empty capacity (also checked if more then capacity added -> no change in capacity).
        locker = new Locker(lts, EMPTY_CAPACITY, CONSTRAINS_PAIRS);
        assertEquals(locker.getCapacity(), EMPTY_CAPACITY);
        locker.addItem(ALL_LEGAL_ITEMS[0], 1);
        assertEquals(locker.getCapacity(), EMPTY_CAPACITY);
    }

    @Test
    // one test to add item with less then 50% capacity and available capacity is ok.
    public void testGetAvailableCapacity() {
        // --- test adding with empty constrains array ----
        LongTermStorage lts = new LongTermStorage();
        Locker locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        assertEquals(locker.getAvailableCapacity(), BIG_CAPACITY);

        //test add negative number of items not added
        for(int i = 1; i < SMALL_LEGAL_ITEM_TO_CHECK.length; ++i){
            locker.addItem(SMALL_LEGAL_ITEM_TO_CHECK[i],  i * -1);
            assertEquals(locker.getAvailableCapacity(), locker.getCapacity());
        }

        // Test add some legal types to the locker, each time one - checking if after one added the
        // available capacity updated.
        // * because locker capacity is bigger then LTS_CAPACITY, each item should added to the locker if it's
        // volume <= BIG_CAPACITY / 2.
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK) {
            if (item.getVolume() <= BIG_CAPACITY / 2) {
                locker.addItem(item, 1);
                assertEquals(locker.getItemCount(item.getType()), 1);
                assertEquals((locker.getCapacity() - locker.getAvailableCapacity()), item.getVolume());
                assertEquals(lts.getAvailableCapacity(), LTS_CAPACITY); //validate no copy in lts
                locker.removeItem(item, 1);
            }
        }

        lts.resetInventory();
        // test add more then one item -> total less then 50% capacity, capacity updated successfully.
        int numOfAddedElements = 3;
        int curAvailableCapacity = locker.getCapacity();
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() * numOfAddedElements <= BIG_CAPACITY / 2 &&
               item.getVolume() * numOfAddedElements <= locker.getAvailableCapacity()){
                locker.addItem(item, numOfAddedElements);
                curAvailableCapacity -= numOfAddedElements * item.getVolume();
                assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                break;
            }
        }

        lts.resetInventory();
        // test add items in one time more then 50% and <= of locker's available capacity -> all should be
        // added to the locker, some of them should move to lts.
        locker = new Locker(lts, LTS_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && item.getVolume() * 2 <= LTS_CAPACITY){
                int numOfElementsToFillHalf = (LTS_CAPACITY / 2) / item.getVolume();
                locker.addItem(item, numOfElementsToFillHalf + 1);
                int numOfElementsShouldStay = (LTS_CAPACITY / 5) / item.getVolume();
                int lockerAvailableCapacity = LTS_CAPACITY - numOfElementsShouldStay * item.getVolume();
                assertEquals(lockerAvailableCapacity, locker.getAvailableCapacity());

                int numOfElementsInLts = numOfElementsToFillHalf + 1 - numOfElementsShouldStay;
                int ltsAvailableCapacity = LTS_CAPACITY - numOfElementsInLts * item.getVolume();
                assertEquals(ltsAvailableCapacity, lts.getAvailableCapacity());
                break;
            }
        }

        lts.resetInventory();
        locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        //Test adding item multiple times, until it more then 50% of capacity, check no item moved to the
        // lts.
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && (item.getVolume() * 3) <= (BIG_CAPACITY / 5)){
                locker.addItem(item, 1);
                curAvailableCapacity = BIG_CAPACITY - item.getVolume();
                assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                locker.addItem(item, 2);
                curAvailableCapacity -= item.getVolume() * 2;
                assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                assertEquals(lts.getAvailableCapacity(), lts.getCapacity()); // no change in tks
                break;
            }
        }

        lts.resetInventory();
        // add item with volume <= then 20% of locker capacity, each time one until pass the 50% of capacity.
        // check if the required item number moved to lts, and the max number that smaller of 20% should stay.
        locker = new Locker(lts, SMALL_CAPACITY, CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            // check the volume of the item not 0 and <= then 20% of the capacity.
            if(item.getVolume() > 0 && item.getVolume() <= SMALL_CAPACITY / 5){
                // fill the locker with max item number to get half of locker volume.
                int numOfElementsToFillHalf = (SMALL_CAPACITY / 2) / item.getVolume();
                for(int i = 0; i < numOfElementsToFillHalf; ++i){
                    locker.addItem(item, 1);
                }
                curAvailableCapacity = SMALL_CAPACITY - numOfElementsToFillHalf * item.getVolume();
                assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                //add item that force move to lts.
                locker.addItem(item, 1);

                // check if required number of the the item moved to lts, and maximal number that <= then
                // 20% of capacity stayed.
                int numOfElementsShouldStay = (SMALL_CAPACITY / 5) / item.getVolume();
                curAvailableCapacity = SMALL_CAPACITY - numOfElementsShouldStay * item.getVolume();
                assertEquals(curAvailableCapacity, locker.getAvailableCapacity());

                int numOfElementsInLts = numOfElementsToFillHalf + 1 - numOfElementsShouldStay;
                int ltsAvailableCapacity = LTS_CAPACITY - numOfElementsInLts * item.getVolume();
                assertEquals(ltsAvailableCapacity, lts.getAvailableCapacity());
                break;
            }
        }

        lts.resetInventory();
        // Test adding in one time less items then LTS_CAPACITY and more then locker capacity, tested with
        // locker capacity smaller than LTS_CAPACITY.
        locker = new Locker(lts, SMALL_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK){
            if(item.getVolume() > 0){
                int maxNumberOfElementsAddToLts = LTS_CAPACITY / item.getVolume();
                locker.addItem(item, maxNumberOfElementsAddToLts);
                assertEquals(locker.getAvailableCapacity(), locker.getCapacity());
                assertEquals(lts.getAvailableCapacity(), lts.getCapacity());
            }
        }

        lts.resetInventory();
        // Test adding in second time more items than available capacity but less then LTS_CAPACITY.
        locker = new Locker(lts, LTS_CAPACITY - 1, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK){
            if(item.getVolume() > 0 && item.getVolume() <= (LTS_CAPACITY - 1) / 2 &&
               item.getVolume() <= locker.getAvailableCapacity()){
                int maxNumberOfElementsAddToLts = LTS_CAPACITY / item.getVolume();
                locker.addItem(item, 1); //added to the locker
                locker.addItem(item, maxNumberOfElementsAddToLts);
                curAvailableCapacity = LTS_CAPACITY - 1 -item.getVolume();
                assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                assertEquals(lts.getAvailableCapacity(), lts.getCapacity());
                break;
            }
        }

        lts.resetInventory();
        //Test add item to the locker until there is no more place in LTS, adding the maximal number minus
        // one of the item, then add 1 to force move to lts, then add again to get error.
        // * Init the locker with LTS_CAPACITY to make the test calculations easier.
        locker = new Locker(lts, LTS_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && item.getVolume() <= (LTS_CAPACITY / 2));
            int maxNumberOfElementsAddToLts = LTS_CAPACITY / item.getVolume();
            locker.addItem(item, maxNumberOfElementsAddToLts - 1); // all added to locker
            locker.addItem(item, 1); // leave 20% in the locker all the rest moved to lts.
            curAvailableCapacity = locker.getAvailableCapacity();
            int ltsAvailableCapacity = lts.getAvailableCapacity();
            // trying to add 80% of capacity, will try to move some to LTS but there is no place for it.
            int numOfItemInLocker = locker.getItemCount(item.getType());
            int curNumberOfElementsAdd = (int)(LTS_CAPACITY * 0.8) / item.getVolume();
            locker.addItem(item, curNumberOfElementsAdd);
            assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
            assertEquals(lts.getAvailableCapacity(), ltsAvailableCapacity);
            break;
        }

        // Test with constrains.
        if(CONSTRAINS_PAIRS.length != 0){
            lts.resetInventory();
            // adding contradict items, create locker with the maximal size between the constrains item -
            // called item A, add A that added to the locker, than add another A make both of them move to
            // lts, then adding  item B  violating constrains with item A, but should added because all
            // instances of type A in LTS.
            for(Item[] constrainsPair : CONSTRAINS_PAIRS){
                if(constrainsPair[0].getVolume() * 2 < LTS_CAPACITY &&
                   constrainsPair[1].getVolume() * 2 < LTS_CAPACITY){

                    Item constrainsItem1 = constrainsPair[0];
                    Item constrainsItem2 = constrainsPair[1];
                    int maxVolume = Math.max(constrainsItem1.getVolume(), constrainsItem2.getVolume());
                    locker = new Locker(lts, maxVolume  * 2, CONSTRAINS_PAIRS);
                    if(constrainsItem1.getVolume() == maxVolume){
                        //constrainsItem1 added to locker
                        locker.addItem(constrainsItem1, 1);
                        //two instances of constrainsItem1 should move to lts
                        locker.addItem(constrainsItem1, 1);
                        int ltsAvailableCapacity = LTS_CAPACITY - constrainsItem1.getVolume() * 2;
                        assertEquals(ltsAvailableCapacity, lts.getAvailableCapacity());
                        //constrainsItem2 should stay in locker
                        locker.addItem(constrainsItem2, 1);
                        curAvailableCapacity = maxVolume  * 2 - constrainsItem2.getVolume();
                        assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                    }else{
                        //constrainsItem2 added to locker
                        locker.addItem(constrainsItem2, 1);
                        //two instances of constrainsItem2 should move to lts
                        locker.addItem(constrainsItem2, 1);
                        int ltsAvailableCapacity = LTS_CAPACITY - constrainsItem2.getVolume() * 2;
                        assertEquals(ltsAvailableCapacity, lts.getAvailableCapacity());
                        //constrainsItem1 should stay in locker
                        locker.addItem(constrainsItem1, 1);
                        curAvailableCapacity = maxVolume  * 2 - constrainsItem1.getVolume();
                        assertEquals(curAvailableCapacity, locker.getAvailableCapacity());
                    }
                }
            }

            Item constrainsItem1 = CONSTRAINS_PAIRS[0][0];
            Item constrainsItem2 = CONSTRAINS_PAIRS[0][1];
            int maxVolume = Math.max(constrainsItem1.getVolume(), constrainsItem2.getVolume());

            lts.resetInventory();
            // Test add one item to the locker, then try to add item violating constrains.
            locker = new Locker(lts, maxVolume * 2, CONSTRAINS_PAIRS);
            if(constrainsItem1.getVolume() == maxVolume){
                locker.addItem(constrainsItem1, 1);
                locker.addItem(constrainsItem2, 1); //didn't added
                assertEquals(constrainsItem1.getVolume(), locker.getAvailableCapacity());
                assertEquals(lts.getAvailableCapacity(), lts.getCapacity());
            }else{
                locker.addItem(constrainsItem2, 1);
                locker.addItem(constrainsItem1, 1); //didn't added
                assertEquals(constrainsItem2.getVolume(), locker.getAvailableCapacity());
                assertEquals(lts.getAvailableCapacity(), lts.getCapacity());
            }
        }

        // ------ test empty capacity ---------
        locker = new Locker(lts, EMPTY_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        assertEquals(locker.getAvailableCapacity(), EMPTY_CAPACITY);

        locker.addItem(ALL_LEGAL_ITEMS[0], 1);
        //test no change in available capacity after trying to add items that can't be added.
        assertEquals(locker.getAvailableCapacity(), EMPTY_CAPACITY);

        locker.addItem(ALL_LEGAL_ITEMS[ALL_LEGAL_ITEMS.length - 1], ALL_LEGAL_ITEMS.length - 1);
        //test no change in available capacity after trying to add items that can't be added.
        assertEquals(locker.getAvailableCapacity(), EMPTY_CAPACITY);

    }

    @Test
    public void testAddItem(){
        int ADDED_SUCCESSFULLY = 0;
        int ADDED_BUT_ITEMS_MOVED_TO_LTS = 1;
        int CANT_ADD = -1;
        int CONSTRAINS_ADD_ERROR = -2;

        // --- test adding with empty constrains array ----
        LongTermStorage lts = new LongTermStorage();
        Locker locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);

        //test add negative number of items not added
        for(int i = 1; i < SMALL_LEGAL_ITEM_TO_CHECK.length; ++i){
            assertEquals(locker.addItem(SMALL_LEGAL_ITEM_TO_CHECK[i],  i * -1), CANT_ADD);
        }


        // Test add all the legal types to the locker, each time one - checking if only one can be added to
        // empty locker.
        // * because locker capacity is bigger then LTS_CAPACITY, each item should added to the locker if it's
        // volume <= BIG_CAPACITY / 2.
        for(Item item : ALL_LEGAL_ITEMS) {
            if (item.getVolume() <= BIG_CAPACITY / 2) {
                assertEquals(locker.addItem(item, 1), ADDED_SUCCESSFULLY);
                assertEquals(locker.getItemCount(item.getType()), 1);
                assertEquals((locker.getCapacity() - locker.getAvailableCapacity()), item.getVolume());
                assertEquals(lts.getItemCount(item.getType()), 0); //validate no copy in lts
                locker.removeItem(item, 1);
            }
        }

        lts.resetInventory();
        // test add more then one item -> total less then 50% capacity -> checked if added successfully.
        int numOfAddedElements = 3;
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK){
            if(item.getVolume() * numOfAddedElements <= BIG_CAPACITY / 2 &&
               item.getVolume() * numOfAddedElements <= locker.getAvailableCapacity()){
                assertEquals(locker.addItem(item, numOfAddedElements), ADDED_SUCCESSFULLY);
                assertEquals(locker.getItemCount(item.getType()), numOfAddedElements);
            }
        }

        lts.resetInventory();
        // test add items in one time more then 50% and <= of locker's available capacity -> all should be
        // added to the locker, some of them should move to lts.
        locker = new Locker(lts, LTS_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && item.getVolume() * 2 <= LTS_CAPACITY){
                int numOfElementsToFillHalf = (LTS_CAPACITY / 2) / item.getVolume();
                assertEquals(locker.addItem(item, numOfElementsToFillHalf + 1), ADDED_BUT_ITEMS_MOVED_TO_LTS);
                int numOfElementsShouldStay = (LTS_CAPACITY / 5) / item.getVolume();
                int numOfElementsInLts = numOfElementsToFillHalf + 1 - numOfElementsShouldStay;
                assertEquals(locker.getItemCount(item.getType()), numOfElementsShouldStay);
                assertEquals(lts.getItemCount(item.getType()), numOfElementsInLts);
                break;
            }
        }

        lts.resetInventory();
        locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        //Test adding item multiple times, until it more then 50% of capacity, check no item moved to the
        // lts.
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && (item.getVolume() * 3) <= (BIG_CAPACITY / 5)){
                assertEquals(locker.addItem(item, 1), ADDED_SUCCESSFULLY);
                assertEquals(locker.addItem(item, 2), ADDED_SUCCESSFULLY);
                assertEquals(locker.getItemCount(item.getType()), 3);
                assertEquals(lts.getAvailableCapacity(), lts.getCapacity());
                break;
            }
        }

        lts.resetInventory();
        // add item with volume <= then 20% of locker capacity, each time one until pass the 50% of capacity.
        // check if the required item number moved to lts, and the max number that smaller of 20% should stay.
        locker = new Locker(lts, SMALL_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            // check the volume of the item not 0 and <= then 20% of the capacity.
            if(item.getVolume() > 0 && item.getVolume() <= SMALL_CAPACITY / 5){
                // fill the locker with max item number to get half of locker volume.
                int numOfElementsToFillHalf = (SMALL_CAPACITY / 2) / item.getVolume();
                for(int i = 0; i < numOfElementsToFillHalf; ++i){
                    assertEquals(locker.addItem(item, 1), ADDED_SUCCESSFULLY);
                }
                assertEquals(locker.addItem(item, 1), ADDED_BUT_ITEMS_MOVED_TO_LTS);

                // check if required number of the the item moved to lts, and maximal number that <= then
                // 20% of capacity stayed.
                int numOfElementsShouldStay = (SMALL_CAPACITY / 5) / item.getVolume();
                int numOfElementsInLts = numOfElementsToFillHalf + 1 - numOfElementsShouldStay;
                assertEquals(locker.getItemCount(item.getType()), numOfElementsShouldStay);
                assertEquals(lts.getItemCount(item.getType()), numOfElementsInLts);
                break;
            }
        }

        lts.resetInventory();
        // Test adding in one time less items then LTS_CAPACITY and more then locker capacity, with locker
        // capacity smaller than LTS_CAPACITY check if -1 returned.
        locker = new Locker(lts, SMALL_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK){
            if(item.getVolume() > 0){
                int maxNumberOfElementsAddToLts = LTS_CAPACITY / item.getVolume();
                assertEquals(locker.addItem(item, maxNumberOfElementsAddToLts), CANT_ADD);
            }
        }

        lts.resetInventory();
        // Test adding in second time more items than available capacity but less then LTS_CAPACITY, check if
        // -1 returned.
        locker = new Locker(lts, LTS_CAPACITY - 1, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : SMALL_LEGAL_ITEM_TO_CHECK){
            if(item.getVolume() > 0 && item.getVolume() <= (LTS_CAPACITY - 1) / 2 &&
                item.getVolume() <= locker.getAvailableCapacity()){
                int maxNumberOfElementsAddToLts = LTS_CAPACITY / item.getVolume();
                assertEquals(locker.addItem(item, 1), ADDED_SUCCESSFULLY); //added to the locker
                assertEquals(locker.addItem(item, maxNumberOfElementsAddToLts), CANT_ADD);
                assertEquals(locker.getItemCount(item.getType()), 1);
            }
        }

        lts.resetInventory();
        //Test add item to the locker until there is no more place in LTS and get CANT_ADD, adding the
        // maximal number minus one of the item, then add 1 to force move to lts, then add again to get error.
        // Init the locker with LTS_CAPACITY to make the test calculations easier.
        locker = new Locker(lts, LTS_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && item.getVolume() <= (LTS_CAPACITY / 2));
            int maxNumberOfElementsAddToLts = LTS_CAPACITY / item.getVolume();
            locker.addItem(item, maxNumberOfElementsAddToLts - 1); // all added to locker
            locker.addItem(item, 1); // leave 20% in the locker all the rest moved to lts.
            // trying to add 80% of capacity, will try to move some to LTS but there is no place for it.
            int numOfItemInLocker = locker.getItemCount(item.getType());
            int curNumberOfElementsAdd = (int)(LTS_CAPACITY * 0.8) / item.getVolume();
            assertEquals(locker.addItem(item, curNumberOfElementsAdd), CANT_ADD);
            assertEquals(locker.getItemCount(item.getType()), numOfItemInLocker); // no change in the locker
            break;
        }



        // ------ Test with constrains -------

        if(CONSTRAINS_PAIRS.length != 0){
            lts.resetInventory();
            // adding contradict items, create locker with the maximal size between the constrains item -
            // called item A, add A that added to the locker, than add another A make both of them move to
            // lts, then adding  item B  violating constrains with item A, but should added because all
            // instances of type A in LTS.
            for(Item[] constrainsPair : CONSTRAINS_PAIRS){
                if(constrainsPair[0].getVolume() * 2 < LTS_CAPACITY &&
                   constrainsPair[1].getVolume() * 2 < LTS_CAPACITY){

                    Item constrainsItem1 = constrainsPair[0];
                    Item constrainsItem2 = constrainsPair[1];
                    int maxVolume = Math.max(constrainsItem1.getVolume(), constrainsItem2.getVolume());
                    locker = new Locker(lts, maxVolume  * 2, CONSTRAINS_PAIRS);
                    if(constrainsItem1.getVolume() == maxVolume){
                        //constrainsItem1 added to locker
                        assertEquals(locker.addItem(constrainsItem1, 1), ADDED_SUCCESSFULLY);
                        //two instances of constrainsItem1 should move to lts
                        assertEquals(locker.addItem(constrainsItem1, 1), ADDED_BUT_ITEMS_MOVED_TO_LTS);
                        //constrainsItem2 should stay in locker
                        assertEquals(locker.addItem(constrainsItem2, 1), ADDED_SUCCESSFULLY);
                    }else{
                        //constrainsItem2 added to locker
                        assertEquals(locker.addItem(constrainsItem2, 1), ADDED_SUCCESSFULLY);
                        //two instances of constrainsItem2 should move to lts
                        assertEquals(locker.addItem(constrainsItem2, 1), ADDED_BUT_ITEMS_MOVED_TO_LTS);
                        //constrainsItem1 should stay in locker
                        assertEquals(locker.addItem(constrainsItem1, 1), ADDED_SUCCESSFULLY);
                    }
                }
            }

            Item constrainsItem1 = CONSTRAINS_PAIRS[0][0];
            Item constrainsItem2 = CONSTRAINS_PAIRS[0][1];
            int maxVolume = Math.max(constrainsItem1.getVolume(), constrainsItem2.getVolume());

            // Test add one item to the locker, then try to add item violating constrains.
            locker = new Locker(lts, maxVolume * 2, CONSTRAINS_PAIRS);
            if(constrainsItem1.getVolume() == maxVolume){
                assertEquals(locker.addItem(constrainsItem1, 1), ADDED_SUCCESSFULLY);
                assertEquals(locker.addItem(constrainsItem2, 1), CONSTRAINS_ADD_ERROR);
            }else{
                assertEquals(locker.addItem(constrainsItem2, 1), ADDED_SUCCESSFULLY);
                assertEquals(locker.addItem(constrainsItem1, 1), CONSTRAINS_ADD_ERROR);
            }
        }
    }

    @Test
    public void testRemoveItem(){
        int REMOVED_SUCCESSFULLY = 0;
        int REMOVED_FAILED = -1;

        LongTermStorage lts = new LongTermStorage();
        Locker locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);

        // Test add multiple items less then 50% capacity and remove part of them, then all of them
        int numToAdd = 4;
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() * numToAdd <= BIG_CAPACITY / 2){
                locker.addItem(item, numToAdd);
                assertEquals(locker.removeItem(item, 1), REMOVED_SUCCESSFULLY); // remove one out of 4
                assertEquals(locker.getItemCount(item.getType()), 3);
                assertEquals(locker.removeItem(item, 2), REMOVED_SUCCESSFULLY); // remove two out of 3
                assertEquals(locker.getItemCount(item.getType()), 1);
                assertEquals(locker.removeItem(item, 1), REMOVED_SUCCESSFULLY); // remove the last one
                assertEquals(locker.getItemCount(item.getType()), 0);
                break;
            }
        }

        lts.resetInventory();
        // Test add item more then 50% of capacity, then delete the stayed 20% -> check no more of the item
        // in the locker.
        locker = new Locker(lts, SMALL_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && item.getVolume() * 2 < SMALL_CAPACITY / 2){
                int numOfElementsToFillHalf = (SMALL_CAPACITY / 2) / item.getVolume();
                locker.addItem(item, numOfElementsToFillHalf + 1);
                int numberOfElementsShouldStay = (SMALL_CAPACITY / 5) / item.getVolume();
                int numberMovedToLts = numOfElementsToFillHalf + 1 - numberOfElementsShouldStay;

                // remove more elements then actual no change in lts and locker.
                assertEquals(locker.removeItem(item, numberOfElementsShouldStay + 1), REMOVED_FAILED);
                assertEquals(lts.getItemCount(item.getType()), numberMovedToLts);
                assertEquals(locker.getItemCount(item.getType()), numberOfElementsShouldStay);

                // remove the actual element number in locker, no change in the lts.
                assertEquals(locker.removeItem(item, numberOfElementsShouldStay), REMOVED_SUCCESSFULLY);
                assertEquals(lts.getItemCount(item.getType()), numberMovedToLts);
                assertEquals(locker.getItemCount(item.getType()), 0);
            }
        }
    }

    @Test
    public void testGetItemCount(){
        LongTermStorage lts = new LongTermStorage();
        Locker locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);

        // Test add various of type, each one of other number.
        for(int i = 0; i < ALL_LEGAL_ITEMS.length; ++i){
            if(ALL_LEGAL_ITEMS[i].getVolume() * i < locker.getAvailableCapacity()){
                locker.addItem(ALL_LEGAL_ITEMS[i], i);
                assertEquals(locker.getItemCount(ALL_LEGAL_ITEMS[i].getType()), i);
            }
        }

        lts.resetInventory();
        // Test add item more then 50% of capacity, check just 20% stayed,
        // then delete the stayed 20% -> check no more of the item in the locker.
        locker = new Locker(lts, SMALL_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0 && item.getVolume() < BIG_CAPACITY / 2){
                int numOfElementsToFillHalf = (SMALL_CAPACITY / 2) / item.getVolume();
                locker.addItem(item, numOfElementsToFillHalf + 1);
                int numberOfElementsShouldStay = (SMALL_CAPACITY / 5) / item.getVolume();
                assertEquals(locker.getItemCount(item.getType()), numberOfElementsShouldStay);

                // remove the actual element number in locker.
                locker.removeItem(item, numberOfElementsShouldStay);
                assertEquals(locker.getItemCount(item.getType()), 0);
            }
        }

        lts.resetInventory();
        // Test add with constrains.
        if(CONSTRAINS_PAIRS.length != 0){
            Item constrainsItem1 = CONSTRAINS_PAIRS[0][0];
            Item constrainsItem2 = CONSTRAINS_PAIRS[0][1];
            int maxVolume = Math.max(constrainsItem1.getVolume(), constrainsItem2.getVolume());

            // Test add one item to the locker, then try to add item violating constrains.
            locker = new Locker(lts, maxVolume * 2, CONSTRAINS_PAIRS);
            locker.addItem(constrainsItem1, 1);
            assertEquals(locker.getItemCount(constrainsItem1.getType()), 1);
            locker.addItem(constrainsItem2, 1);
            assertEquals(locker.getItemCount(constrainsItem2.getType()), 0);

            if(constrainsItem1.getVolume() > 0 && constrainsItem2.getVolume() > 0){
            // Test adding item A that moved to lts, should be zero times in the locker,
            // then adding item B violating constrains with item A, but should added because A in LTS.
                locker = new Locker(lts, maxVolume * 2, CONSTRAINS_PAIRS);
                if(maxVolume * 2 <= LTS_CAPACITY){
                    if(constrainsItem1.getVolume() == maxVolume) {
                        locker.addItem(constrainsItem1, 2); //both added to LTS
                        assertEquals(locker.getItemCount(constrainsItem1.getType()), 0);

                        locker.addItem(constrainsItem2, 1);
                        assertEquals(locker.getItemCount(constrainsItem2.getType()), 1);
                    }else{
                        locker.addItem(constrainsItem2, 2);
                        assertEquals(locker.getItemCount(constrainsItem2.getType()), 0);

                        locker.addItem(constrainsItem1, 1);
                        assertEquals(locker.getItemCount(constrainsItem1.getType()), 1);
                    }
                }
            }
        }
    }

    @Test
    public void testGetInventory(){
        LongTermStorage lts = new LongTermStorage();
        Locker locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);


        assertNotNull(locker.getInventory());
        assertTrue(locker.getInventory().isEmpty());

        // test add items to the locker then check if the inventory contain the same value for each key,
        // amount of value for each key.
        int numberOfKeys = 0;
        for(int i = 0; i < ALL_LEGAL_ITEMS.length; ++i){
            if(ALL_LEGAL_ITEMS[i].getVolume() * i <= locker.getAvailableCapacity() &&
               ALL_LEGAL_ITEMS[i].getVolume() * i <= BIG_CAPACITY / 2){

                locker.addItem(ALL_LEGAL_ITEMS[i], i);
                assertEquals(locker.getInventory().get(ALL_LEGAL_ITEMS[i].getType()), Integer.valueOf(i));
                numberOfKeys++;
            }
        }
        assertEquals(locker.getInventory().size(), numberOfKeys);

        lts.resetInventory();
        // test adding elements to the locker, then remove them and check if the inventory updated.
        locker = new Locker(lts, BIG_CAPACITY, EMPTY_CONSTRAINS_PAIRS);
        for(int i = 0; i < ALL_LEGAL_ITEMS.length; ++i){
            if(ALL_LEGAL_ITEMS[i].getVolume() * (i + 1) <= locker.getAvailableCapacity() &&
               ALL_LEGAL_ITEMS[i].getVolume() * (i + 1) <= BIG_CAPACITY / 2){

                locker.addItem(ALL_LEGAL_ITEMS[i], i + 1);
                locker.removeItem(ALL_LEGAL_ITEMS[i], i);
                assertEquals(locker.getInventory().get(ALL_LEGAL_ITEMS[i].getType()), Integer.valueOf(1));

                locker.removeItem(ALL_LEGAL_ITEMS[i], 1); //remove the last element
                assertNull(locker.getInventory().get(ALL_LEGAL_ITEMS[i].getType()));
            }
        }

        lts.resetInventory();
        // test add more then capacity -> nothing added to the inventory.
        for(Item item : ALL_LEGAL_ITEMS){
            if(item.getVolume() > 0){
                locker = new Locker(lts, item.getVolume() * 4, EMPTY_CONSTRAINS_PAIRS);
                locker.addItem(item, 5);
                assertNull(locker.getInventory().get(item.getType()));
                break;
            }
        }

        lts.resetInventory();
        // test add with constrains
        if(CONSTRAINS_PAIRS.length != 0){
            Item constrainsItem1 = CONSTRAINS_PAIRS[0][0];
            Item constrainsItem2 = CONSTRAINS_PAIRS[0][1];
            int maxVolume = Math.max(constrainsItem1.getVolume(), constrainsItem2.getVolume());

            // Test add one item to the locker, then try to add item violating constrains.
            locker = new Locker(lts, maxVolume * 2, CONSTRAINS_PAIRS);
            locker.addItem(constrainsItem1, 1);
            locker.addItem(constrainsItem2, 1);
            assertEquals(locker.getInventory().get(constrainsItem1.getType()), Integer.valueOf(1));
            assertNull(locker.getInventory().get(constrainsItem2.getType()));

            lts.resetInventory();
            if(constrainsItem1.getVolume() > 0 && constrainsItem2.getVolume() > 0) {
                // Test adding item A that moved to lts, should be zero times in the locker,
                // then adding item B violating constrains with item A,but should added because A in LTS.
                locker = new Locker(lts, maxVolume * 3, CONSTRAINS_PAIRS);
                if (maxVolume * 2 <= LTS_CAPACITY) {
                    if(constrainsItem1.getVolume() == maxVolume) {
                        locker.addItem(constrainsItem1, 2); //both added to LTS
                        assertNull(locker.getInventory().get(constrainsItem1.getType()));

                        locker.addItem(constrainsItem2, 1);
                        assertEquals(locker.getInventory().get(constrainsItem2.getType()), Integer.valueOf(1));
                    } else {
                        locker.addItem(constrainsItem2, 3); //both added to LTS
                        assertNull(locker.getInventory().get(constrainsItem2.getType()));

                        locker.addItem(constrainsItem1, 1);
                        assertEquals(locker.getInventory().get(constrainsItem1.getType()), Integer.valueOf(1));
                    }
                }
            }
        }
    }
}
