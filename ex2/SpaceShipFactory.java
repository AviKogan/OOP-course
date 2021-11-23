
/**
 * This class used to create all the spaceship objects according to the command line arguments
 *
 * @author Avi Kogan
 */
public class SpaceShipFactory {

    /**
     * Creates empty SpaceShip array based on the args (param) length, then according to the letter
     * new spaceShip generated and added to the "ships" array.
     * @param args the arguments given to the program, need to represent the ships to create in the game.
     * @return null if there is an invalid input (undefined letter) to indicate violation,
     *         otherwise returns array with suitable ships according the input.
     */
    public static SpaceShip[] createSpaceShips(String[] args) {
        SpaceShip[] ships = new SpaceShip[args.length];
        for(int i = 0; i < args.length; i++){
            switch (args[i]){
                case "s":
                    ships[i] = new Special();
                    break;
                case "d":
                    ships[i] = new Drunkard();
                    break;
                case "a":
                    ships[i] = new Aggressive();
                    break;
                case "b":
                    ships[i] = new Basher();
                    break;
                case "r":
                    ships[i] = new Runner();
                    break;
                case "h":
                    ships[i] = new Human();
                    break;
                default: //invalid input (undefined letter) violation -> return null instead array.
                    return null;
            }
        }
        return ships;
    }
}
