package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList; 

public abstract class Entity extends Object{
   
    // protected attributes for Entity
    protected boolean up, down, left, right;
    protected boolean spacePressed; 
    protected int speed; 
    protected boolean moving;
    protected int pixelCounter;  
    protected String direction;

    /**
     * Constructor for all moving objects you see in the game. All entities have directions that they can go to at
     * certain speed, and a sophisticated collision detection system to avoid clipping through walls or to check for
     * collision with other entities in the game. I based my collision detection system from a youtube video on how 
     * to create <a href="https://youtu.be/q9nrO31oI6c">Bomberman</a> entirely from scratch. 
     * @param x X-position of our Entity
     * @param y Y-position of our Entity
     * @param image Image of our Entity
     */
    public Entity(int x, int y, PImage image){
        super(x,y,image);
    }

    // update entity on the screen
    public abstract void update();

    // draw entity on the screen
    public abstract void draw(PApplet app);

    // method to check for collision
    public boolean isFree(int nextX, int nextY) {
        
        // obtain level layout from Levels
        ArrayList<ArrayList<String>> level = Levels.getLevelString();
        int size = Levels.getTileWidth();


        int nextX_1 = nextX / size; 
        int nextY_1 = nextY / size; 

        int nextX_2 = (nextX + size - 1) / size; 
        int nextY_2 = nextY / size; 

        int nextX_3 = nextX / size; 
        int nextY_3 = (nextY + size - 1) / size; 

        int nextX_4 = (nextX + size - 1) / size; 
        int nextY_4 = (nextY + size - 1) / size; 
        
        return !((level.get(nextY_1).get(nextX_1).equals("B") || level.get(nextY_1).get(nextX_1).equals("X")) ||
        (level.get(nextY_2).get(nextX_2).equals("B") || level.get(nextY_2).get(nextX_2).equals("X")) || 
        (level.get(nextY_3).get(nextX_3).equals("B") || level.get(nextY_3).get(nextX_3).equals("X")) || 
        (level.get(nextY_4).get(nextX_4).equals("B") || level.get(nextY_4).get(nextX_4).equals("X")));
    }
}