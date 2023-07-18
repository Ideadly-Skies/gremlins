package gremlins;

import processing.core.PImage;

public class BrickWall extends Tile {
    
    /**
     * Constructor for the Brickwall Tile which is used in the game. Brickwalls 
     * are breakable and not empty.
     * @param x X-position of the Brickwall Tile 
     * @param y Y-position of the Brickwall Tile
     * @param image Image of the Brickwall Tile
     */
    public BrickWall(int x, int y, PImage image){
        super(x,y,image,true,false,false);
    }

}   
