package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;
import java.util.ArrayList;

public class Gremlins extends Entity{
    
    // private and protected attributes
    private Random rand;
    private ArrayList<String> moves; 
    private int counter;
    private Projectile slime; 
    private Projectile fireball;
    protected String direction; 
    protected boolean condition; 
    protected int cooldown; 
    
    /**
     * Constructor for Gremlin entity that player would have a fateful encounter with it in the game. 
     * Gremlin moves in a random starting direction before changing after it encounters a wall. They shoot
     * green projectiles called slime and will decrement the live of the wizard if the wizard touches it. 
     * The wizard must avoid touching them or their slimes in order not to lose a life. 
     * @param x X-Position of the Gremlins
     * @param y Y-Position of the Gremlins
     * @param image Image of the Gremlins
     */
    public Gremlins(int x, int y, PImage image){
        super(x, y, image);
        this.speed = 1;
        rand = new Random();
        cooldown = (int) Levels.getGremlinCoolDown() * 60;
    }

    // draw gremlin on the screen
    public void draw(PApplet app){
        app.image(image, x, y);
    }

    // check collision with wizard
    public void touchWizard(){    
        // grab information from gremlin (main)
        int gremlinRight = this.x + this.getWidth();
        int gremlinBottom = this.y + this.getHeight();
        
        // grab information from wizard (target)
        int wizardLeft = App.wizard.getX();
        int wizardRight = App.wizard.getX() + App.wizard.getWidth();
        int wizardTop = App.wizard.getY();
        int wizardBottom = App.wizard.getY() + App.wizard.getHeight();
        
        // decrease life
        if(gremlinRight > wizardLeft && this.x < wizardRight && gremlinBottom > wizardTop && this.y < wizardBottom){
            App.noOfLives.decreaseLives();
        }
    }

    // check collision with fireball
    public void touchFireball(){
        // grab information from gremlin (main)
        int gremlinRight = this.x + this.getWidth();
        int gremlinBottom = this.y + this.getHeight();
        
        int i = 0;
        int fireballLeft = 0;
        int fireballRight = 0;
        int fireballTop = 0;
        int fireballBottom = 0;

        // grab information from projectile (target)
        while (i < Levels.getProjectileList().size()){
            fireball = Levels.getProjectileList().get(i);
            if (fireball.type == "fireball"){
                fireballLeft = fireball.getX();
                fireballRight = fireball.getX() + fireball.getWidth();
                fireballTop = fireball.getY();
                fireballBottom = fireball.getY() + fireball.getHeight();
            }
            i++;
        }
        
        int j = 0; 
        
        // kill the gremlin
        while (j < Levels.getEntities().size()){
            if (Levels.getEntities().get(j) instanceof Gremlins){
                if(gremlinRight > fireballLeft && this.x < fireballRight && gremlinBottom > fireballTop && this.y < fireballBottom){
                    Levels.removeGremlin(this);                    
                    Levels.addGremlin(this);
                    fireball.resetCondition();
                }
            } 
            j++;
        }
    }

    // set condition for gremlin after respawn
    public void setCondition(boolean condition){
        this.condition = condition;
    }

    // check gremlin valid next move
    public String checkValidMoves(){
        moves = new ArrayList<String>();

        if(up == true){
            if(isFree(this.x - speed, this.y)) {
                moves.add("left"); 
            } if(isFree(this.x + speed, this.y)) {
                moves.add("right");
            } if(!isFree(this.x - speed, this.y) && !isFree(this.x + speed, this.y) && !isFree(this.x, this.y - speed)){
                moves.add("down");
            }
        } 
        
        else if (down == true){
            if(isFree(this.x - speed, this.y)) {
                moves.add("left"); 
            } if(isFree(this.x + speed, this.y)) {
                moves.add("right");
            } if(!isFree(this.x - speed, this.y) && !isFree(this.x + speed, this.y) && !isFree(this.x, this.y + speed)){
                moves.add("up");
            }
        } 
        
        else if (left == true){
            if(isFree(this.x, this.y + speed)) {
                moves.add("down");
            } if(isFree(this.x, this.y - speed)){
                moves.add("up"); 
            } if(!isFree(this.x - speed, this.y) && !isFree(this.x, this.y + speed) && !isFree(this.x, this.y - speed)){
                moves.add("right");
            }
        }
        
        else if (right == true){
            if(isFree(this.x, this.y + speed)) {
                moves.add("down");
            } if(isFree(this.x, this.y - speed)){
                moves.add("up"); 
            } if(!isFree(this.x + speed, this.y) && !isFree(this.x, this.y + speed) && !isFree(this.x, this.y - speed)){
                moves.add("left");
            }
        }

        String direction = "";

        if (!moves.isEmpty()){
            int i = rand.nextInt(moves.size());
            direction = moves.get(i);
        } 
        
        else if (moves.isEmpty()) {
            if(isFree(this.x, this.y - speed)){
                moves.add("up"); 
            } 
            if(isFree(this.x, this.y + speed)) {
                moves.add("down");
            } 
            if(isFree(this.x - speed, this.y)) {
                moves.add("left"); 
            } 
            if(isFree(this.x + speed, this.y)) {
                moves.add("right");
            }
            int i = rand.nextInt(moves.size());
            direction = moves.get(i);
        }
        
        return direction;
    }

    // initialize slime projectile
    public void initProjectile(String dir){
        // reset direction and speed
        slime = new Projectile(x, y, App.slime);
        slime.resetDirection();
        slime.type = "slime";
        slime.speed = 4; 
        // choose firing direction
        if (dir.equalsIgnoreCase("up")){
            slime.up = true;
        } else if (dir.equalsIgnoreCase("down")){
            slime.down = true;
        } else if (dir.equalsIgnoreCase("left")){
            slime.left = true;
        } else if (dir.equalsIgnoreCase("right")){
            slime.right = true;
        }   
        // set firing condition
        slime.setCondition();
    }

    // update gremlin on the screen
    public void update(){

        // reset level
        touchWizard();
        // kill gremlin
        touchFireball();

        // move gremlin left
        if(left && isFree(this.x - speed, this.y)) {
            this.x -= speed;
            // shoot projectile left
            if (counter == cooldown){
                initProjectile("left");
                Levels.addProjectile(slime);
                counter = 0; 
            }
            // set condition as left
            condition = left && isFree(this.x - speed, this.y);
        } 

        // move gremlin right
        else if(right && isFree(this.x + speed, this.y)) {
            this.x += speed;
            // shoot projectile right
            if (counter == cooldown){
                initProjectile("right");
                Levels.addProjectile(slime);
                counter = 0;
            }
            // set condition as right
            condition = right && isFree(this.x + speed, this.y);
        } 

        // move gremlin down
        else if(down && isFree(this.x, this.y + speed)) {
            this.y += speed;
            // shoot projectile down
            if (counter == cooldown){
                initProjectile("down");
                Levels.addProjectile(slime);
                counter = 0;
            }
            // set condition as down
            condition = down && isFree(this.x, this.y + speed);
        } 
        // move gremlin up
        else if(up && isFree(this.x, this.y - speed)){
            this.y -= speed;
            // shoot projectile up
            if (counter == cooldown){
                initProjectile("up");
                Levels.addProjectile(slime);
                counter = 0;
            }
            // set checking condition to up
            condition = up && isFree(this.x, this.y - speed);
        }

        // change direction of gremlin
        if (!condition){
            // reset the projectile
            Levels.resetProjectile(slime);
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
        counter++;
    }
}