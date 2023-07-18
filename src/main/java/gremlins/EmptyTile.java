package gremlins;

import processing.core.PImage;

public class EmptyTile extends Tile{
    
    /**
     * Constructor for the Empty Tile which both Wizard and Gremlin can walk on. It is empty in a sense that you can't 
     * destroy it. When a brickwall is destroyed, it will be replaced with this tile.
     * <p> 
     * This is NOT one of the three main type of tiles which we are only allowed to use. Since I implemented
     * the draw method based on Tile Objects, I found out about this pretty late and sadly have to sacrifice one
     * major key component of the game. To Tom Schwarz, I sincerely apologize. Next time I will base my draw method
     * on the ArrayList of String Map directly instead of having to create a tile object for each tiles :'(.
     * @param x X-position of the EmptyTile
     * @param y Y-position of the EmptyTile
     * @param image Image of the EmptyTile
     */
    public EmptyTile(int x, int y, PImage image){
        super(x, y, image, false, true, false);
    }
}
