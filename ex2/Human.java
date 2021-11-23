
import oop.ex2.*;
import java.awt.*;

/**
 * A ship of type Human
 * This spaceship controlled by the user. The keys are: left-arrow and right-arrow to turn, up-arrow to
 * accelerate, 'd' to fire a shot, 's' to turn on the shield, 'a' to teleport.
 *
 * @author Avi Kogan
 */
public class Human extends SpaceShip{

    /**
     * Check if right or left direction pressed and if accelerate pressed and call to SpaceShipPhysics
     * move method with the relevant parameters.
     * @param game the game object.
     */
    private void moveAndAccelerate(SpaceWars game){
        if(game.getGUI().isRightPressed())
        {
            this.getPhysics().move(game.getGUI().isUpPressed(), moveRight);
        }
        else if(game.getGUI().isLeftPressed()){
            this.getPhysics().move(game.getGUI().isUpPressed(), moveLeft);
        }
        else{
            this.getPhysics().move(game.getGUI().isUpPressed(), noMoveSidePressed);
        }
    }


    /**
     * Does the actions of this ship for this round.
     * This is called once per round by the SpaceWars game driver.
     *
     * @param game the game object to which this ship belongs.
     */
    public void doAction(SpaceWars game) {
        if(game.getGUI().isTeleportPressed()){
            teleport();
        }

        moveAndAccelerate(game);

        shieldOff();
        if(game.getGUI().isShieldsPressed()){
            shieldOn();
        }

        reduceRoundLeftToFireByOne();
        if(game.getGUI().isShotPressed()){
            fire(game);
        }

        addToEnergy(ROUND_ENERGY_CHARGING);
    }


    /**
     * This method is called every time a collision with this ship occurs, calls shieldAvailableCollision()
     * because the Human may activated it's shield.
     */
    public void collidedWithAnotherShip() {
        shieldAvailableCollision();
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship gets hit by a shot, calls
     * shieldAvailableGotHit() because the Human may activated it's shield.
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
        if(getShieldStatus() == SHIELD_OFF) return GameGUI.SPACESHIP_IMAGE;
        return GameGUI.SPACESHIP_IMAGE_SHIELD;
    }
}
