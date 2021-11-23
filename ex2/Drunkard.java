
import oop.ex2.*;
import java.awt.*;

/**
 * A ship of type Drunkard
 * This ship can fire, activate it's shield and teleport, but it's doing all it's actions randomly and
 * independently to the other action.
 */
public class Drunkard extends SpaceShip{

    /**
     * The threshold that the number received randomly must pass in order for an attempted to teleport.
     */
    private static final double TELEPORT_THRESHOLD = 0.9;

    /**
     * The threshold that the number received randomly must pass in order for an attempted to fire.
     */
    private static final double FIRE_THRESHOLD = 0.85;

    /**
     * The threshold that the number received randomly must pass in order for an attempted to activate shield.
     */
    private static final double ACTIVATE_SHIELD_THRESHOLD = 0.95;

    /**
     * The threshold that the number received randomly must pass in order to move right, otherwise move left.
     */
    private static final double RIGHT_THRESHOLD = 0.8;

    /**
     * The threshold that the number received randomly must pass in order to accelerate.
     */
    private static final double ACCELERATE_THRESHOLD = 0.5;



    /**
     * randomly move and accelerate the Drunkard,
     * if moveRand large or equal RIGHT_THRESHOLD it move's the Drunkard right, otherwise left.
     * if accelRand large or equal ACCELERATE_THRESHOLD it accelerate the Drunkard, otherwise not accelerate.
     */
    private void randMoveAndAccelerate(){
        double moveRand = SpaceShipPhysics.randomGenerator.nextDouble();
        double accelRand = SpaceShipPhysics.randomGenerator.nextDouble();
        if(moveRand >= RIGHT_THRESHOLD && accelRand >= ACCELERATE_THRESHOLD){
            this.getPhysics().move(true, moveRight);
        }
        else if(moveRand >= RIGHT_THRESHOLD && accelRand < ACCELERATE_THRESHOLD){
            this.getPhysics().move(false, moveRight);
        }
        else if(moveRand < RIGHT_THRESHOLD && accelRand >= ACCELERATE_THRESHOLD){
            this.getPhysics().move(true, moveLeft);
        }
        else{ //moveRand < 0, accelRand < 0
            this.getPhysics().move(false, moveLeft);
        }
    }

    /**
     * randomly trying to teleport the Drunkard,
     * if teleportRand large or equal TELEPORT_THRESHOLD trying to teleport.
     */
    private void randTeleport(){
        double teleportRand = SpaceShipPhysics.randomGenerator.nextDouble();
        if(teleportRand >= TELEPORT_THRESHOLD){
            teleport();
        }
    }

    /**
     * randomly trying to activate the Drunkard shield,
     * if shieldRand large or equal ACTIVATE_SHIELD_THRESHOLD trying to activate shield.
     */
    private void randShield(){
        double shieldRand = SpaceShipPhysics.randomGenerator.nextDouble();
        if(shieldRand >= ACTIVATE_SHIELD_THRESHOLD){
            shieldOn();
        }
    }

    /**
     * randomly trying to teleport the Drunkard,
     * if fireRand large or equal FIRE_THRESHOLD trying to Fire.
     *
     * @param game the game object to which this ship belongs.
     */
    private void randFire(SpaceWars game){
        double fireRand = SpaceShipPhysics.randomGenerator.nextDouble();
        if(fireRand >= FIRE_THRESHOLD){
            fire(game);
        }
    }

    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     *
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        randTeleport();
        randMoveAndAccelerate();

        shieldOff();
        randShield();

        reduceRoundLeftToFireByOne();
        randFire(game);

        addToEnergy(ROUND_ENERGY_CHARGING);
    }

    /**
     * This method is called every time a collision with this ship occurs, calls shieldAvailableCollision()
     * because the Drunkard may activated it's shield.
     */
    public void collidedWithAnotherShip() {
        shieldAvailableCollision();
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship gets hit by a shot, calls
     * shieldAvailableGotHit() because the Drunkard may activated it's shield.
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
