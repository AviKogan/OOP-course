

import oop.ex2.*;
import java.awt.*;

/**
 * A ship of type Aggressive
 * This ship pursues other ships and tries to fire at them. It will always accelerate, and turn towards the
 * nearest ship. When its angle to the nearest ship is less than 0.21 radians, it will try to fire.
 *
 * @author Avi Kogan.
 */
public class Aggressive extends SpaceShip{

    /**
     * The minimal angle in radians the Aggressive start to shoot to the nearest ship.
     */
    private static final double MIN_ANGLE_TO_FIRE = 0.21;

    /**
     * The Aggressive will always accelerate, and will constantly turn towards the nearest ship.
     *
     * @param game the game object.
     */
    protected void moveAndAccelerate(SpaceWars game){
        SpaceShip closestShip = game.getClosestShipTo(this);
        double angleToClosest = this.getPhysics().angleTo(closestShip.getPhysics());
        if(angleToClosest > 0){ //the closest is left of the Aggressive -> moveLeft to colloid with.
            getPhysics().move(true, moveLeft);
        }
        else{               //the closest id right or in front of the Aggressive -> moveRight to colloid with.
            getPhysics().move(true, moveRight);
        }
    }

    /**
     * Check if its angle to the nearest ship is less than 0.21 radians.
     *
     * @param game the game object.
     *
     * @return true if its angle to the nearest ship is less than 0.21 radians, otherwise false.
     */
    private boolean needToFire(SpaceWars game){
        SpaceShip closestShip = game.getClosestShipTo(this);
        // Checking the angle as absolute value because need to know if the angle in
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
        moveAndAccelerate(game);

        reduceRoundLeftToFireByOne();
        if(needToFire(game)){
            fire(game);
        }
        addToEnergy(ROUND_ENERGY_CHARGING);
    }

    /**
     * This method is called every time a collision with this ship occurs, calls noShieldAvailableCollision()
     * because the aggressive don't activate shield.
     */
    public void collidedWithAnotherShip() {
        noShieldAvailableCollision();
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship gets hit by a shot, calls
     * noShieldAvailableGotHit() because the aggressive don't activate shield.
     */
    public void gotHit() {
        noShieldAvailableGotHit();
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     *
     * @return image of ship without shield - because the Aggressive don't use shield.
     */
    public Image getImage() {
        return GameGUI.ENEMY_SPACESHIP_IMAGE;
    }
}
