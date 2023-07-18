package gremlins;

import processing.core.PImage;
import processing.core.PApplet;

// powerup to spawn in levels
public class Powerup extends Object{
    /**
     * Powerup constructor which represents a collectible item in the game. They spawn after 10 seconds (600 frames)
     * at specific locations specified on the map. Once the wizard collects one of these item, they will respawn again after
     * 10 seconds. I have decided to implement a speed power up which makes the player speed up for only a certain amount of time.
     * @param x X-Position of the powerup on the map
     * @param y Y-Position of the powerup on the map
     * @param image Image of the powerup
    */
    public Powerup(int x, int y, PImage image){
        super(x, y, image);
    }

    // draw powerup on the screen
    public void draw(PApplet app){
        app.image(this.image, this.x, this.y);
    }

}
