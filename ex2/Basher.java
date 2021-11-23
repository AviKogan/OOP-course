
import oop.ex2.*;
import java.awt.*;

/**
 * A ship of type Basher
 * This ship attempts to collide with other ships. It will always accelerate, and will constantly turn
 * towards the closest ship. If it gets within a distance of 0.19 units (inclusive) from another ship, it
 * will attempt to turn on its shields.
 *
 * @author Avi Kogan.
 */
public class Basher extends SpaceShip{

    /**
     * The minimal distance the Basher trying to activate the shield if there is a ship at a shorter
     * distance
     */
    private static final double MIN_DISTANCE_TO_ACTIVATE_SHIELD = 0.19;

    /**
     * The Basher will always accelerate, and will constantly turn towards the nearest ship.
     *
     * @param game the game object.
     */
    private void moveAndAccelerate(SpaceWars game){
        SpaceShip closestShip = game.getClosestShipTo(this);
        double angleToClosest = this.getPhysics().angleTo(closestShip.getPhysics());
        if(angleToClosest > 0){ //the closest is left of the Basher -> moveLeft to colloid with.
            this.getPhysics().move(true, moveLeft);
        }
        else{                   //the closest id right or in front of the Basher -> moveRight to colloid with.
            this.getPhysics().move(true, moveRight);
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
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     *
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        moveAndAccelerate(game);

        shieldOff();
        if(needGetShieldActive(game)){
            shieldOn();
        }

        addToEnergy(ROUND_ENERGY_CHARGING);
    }

    /**
     * This method is called every time a collision with this ship occurs, calls shieldAvailableCollision()
     * because the Basher may activated it's shield.
     */
    public void collidedWithAnotherShip() {
        shieldAvailableCollision();
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship gets hit by a shot, calls
     * shieldAvailableGotHit() because the Basher may activated it's shield.
     */
    public void gotHit() {
        shieldAvailableGotHit();
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
