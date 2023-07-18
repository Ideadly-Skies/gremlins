package gremlins;

import processing.core.PImage;
import processing.core.PApplet;

public class Clock extends Object {
    
    // private attributes
    private int timer;
    private int framesCounter; 

    /**
     * The constructor for clock
     * @param x X-position of the clock
     * @param y Y-position of the clock
     * @param image Image of the clock
     * @param time Displays the time in seconds
     */
    public Clock(int x, int y, PImage image, int time){
        super(x, y, image);
        this.timer = time; 
        this.framesCounter = 0; 
    }
    
    /**
     * Draws the clock on the PApplet screen. 
     * @param app an instance of PApplet
    */
    public void draw(PApplet app){
        app.image(this.image, this.x, this.y);
        String display = Integer.toString(this.timer);
        app.text(display, 500, 700);
    }

    // updates the clock every frame
    public void update(){
        
        framesCounter++; 

        if (framesCounter == App.FPS){
            this.timer--;
            framesCounter = 0;
        }        
    }

    // get time in seconds
    public int getTimer(){
        return this.timer;
    }

}
