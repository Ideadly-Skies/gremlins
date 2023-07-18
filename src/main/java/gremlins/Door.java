package gremlins;

import processing.core.PImage;

public class Door extends Tile{
    /**
     * Constructor for the Door Tile which is used to transport player to the next level. 
     * Door tile are not breakable and is empty, so the Gremlins can move pass it.
     * @param x X-position of the DoorTile
     * @param y Y-position of the DoorTile
     * @param image Image of the DoorTile
     */
    public Door(int x, int y, PImage image){
        super(x, y, image, false, true, true);
    }

}
