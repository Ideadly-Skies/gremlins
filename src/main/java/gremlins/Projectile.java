package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class Projectile extends Entity{

    // protected attributes
    protected boolean up, down, left, right;
    protected int speed; 
    protected boolean condition;
    protected String type = "";
    protected PApplet app;

    /**
     * Constructor for the projectile entity which represents the "bullet" that are being used 
     * in the game. Projectile are moving objects, which are shortlived. It collides with walls
     * or other entities throughout the game. If the projectile is of type fireball, and it collides
     * with a brickwall, the brickwall gets destroyed and the projectile dies. There are many other 
     * projectile collision available throughout the game. 
     * @param x X-Position of the Projectile
     * @param y Y-Position of the Projectile 
     * @param image Image of the projectile
     */
    public Projectile(int x, int y, PImage image){
        super(x, y, image);
        direction = "";
    }

    // draw the projectile on the screen
    public void draw(PApplet app){
        this.app = app;
        app.image(image, x, y); 
    }

    // returns boolean condition
    public boolean getCondition(){
        return condition;
    }

    // set condition to true
    public void setCondition(){
        this.condition = true;
    }

    // set condition to false
    public void resetCondition(){
        this.condition = false; 
    }

    // set all direction to false
    public void resetDirection(){
        up = false; 
        down = false; 
        left = false; 
        right = false; 
    }

    // update projectile on the screen
    public void update(){
        
        // clash projectiles
        App.levels.clashProjectiles();

        // move the projectile
        if(left && isFree(this.x - speed, this.y)) {
            this.x -= speed;
            condition = left && isFree(this.x - speed, this.y);
        } else if(right && isFree(this.x + speed, this.y)) {
            this.x += speed;
            condition = right && isFree(this.x + speed, this.y);
        } else if(down && isFree(this.x, this.y + speed)) {
            this.y += speed;
            condition = down && isFree(this.x, this.y + speed);
        } else if(up && isFree(this.x, this.y - speed)){
            this.y -= speed;
            condition = up && isFree(this.x, this.y - speed);
        }

        // remove brickwall
        if(condition == false && type == "fireball"){
            int j = this.x;
            int i = this.y; 

            if(up){
                i = i - Levels.getTileWidth();
            } else if (down){
                i = i + Levels.getTileWidth();
            } else if (left){
                j = j - Levels.getTileWidth();
            } else if (right){
                j = j + Levels.getTileWidth();
            }

            j = j / Levels.getTileWidth();
            i = i / Levels.getTileWidth();

            App.levels.removeWall(j, i);
        }

    }

}
