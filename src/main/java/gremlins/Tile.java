package gremlins;

import processing.core.PImage;

public abstract class Tile extends Object {
    // protected attributes
    protected boolean breakable;
    protected boolean empty;
    protected boolean door; 

    /**
     * Constructor for Tile which represent the parent class for all the tiles used for drawing in the game. 
     * There are only two methods. They check whether a certain tile are empty or breakable. 
     * @param x X-Position of the Tile
     * @param y Y-Position of the Tile
     * @param image Image of the Tile
     * @param breakable attribute to check whether the tile is breakable or not
     * @param empty attribute to check whether the tile is empty or not
     * @param door attribute to check whether the tile is a door or not
     */
    public Tile(int x, int y, PImage image, boolean breakable, boolean empty, boolean door){
        super(x,y,image);
        this.breakable = breakable;
        this.empty = empty;
        this.door = door;
    }

    // returns whether a tile is empty
    public boolean isEmpty(){
        return this.empty;
    }

    // returns whether a tile is breakable
    public boolean isBreakable(){
        return this.breakable;
    }

}
