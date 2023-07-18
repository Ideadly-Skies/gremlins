package gremlins;

import processing.core.PImage;

public class StoneWall extends Tile {
    
    /**
     * Constructor for StoneWall Tile which represents the boundary that the player or gremlin can't walk 
     * through. StoneWall is not breakable and not empty, so no amount of projectiles could destroy it. 
     * @param x X-Position of the StoneWall
     * @param y Y-Position of the StoneWall 
     * @param image Image of the StoneWall
     */
    public StoneWall(int x, int y, PImage image){
        super(x,y,image,false,false,false);
    }

}
