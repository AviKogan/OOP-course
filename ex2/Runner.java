
import oop.ex2.*;
import java.awt.*;

/**
 * A ship of type Runner
 * This spaceship attempts to run away from the fight. It will always accelerate, and will constantly turn
 * away from the closest ship. If the nearest ship is closer than 0.25 units, and if its angle to the
 * Runner is less than 0.23 radians, the Runner feels threatened and will attempt to teleport.
 *
 * @author Avi Kogan
 */
public class Runner extends SpaceShip {

    /**
     * The minimal distance that ship can be closed to the runner to check its angle to the other ship and
     * conclude if the runner is threatened.
     */
    private static final double MIN_THREATENED_DISTANCE = 0.25;

    /**
     * the minimal angle other ship can have related to the runner when it's distance is lower than
     * MIN_THREATENED_DIST without be threatened.
     */
    private static final double MIN_THREATENED_ANGLE = 0.23;

    /**
     * The Runner will always accelerate, and will constantly turn away from the closest ship, to turn away
     * it checking the angle to the closest ship and turn to the opposite side.
     * @param game the game object.
     */
    private void moveAndAccelerate(SpaceWars game){
        SpaceShip closestShip = game.getClosestShipTo(this);
        double angleToClosest = this.getPhysics().angleTo(closestShip.getPhysics());
        if(angleToClosest > 0){ //the closest is left of the runner -> moveRight to avoid.
            this.getPhysics().move(true, moveRight);
        }
        else{                   //the closest id right or in front of the runner -> moveLeft to avoid.
            this.getPhysics().move(true, moveLeft);
        }
    }

    /**
     * The Runner trying to teleport if the nearest ship is closer than MIN_THREATENED_DISTANCE units, and
     * if its angle to the Runner is less than MIN_THREATENED_ANGLE in radians,
     *
     * @param game the game object to which this ship belongs.
     * @return true if the Runner need to try teleporting, otherwise false.
     */
    private boolean isTeleportNeeded(SpaceWars game){
        SpaceShip closestShip = game.getClosestShipTo(this);
        Physics closestShipPhysics = closestShip.getPhysics();
        Physics curShipPhysics = getPhysics();
        if(curShipPhysics.distanceFrom(closestShipPhysics) < MIN_THREATENED_DISTANCE){
            // Checking the angle as absolute value because have to know if the angle in
            // (-MIN_THREATENED_ANGLE , MIN_THREATENED_ANGLE).
            double angleToClosest = java.lang.Math.abs(this.getPhysics().angleTo(closestShip.getPhysics()));

            return angleToClosest < MIN_THREATENED_ANGLE;
        }
        return false;
    }

    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     *
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        if(isTeleportNeeded(game)){
            teleport();
        }
        moveAndAccelerate(game);
        addToEnergy(ROUND_ENERGY_CHARGING);
    }

    /**
     * This method is called every time a collision with this ship occurs, calls noShieldAvailableCollision()
     * because the Runner don't activate shield.
     */
    public void collidedWithAnotherShip() {
        noShieldAvailableCollision();
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship gets hit by a shot, calls
     * noShieldAvailableGotHit() because the aggressive don't Runner shield.
     */
    public void gotHit() {
        noShieldAvailableGotHit();
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     *
     * @return image of ship without shield - because the runner don't use shield.
     */
    public Image getImage() {
        return GameGUI.ENEMY_SPACESHIP_IMAGE;
    }

}
