package gremlins;

import processing.core.PImage;

public class ChadGremlin extends Gremlins{
    
    // private attributes
    private int freezeTimer;
    private Clock freezeClock;
    private Projectile fireballCheck;

    /**
     * Constructor for the Gremlin "Boss", Chad Gremlin.
     * <p> 
     * It inherits all methods and attributes from Gremlin but methods to check for 
     * collision with wizard, fireball, and update gets modified. 
     * @param x X-position of the Chad Gremlin
     * @param y Y-position of the Chad Gremlin
     * @param image Image of the Chad Gremlin
    */
    public ChadGremlin(int x, int y, PImage image){
        super(x, y, image);
        this.speed = 4; 
        this.cooldown = 5; 
    }

    /*
     * ChadGremlin is invisible towards fireball, no matter how much you shoot
     * You can't kill it, as it absorbs all of the fireballs directed towards him.
    */
    @Override
    public void touchFireball(){    
        // grab information from gremlin (main)
        int gremlinRight = this.x + this.getWidth();
        int gremlinBottom = this.y + this.getHeight();
        
        int i = 0;
        int fireballLeft = 0;
        int fireballRight = 0;
        int fireballTop = 0;
        int fireballBottom = 0;

        // grab information from projectile
        while (i < Levels.getProjectileList().size()){
            fireballCheck = Levels.getProjectileList().get(i);
            if (fireballCheck.type == "fireball"){
                // grab information from fireball (target)
                fireballLeft = fireballCheck.getX();
                fireballRight = fireballCheck.getX() + fireballCheck.getWidth();
                fireballTop = fireballCheck.getY();
                fireballBottom = fireballCheck.getY() + fireballCheck.getHeight();
            }
            i++;
        }
        
        // absorb fireball
        int j = 0; 
        while (j < Levels.getEntities().size()){
            if (Levels.getEntities().get(j) instanceof Gremlins){
                if(gremlinRight > fireballLeft && this.x < fireballRight && gremlinBottom > fireballTop && this.y < fireballBottom){
                    fireballCheck.resetCondition(); 
                }
            } 
            j++;
        }

    }

    /*
     * Instead of decreasing the life of the wizard, Chad Gremlin would make the
     * wizard feel as if he is stuck in a mud and can't move fast. The level does not
     * reset. 
    */
    @Override
    public void touchWizard(){
        
        // grab information from gremlin (main)
        int ChadGremlinRight = this.x + this.getWidth();
        int ChadGremlinBottom = this.y + this.getHeight();
        
        // grab information from wizard (target)
        int wizardLeft = App.wizard.getX();
        int wizardRight = App.wizard.getX() + App.wizard.getWidth();
        int wizardTop = App.wizard.getY();
        int wizardBottom = App.wizard.getY() + App.wizard.getHeight();
        
        // make the wizard stuck (3 seconds)
        if(ChadGremlinRight > wizardLeft && this.x < wizardRight && ChadGremlinBottom > wizardTop && this.y < wizardBottom){
            freezeTimer = 3 * App.FPS; 
            freezeClock = new Clock(480, 685, App.chad, 3);
            Levels.addClock(freezeClock);
        }
    }

    /*
     * Chad Gremlin do not shoot projectiles. They simply move fast and slows down the wizard. 
    */
    @Override
    public void update(){

        // freeze wizard
        touchWizard();
        // absorb fireball
        touchFireball();

        // decrement freezeTimer
        if (freezeTimer > 0){
            App.wizard.up = false; 
            App.wizard.down = false; 
            App.wizard.left = false; 
            App.wizard.right = false;
            App.spacePressed = false;  
            freezeTimer--;
        }

        // move gremlin
        if(left && isFree(this.x - speed, this.y)) {
            this.x -= speed;
            // set condition as left
            condition = left && isFree(this.x - speed, this.y);
        } else if(right && isFree(this.x + speed, this.y)) {
            this.x += speed;
            // set condition as right
            condition = right && isFree(this.x + speed, this.y);
        } else if(down && isFree(this.x, this.y + speed)) {
            this.y += speed;
            // set condition as down
            condition = down && isFree(this.x, this.y + speed);
        } else if(up && isFree(this.x, this.y - speed)){
            this.y -= speed;
            // set condition as up
            condition = up && isFree(this.x, this.y - speed);
        }

        // change direction of gremlin
        if (!condition){
            // get new direction
            direction = checkValidMoves();
            // reset direction
            up = false; down = false; left = false; right = false; 
            // set direction
            if (direction.equals("up")){
                up = true; 
            } else if (direction.equals("down")){
                down = true; 
            } else if (direction.equals("left")){
                left = true;
            } else if (direction.equals("right")){
                right = true;
            }
        }
    }



}
