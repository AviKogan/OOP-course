import oop.ex3.spaceship.*;
import java.util.HashSet;

/**
 * Represent a USS DISCOVERY spaceShip.
 * The spaceShip contains private lockers for the crew to store their items, and also contains a shared
 * storage that is the long term storage, used to move items from the private locker if necessary.
 *
 * @author Avi Kogan.
 */
public class Spaceship {


    private String _name;

    /**
     * The long term storage of the spaceShip.
     */
    private LongTermStorage _lts;

    /**
     * The all created lockers in the spaceShip constrains.
     */
    private Item[][] _constrains;

    /**
     * The maximal number of locker can be created in the spaceShip.
     */
    private int _maxNumOfLockers;

    /**
     * Represent the all created lockers, initialized empty with size of _maxNumOfLockers in the constructor.
     */
    private Locker[] _lockers;
    /**
     * Represent the current number of created lockers, and also the first available index in _lockers if
     * it's no full.
     */
    private int _curNumOfLockers = 0;

    private int [] _crewIDs;

    /**
     * Represent the all crewIDs in _crewIDs, created to get efficient access for the valid crewIDs.
     */
    private HashSet<Integer> _crewIDsSet = new HashSet<Integer>();

    /**
     * Init the _crewIDs member,
     * Init the _crewIDsSet with the IDs from _crewIDs.
     * @param crewIDs the given array of valid crew IDs.
     */
    private void initCrewIds(int [] crewIDs){
        _crewIDs = crewIDs;
        for(int crewId : _crewIDs){
            _crewIDsSet.add(crewId);
        }
    }

    /**
     * Init _numOfLockers.
     * Init the _locker member with empty array of size _numOfLockers.
     * @param numOfLockers the maximal number of lockers can be created on the spaceShip.
     */
    private void initLockers(int numOfLockers){
        _maxNumOfLockers = numOfLockers;
        _lockers = new Locker[_maxNumOfLockers]; // by default all indexes null.
    }

    /**
     * SpaceShip constructor, assumes all the given parameters are valid, none of them null.
     * @param name the spaceShip name.
     * @param crewIDs the crew IDs, the IDs that valid in the spaceShip.
     * @param numOfLockers the maximal number of lockers can be created on the spaceShip.
     * @param constrains array of pair of two items that are not allowed to reside in any locker in the
     *                   spaceShip.
     */
    public Spaceship(String name, int[] crewIDs, int numOfLockers, Item[][] constrains){
        _name = name;
        _lts = new LongTermStorage();
        _constrains = constrains;
        initLockers(numOfLockers);
        initCrewIds(crewIDs);
    }

    /**
     * @return the spaceShip long term storage.
     */
    public LongTermStorage getLongTermStorage(){ return _lts; }

    /**
     * Creates a locker and add it to the first available index in the _lockers array.
     * @param crewId the crewID to create the locker for.
     * @param capacity the need to be created locker capacity.
     * @return 0 if the locker created successfully.
     *         -1 if the crewID is invalid - not exist in the _crewIDsSet, also returned if locker didn't
     *            created when trying to create locker after all the other checks passed.
     *         -2 if the capacity negative.
     *         -3 if the _lockers array is full - there is no more place for new locker.
     */
    public int createLocker(int crewId, int capacity){
        int CREATE_SUCCESS = 0;
        int INVALID_ID = -1;
        int INVALID_CAPACITY = -2;
        int MAX_LOCKERS_CREATED = -3;
        int LOCKER_DIDNT_CREATE = -1;

        if(!_crewIDsSet.contains(crewId)) return INVALID_ID;
        if(capacity < 0) return INVALID_CAPACITY;
        if(_curNumOfLockers == _maxNumOfLockers) return MAX_LOCKERS_CREATED;
        _lockers[_curNumOfLockers] = new Locker(_lts, capacity, _constrains);
        if(_lockers[_curNumOfLockers] == null) return LOCKER_DIDNT_CREATE;
        _curNumOfLockers++;
        return CREATE_SUCCESS;
    }

    /**
     * @return the spaceShip crewIDs array.
     */
    public int[] getCrewIDs(){ return _crewIDs; }

    /**
     * @return the spaceShip lockers array - updated with the all created lockers.
     */
    public Locker[] getLockers(){ return _lockers; }

}
