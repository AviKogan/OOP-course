

import java.awt.Image;
import oop.ex2.*;

/**
 * An abstract class that represents a spaceShip,
 * no constructor, all data members initialized with class constants.
 *  
 * @author Avi Kogan
 */
public abstract class SpaceShip{

    /**
     * the health value of new ship when created.
     */
    private static final int HEALTH_AT_START = 22;

    /**
     * the health value of ship when it dead and need to be restarted.
     */
    private static final int DEAD_HEALTH = 0;

    /**
     * the value of max energy level for new ship when created.
     */
    private static final int MAX_ENERGY_LEVEL_AT_START = 210;

    /**
     * the value of minimal energy level for new ship.
     */
    private static final int MIN_ENERGY_LEVEL = 0;

    /**
     * the value of current energy level for new ship when created.
     */
    private static final int ENERGY_LEVEL_AT_START = 180;

    /**
     * The amount added to current ship's energy at the end of every round.
     */
    protected static final int ROUND_ENERGY_CHARGING = 1;

    /**
     * The energy cost for ship to do teleport.
     */
    private static final int TELEPORT_ENERGY_COST = 140;

    /**
     * The energy cost for ship to activate shield.
     */
    private static final int SHIELD_ENERGY_COST = 3;

    /**
     * The energy value added to _currentMaxEnergy and _currentEnergyLevel when bashing happened (collided
     * with shield)
     */
    protected static final int BASHING_BONUS = 18;

    /**
     * Represent the shield status when the shield active.
     */
    protected static final int SHIELD_ON = 1;

    /**
     * Represent the shield status when the shield not active.
     */
    protected static final int SHIELD_OFF = 0;

    /**
     * The parameter passed to move function in SpaceShipPhysics to move the ship right.
     */
    protected static final int moveRight = -1;

    /**
     * The parameter passed to move function in SpaceShipPhysics to move the ship left.
     */
    protected static final int moveLeft = 1;

    /**
     * The parameter passed to move function in SpaceShipPhysics to move the ship left.
     */
    protected static final int noMoveSidePressed = 0;

    /**
     * The number of rounds ship need to wait before it can fire again after fired.
     */
    private static final int MIN_ROUNDS_BETWEEN_FIRES = 7;

    /**
     * Represent the value of _roundLeftToFire when it's possible to fire in current round.
     */
    protected static final int CAN_FIRE_THIS_ROUND = 0;

    /**
     * The energy cost for ship to do teleport.
     */
    private static final int FIRE_ENERGY_COST = 19;

    /**
     * The current ship's health level.
     */
    private int _currentHealth = HEALTH_AT_START;

    /**
     * the max energy value ship can reach, updated during the game.
     */
    private int _currentMaxEnergy = MAX_ENERGY_LEVEL_AT_START;

    /**
     * The current ship's energy level, updated during the game, can't be more than _currentMaxEnergy.
     */
    private int _currentEnergyLevel = ENERGY_LEVEL_AT_START;

    /**
     * Represent the current shield status.
     */
    private int _shieldStatus = SHIELD_OFF;

    /**
     * Represent the number of round need to wait until the ship can fire again, between
     * CAN_FIRE_THIS_ROUND - MIN_ROUNDS_BETWEEN_FIRES;
     */
    private int _roundLeftToFire = CAN_FIRE_THIS_ROUND;

    /**
     * Composition relation to SpaceShipPhysics, represent the ship's physics state.
     */
    private SpaceShipPhysics _spaceShipPhysics = new SpaceShipPhysics();

    /**
     * Does the actions of this ship for this round. 
     * This is called once per round by the SpaceWars game driver.
     * Defined abstract because each ship does other actions in each round.
     * 
     * @param game the game object to which this ship belongs.
     */
    public abstract void doAction(SpaceWars game);

    /**
     * This method is called every time a collision with this ship occurs
     * Defined abstract because not all ships have the same checks for collision.
     */
    public abstract void collidedWithAnotherShip();

    /**
     * This method called every time a collision with this ship occurs if this ship may have shield available.
     */
    protected void shieldAvailableCollision(){
        if(_shieldStatus == SHIELD_ON){
            _currentMaxEnergy += BASHING_BONUS;
            addToEnergy(BASHING_BONUS);
        }
        else{
            reduceHealthByOne();
        }
    }

    /**
     * This method called every time a collision with this ship occurs if this ship without shield available.
     */
    protected void noShieldAvailableCollision(){
        reduceHealthByOne();
    }

    /** 
     * This method is called whenever a ship has died. It resets the ship's 
     * attributes, and starts it at a new random position.
     */
    public void reset(){
        _currentHealth = HEALTH_AT_START;
        _currentEnergyLevel = ENERGY_LEVEL_AT_START;
        _currentMaxEnergy = MAX_ENERGY_LEVEL_AT_START;
        _spaceShipPhysics = new SpaceShipPhysics();
        _shieldStatus = SHIELD_OFF;
    }

    /**
     * Checks if this ship is dead.
     * 
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {
        return _currentHealth == DEAD_HEALTH;
    }

    /**
     * Gets the physics object that controls this ship.
     * 
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return _spaceShipPhysics;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets hit by a shot.
     * Defined abstract because not all ships have the same checks when got hit.
     */
    public abstract void gotHit();

    /**
     * This method called every time this ship gets hit by a shot, if this ship may have shield available.
     */
    protected void shieldAvailableGotHit(){
        if(_shieldStatus == SHIELD_OFF){
            reduceHealthByOne();
        }
    }

    /**
     * This method called every time this ship gets hit by a shot, if this ship without shield available.
     */
    protected void noShieldAvailableGotHit(){
        reduceHealthByOne();
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * Defined abstract because each ship can have it's own image.
     * 
     * @return the image of this ship.
     */
    public abstract Image getImage();

    /**
     * Attempts to fire a shot.
     * Implemented because its the same for each ship that can fire.
     * 
     * @param game the game object.
     */
    public void fire(SpaceWars game){
        if(_currentEnergyLevel >= FIRE_ENERGY_COST && _roundLeftToFire == CAN_FIRE_THIS_ROUND){
            game.addShot(getPhysics());
            _roundLeftToFire = MIN_ROUNDS_BETWEEN_FIRES;
            addToEnergy(-1 * FIRE_ENERGY_COST);
        }
    }

    /**
     * Attempts to turn on the shield.
     * Implemented because its the same for each ship that can activate shield.
     */
    public void shieldOn(){
        if(_shieldStatus == SHIELD_OFF && _currentEnergyLevel >= SHIELD_ENERGY_COST){
            _shieldStatus = SHIELD_ON;
            addToEnergy(-1 * SHIELD_ENERGY_COST);
        }
    }

    /**
     * set _shieldStatus to SHIELD_OFF.
     * Implemented because its the same for each ship that can activate shield.
     */
    public void shieldOff(){
        _shieldStatus = SHIELD_OFF;
    }

    /**
     * @return current _shieldStatus value.
     */
    public int getShieldStatus() {
        return _shieldStatus;
    }

    /**
     * Attempts to teleport.
     * Implemented because its the same for each ship that can teleport.
     */
    public void teleport(){
        if(_currentEnergyLevel >= TELEPORT_ENERGY_COST){
            _spaceShipPhysics = new SpaceShipPhysics();
            addToEnergy(-1 * TELEPORT_ENERGY_COST);
        }
    }

    /**
     * Change ship's energy with the given amount, validating the new energy level between
     * (0 -  _currentMaxEnergy).
     *
     * @param amount the amount to add to the energy can be positive or negative.
     */
    public void addToEnergy(int amount){
        _currentEnergyLevel += amount;

        if(_currentEnergyLevel > _currentMaxEnergy){
            _currentEnergyLevel = _currentMaxEnergy;
        }
        else if(_currentEnergyLevel < MIN_ENERGY_LEVEL){
            _currentEnergyLevel = MIN_ENERGY_LEVEL;
        }
    }

    /**
     * This method called every time this ship health need to be reduced by one.
     */
    public void reduceHealthByOne() {
        if(_currentHealth > DEAD_HEALTH){
            _currentHealth--;
        }
    }

    /**
     * This method called every round for ship that can fire to maintain the rounds left to fire for this
     * ship.
     */
    public void reduceRoundLeftToFireByOne() {
        if(_roundLeftToFire > CAN_FIRE_THIS_ROUND){
            _roundLeftToFire--;
        }
    }

    /**
     * @return _roundLeftToFire
     */
    public int getRoundLeftToFire() {
        return _roundLeftToFire;
    }
}
