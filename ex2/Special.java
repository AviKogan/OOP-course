
import oop.ex2.*;
import java.awt.*;

/**
 * A ship of type Special
 * This spaceship attempts to run away from the closest ship. It will always accelerate, and will constantly
 * turn away from the closest ship similar to the Runner.
 * Similar to the Aggressive - If its angle to the nearest ship is less than 0.21 radians, it will try to
 * fire to defense itself, instead of regular fire it has the specialFire.
 * Similar to the Basher if it gets within a distance of 0.19 units (inclusive) from another ship, it will
 * attempt to turn on its shields.
 * Not like all the other ships the special ships can't defend against shots, so each shot reduce its life.
 *
 * @author Avi Kogan
 */
public class Special extends SpaceShip {

    /**
     * Represent the number of fires minus one the Special ship fire each time it can fire.
     */
    private static final int NUM_OF_FIRES = 5;

    /**
     * The minimal distance the Special trying to activate the shield if there is a ship at a shorter or
     * equal distance.
     */
    private static final double MIN_DISTANCE_TO_ACTIVATE_SHIELD = 0.19;

    /**
     * The minimal angle in radians the Aggressive start to shoot to the nearest ship.
     */
    private static final double MIN_ANGLE_TO_FIRE = 0.21;

    /**
     * When the Special trying to fire, it fire NUM_OF_FIRES times (at least one fire) and it cost like one
     * fire for non-special ship.
     *
     * @param game the game object.
     */
    private void specialFire(SpaceWars game) {
        if (getRoundLeftToFire() == CAN_FIRE_THIS_ROUND) {
            for (int i = 1; i < NUM_OF_FIRES; i++) {
                game.addShot(getPhysics());
                getPhysics().move(false, moveRight); //move each shot little bit to the right of the
                                                           // shot before
            }
            fire(game); //regular fire to update the round left to fire.
        }
    }

    /**
     * The Special will always accelerate, and will constantly turn away from the closest ship, to turn away
     * it checking the angle to the closest ship and turn to the opposite side.
     *
     * @param game the game object.
     */
    private void moveAndAccelerate(SpaceWars game){
        SpaceShip closestShip = game.getClosestShipTo(this);
        double angleToClosest = this.getPhysics().angleTo(closestShip.getPhysics());
        if(angleToClosest > 0){ //the closest is left of the Special -> moveRight to avoid.
            this.getPhysics().move(true, moveRight);
        }
        else{                   //the closest id right or in front of the Special -> moveLeft to avoid.
            this.getPhysics().move(true, moveLeft);
        }
    }

    /**
     * Check if ship's distance from the closest ship is shorter or equal than MIN_DISTANCE_TO_ACTIVATE_SHIELD
     * so the shield need to be activated.
     *
     * @param game the game object to which this ship belongs.
     * @return true if distance from the closest ship is shorter or equal than
     * MIN_DISTANCE_TO_ACTIVATE_SHIELD, otherwise false.
     */
    private boolean needGetShieldActive(SpaceWars game){
        Physics closestShipPhysics = game.getClosestShipTo(this).getPhysics();
        Physics curShipPhysics = getPhysics();
        return curShipPhysics.distanceFrom(closestShipPhysics) <= MIN_DISTANCE_TO_ACTIVATE_SHIELD;
    }

    /**
     * Check if its angle to the nearest ship is less than 0.21 radians.
     *
     * @param game the game object.
     *
     * @return true if its angle to the nearest ship is less than 0.21 radians, otherwise false.
     */
    private boolean needToFire(SpaceWars game) {
        SpaceShip closestShip = game.getClosestShipTo(this);
        // Checking the angle as absolute value because have to know if the angle in
        // (-MIN_ANGLE_TO_FIRE , MIN_ANGLE_TO_FIRE).
        double angleToClosest = java.lang.Math.abs(this.getPhysics().angleTo(closestShip.getPhysics()));
        return angleToClosest < MIN_ANGLE_TO_FIRE;
    }

    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     *
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        moveAndAccelerate(game); //Similar the Runner

        reduceRoundLeftToFireByOne();
        if(needToFire(game)){ //Similar the aggressive
            specialFire(game);
        }

        shieldOff();
        if(needGetShieldActive(game)){ //Similar the Basher
            shieldOn();
        }

        addToEnergy(ROUND_ENERGY_CHARGING);
    }


    /**
     * This method is called every time a collision with this ship occurs, calls shieldAvailableCollision()
     * because the Special may activated it's shield.
     */
    public void collidedWithAnotherShip() {
        shieldAvailableCollision();
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship gets hit by a shot, calls
     * noShieldAvailableGotHit() because the Special shield can't defend against shots.
     */
    public void gotHit() {
        noShieldAvailableGotHit();
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     *
     * @return the image of this ship according to the shield state.
     */
    public Image getImage() {
        if(getShieldStatus() == SHIELD_OFF) return GameGUI.ENEMY_SPACESHIP_IMAGE;
        return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
    }
}
