package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Object {
    // protected attributes
    protected int x; 
    protected int y;
    protected PImage image;
    
    /**
     * Constructor for Object, which is a parent class of all the game objects used in this game. 
     * They can't be instantiated and serves as a guideline for all the game objects which inherits from them. 
     * It consists of nothing other than getter and a draw method. 
     * @param x X-Position of the Object
     * @param y Y-Position of the Object
     * @param image Image of the Object
     */
    public Object(int x, int y, PImage image){
        this.x = x; 
        this.y = y;
        this.image = image;
    }

    // return X-Position
    public int getX(){
        return this.x;
    }

    // return Y-Position
    public int getY(){
        return this.y;
    }

    // draw object on the screen
    public void draw(PApplet app){
        app.image(this.image, this.x, this.y);
    }

    // returns width of object
    public int getWidth(){
        return this.image.width;
    }

    // returns height of object
    public int getHeight(){
        return this.image.height;
    }

}
