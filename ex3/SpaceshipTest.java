import oop.ex3.spaceship.Item;
import oop.ex3.spaceship.ItemFactory;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for the Spaceship class.
 *
 * @author Avi Kogan.
 */
public class SpaceshipTest {
    static Item[] ALL_LEGAL_ITEMS;
    static Item[][] CONSTRAINS_PAIRS;
    static Item[][] EMPTY_CONSTRAINS_PAIRS = new Item[0][0];

    static int[] EMPTY_CREW_ID = new int[0];

    static final int SMALL_CREW_ID_SIZE = 15; //not less then 2
    static int[] SMALL_CREW_ID = new int[SMALL_CREW_ID_SIZE];

    static final int[] CREW_IDS_WITH_NEGATIVE_IDS = {-2, -1, 0, 1};

    static final int BIG_CREW_ID_SIZE = 15;
    static int[] BIG_CREW_ID = new int[BIG_CREW_ID_SIZE];

    static final int BIG_NUM_OF_LOCKERS = 1000;
    static final int SMALL_NUM_OF_LOCKERS = 10;
    static final int EMPTY_NUM_OF_LOCKERS = 0;

    static final int SMALL_LOCKER_CAPACITY = 10;
    static final int BIG_LOCKER_CAPACITY = 1000;

    @BeforeClass
    public static void createTestObjects(){
        ALL_LEGAL_ITEMS = ItemFactory.createAllLegalItems();
        CONSTRAINS_PAIRS = ItemFactory.getConstraintPairs();
        for(int i = 0; i < SMALL_CREW_ID_SIZE; ++i){
            SMALL_CREW_ID[i] = i * i;
        }
        for(int i = 0; i < BIG_CREW_ID_SIZE; ++i){
            BIG_CREW_ID[i] = i * i;
        }
    }

    private void testConstructorHelper(Spaceship curSpaceship, int[] curCrewId, int numOfLockers){
        Locker[] EMPTY_LOCKERS = new Locker[numOfLockers];

        assertNotNull(curSpaceship);
        assertNotNull(curSpaceship.getLongTermStorage());
        assertEquals(curCrewId, curSpaceship.getCrewIDs());
        assertArrayEquals(curSpaceship.getLockers(), EMPTY_LOCKERS);
    }

    @Test
    public void testConstructor(){
        //Test regular parameters
        Spaceship spaceShip = new Spaceship("spaceShipTest", SMALL_CREW_ID, BIG_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        testConstructorHelper(spaceShip, SMALL_CREW_ID, BIG_NUM_OF_LOCKERS);

        //Test with EMPTY_CREW_ID
        spaceShip = new Spaceship("spaceShipTest",EMPTY_CREW_ID, BIG_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        testConstructorHelper(spaceShip, EMPTY_CREW_ID, BIG_NUM_OF_LOCKERS);

        //Test with EMPTY_CREW_ID and emptyNumOfLockers
        spaceShip = new Spaceship("spaceShipTest",EMPTY_CREW_ID, EMPTY_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        testConstructorHelper(spaceShip, EMPTY_CREW_ID, EMPTY_NUM_OF_LOCKERS);

        //Test with EMPTY_CREW_ID and emptyNumOfLockers and EMPTY_CONSTRAINS_PAIRS
        spaceShip = new Spaceship("spaceShipTest",EMPTY_CREW_ID, EMPTY_NUM_OF_LOCKERS, EMPTY_CONSTRAINS_PAIRS);
        testConstructorHelper(spaceShip, EMPTY_CREW_ID, EMPTY_NUM_OF_LOCKERS);

        //Test with negative crewIds
        spaceShip = new Spaceship("spaceShipTest", CREW_IDS_WITH_NEGATIVE_IDS, EMPTY_NUM_OF_LOCKERS, EMPTY_CONSTRAINS_PAIRS);
        testConstructorHelper(spaceShip, CREW_IDS_WITH_NEGATIVE_IDS, EMPTY_NUM_OF_LOCKERS);
    }

    @Test
    public void testGetLongTermStorage(){
        Spaceship spaceship1 = new Spaceship("spaceship1", SMALL_CREW_ID, SMALL_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        //Test empty lts
        LongTermStorage lts = spaceship1.getLongTermStorage();
        assertEquals(lts.getAvailableCapacity(), lts.getCapacity());

        //Test added to some locker more then it's capacity, so some required capacity in lts.
        spaceship1.createLocker(SMALL_CREW_ID[0], SMALL_LOCKER_CAPACITY);
        for(Item item : ALL_LEGAL_ITEMS){
            int itemVolume = item.getVolume();
            if(itemVolume > 0 && itemVolume <= SMALL_LOCKER_CAPACITY){
                Locker[] lockers = spaceship1.getLockers();
                assertEquals(lts.getAvailableCapacity(), lts.getAvailableCapacity());
                int numOfElementsToFillHalf = (SMALL_LOCKER_CAPACITY / 2) / item.getVolume();
                lockers[0].addItem(item, numOfElementsToFillHalf + 1);
                assertNotEquals(lts.getAvailableCapacity(), lts.getCapacity()); //change in lts.
                break;
            }
        }
    }

    @Test
    public void testCreateLocker(){
        int CREATE_SUCCESS = 0;
        int INVALID_ID = -1;
        int INVALID_CAPACITY = -2;
        int MAX_LOCKERS_CREATED = -3;

        Spaceship spaceship1 = new Spaceship("spaceship1", SMALL_CREW_ID, SMALL_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        Spaceship spaceship2 = new Spaceship("spaceship2", SMALL_CREW_ID, SMALL_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        Locker[] emptyLocker = spaceship1.getLockers();
        //Test create with valid Id
        assertEquals(spaceship1.createLocker(SMALL_CREW_ID[0], SMALL_LOCKER_CAPACITY), CREATE_SUCCESS);
        assertEquals(spaceship1.createLocker(SMALL_CREW_ID[SMALL_CREW_ID_SIZE - 1], SMALL_LOCKER_CAPACITY),
                     CREATE_SUCCESS);
        assertNotNull(spaceship1.getLockers()[0]); //the first created locker
        assertNotNull(spaceship1.getLockers()[1]); //the second created locker
        assertEquals(spaceship1.getLockers()[2], spaceship1.getLockers()[4]); //check the other have the
                                                                              // default value

        Locker[] curLockers = spaceship1.getLockers();

        //Test create with not valid ID
        assertEquals(spaceship1.createLocker(-1 , SMALL_LOCKER_CAPACITY), INVALID_ID);
        assertEquals(spaceship1.createLocker(-99 , SMALL_LOCKER_CAPACITY), INVALID_ID);
        assertEquals(spaceship1.createLocker(SMALL_LOCKER_CAPACITY , SMALL_LOCKER_CAPACITY), INVALID_ID);
        assertArrayEquals(curLockers, spaceship1.getLockers()); //no change in lockers array

        //Test create with not valid capacity and valid id.
        assertEquals(spaceship1.createLocker(SMALL_CREW_ID[0] , -1), INVALID_CAPACITY);
        assertEquals(spaceship1.createLocker(SMALL_CREW_ID[0] , -99), INVALID_CAPACITY);
        assertArrayEquals(curLockers, spaceship1.getLockers()); //no change in lockers array

        //Test two different spaceShips don't have the same lockers
        assertNotEquals(spaceship1.getLockers(), spaceship2.getLockers());

        //Test create more lockers then available
        // * spaceship2 current lockers array empty.
        for(int i = 0; i < SMALL_NUM_OF_LOCKERS; ++i){
            spaceship2.createLocker(SMALL_CREW_ID[0], SMALL_LOCKER_CAPACITY);
        }
        curLockers = spaceship1.getLockers();
        assertEquals(spaceship2.createLocker(SMALL_CREW_ID[0], SMALL_LOCKER_CAPACITY), MAX_LOCKERS_CREATED);
        assertArrayEquals(curLockers, spaceship1.getLockers());
    }

    @Test
    public void testGetCrewIDs(){
        //Test regular crewIds
        Spaceship spaceship1 = new Spaceship("spaceship1", SMALL_CREW_ID, SMALL_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        int[] crewIDs = spaceship1.getCrewIDs();
        assertArrayEquals(crewIDs, SMALL_CREW_ID);

        //Test EMPTY crewIds
        Spaceship spaceship2 = new Spaceship("spaceship2", EMPTY_CREW_ID, SMALL_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        crewIDs = spaceship2.getCrewIDs();
        assertArrayEquals(crewIDs, EMPTY_CREW_ID);

        //Test with negative crewIds
        Spaceship spaceship3 = new Spaceship("spaceship2", CREW_IDS_WITH_NEGATIVE_IDS, SMALL_NUM_OF_LOCKERS
                , CONSTRAINS_PAIRS);
        crewIDs = spaceship2.getCrewIDs();
        assertArrayEquals(crewIDs, EMPTY_CREW_ID);
    }

    @Test
    public void testGetLockers(){
        //Test length of lockers array equal to SMALL_NUM_OF_LOCKERS
        Spaceship spaceship1 = new Spaceship("spaceship1", SMALL_CREW_ID, SMALL_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        Locker[] lockers1 = spaceship1.getLockers();
        assertEquals(lockers1.length, SMALL_NUM_OF_LOCKERS);

        //Test create locker updates the lockers
        Locker defValue = lockers1[0];
        spaceship1.createLocker(SMALL_CREW_ID[0], SMALL_LOCKER_CAPACITY);
        lockers1 = spaceship1.getLockers();
        assertNotNull(lockers1[0]);
        assertNotEquals(lockers1[0], defValue); //check it is not as the default value.

        //Test get Lockers that EMPTY_NUM_OF_LOCKERS
        Spaceship spaceship2 = new Spaceship("spaceship2", EMPTY_CREW_ID, EMPTY_NUM_OF_LOCKERS, CONSTRAINS_PAIRS);
        Locker[] lockers2 = spaceship2.getLockers();
        assertEquals(lockers2.length, EMPTY_NUM_OF_LOCKERS);

        //Test after lockers capacity is full, create more lockers not changes the lockers
        for(int i = 1; i < SMALL_NUM_OF_LOCKERS; ++i){
            spaceship1.createLocker(SMALL_CREW_ID[1], BIG_LOCKER_CAPACITY);
        }
        lockers1 = spaceship1.getLockers();
        spaceship1.createLocker(SMALL_CREW_ID[2], BIG_LOCKER_CAPACITY);
        assertArrayEquals(lockers1, spaceship1.getLockers());

        //Test add to EMPTY_NUM_OF_LOCKERS spaceShip locker doesn't change the empty array of lockers.
        lockers2 = spaceship2.getLockers();
        spaceship2.createLocker(SMALL_CREW_ID[0], SMALL_LOCKER_CAPACITY);
        assertArrayEquals(lockers2, spaceship2.getLockers());
    }
}
