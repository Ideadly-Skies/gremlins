package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Wizard extends Entity{
    // private attributes
    private int cooldown; 
    private int projectileTimer; 
    private boolean powerupEnabled; 
    private int powerupTimer; 
    private ArrayList<Powerup> temp;  
    private Clock powerupClock; 

    /**
     * Constructor for Wizard, the main character that you are going to be controlling throughout the game. 
     * @param x X-Position of the wizard
     * @param y Y-Position of the wizard
     * @param image Image of the wizard
    */
    public Wizard(int x, int y, PImage image){
        super(x,y,image);
        this.speed = 2;
        cooldown = (int)(Levels.getWizardCoolDown() * 60);
        temp = new ArrayList<Powerup>();
    }

    // draw the wizard on the screen
    public void draw(PApplet app){
        app.image(this.image, this.x, this.y);
    }

    // touch slime method
    public void touchSlime(){
        // grab information from wizard (main)
        int wizardRight = this.x + this.getWidth();
        int wizardBottom = this.y + this.getHeight();
        
        int i = 0;
        while (i < Levels.getProjectileList().size()){
            // check slime projectile
            Projectile slime = Levels.getProjectileList().get(i);
            if (slime.type == "slime"){
                // grab information from slime (target)
                int slimeLeft = slime.getX();
                int slimeRight = slime.getX() + slime.getWidth();
                int slimeTop = slime.getY();
                int slimeBottom = slime.getY() + slime.getHeight();

                // decrease live
                if(wizardRight > slimeLeft && this.x < slimeRight && wizardBottom > slimeTop && this.y < slimeBottom){
                    App.noOfLives.decreaseLives();
                }
            }
            i++;
        }
    }

    // check if wizard touches powerup
    public void touchPowerup(){
        // grab information from wizard (main)
        int wizardRight = this.x + this.getWidth();
        int wizardBottom = this.y + this.getHeight();

        int i = 0; 
        while (i < Levels.getPowerUps().size()){
            // check powerup projectile (main)
            Powerup powerup = Levels.getPowerUps().get(i);
            int powerupLeft = powerup.getX();
            int powerupRight = powerup.getX() + powerup.getWidth();
            int powerupTop = powerup.getY();
            int powerupBottom = powerup.getY() + powerup.getHeight();

            // add powerup clock
            if(wizardRight > powerupLeft && this.x < powerupRight && wizardBottom > powerupTop && this.y < powerupBottom){
                // add powerup to temp
                temp.add(powerup);
                Levels.removePowerUps(powerup);
                powerupEnabled = true;
                // instantiate powreupClock
                powerupTimer = 10 * App.FPS; 
                powerupClock = new Clock(470, 685, App.powerup, 10);
                Levels.addClock(powerupClock);
            }
            i++;
        }
    }

    // get projectile timer
    public int getProjectileTimer(){
        return this.projectileTimer;
    }

    // get cooldown
    public int getCoolDown(){
        return this.cooldown;
    }

    // update wizard on the screen
    public void update(){
        // reset level
        touchSlime(); 
        touchPowerup();
        
        // change level
        if(App.door.getX() == this.x && App.door.getY() == this.y){
            App.levels.changeLevel();
        }

        // shoot fireball
        if (App.spacePressed == true && projectileTimer == 0){
            Projectile fireball = new Projectile(x, y, App.fireball);
            fireball.type = "fireball";
            // check direction with wizard sprite
            if (image == App.wizardUp){
                fireball.up = true;
                // check if you can fire up
                if (isFree(fireball.x, fireball.y - speed))
                    fireball.setCondition();
            } else if (image == App.wizardDown){
                fireball.down = true;
                // check if you can fire down
                if (isFree(fireball.x, fireball.y + speed))
                    fireball.setCondition();
            } else if (image == App.wizardLeft){
                fireball.left = true;
                // check if you can fire left
                if (isFree(fireball.x - speed, fireball.y))
                    fireball.setCondition();
            } else if (image == App.wizardRight){
                fireball.right = true;
                // check if you can fire right
                if (isFree(fireball.x + speed, fireball.y))
                    fireball.setCondition();
            }
            fireball.speed = 4; 
            Levels.addProjectile(fireball);
            App.spacePressed = false; 
            projectileTimer = cooldown;
        }

        // decrement projectile timer
        if (projectileTimer > 0){
            projectileTimer--; 
        }

        // decrement powerup timer
        if (powerupTimer > 0){
            powerupTimer--;
        }

        // set moving to false
        if(moving == false){
            if (App.upPressed == true){
                up = true;
                moving = true;
            } else if (App.downPressed == true){
                down = true; 
                moving = true; 
            } else if (App.leftPressed == true){
                left = true;
                moving = true;  
            } else if (App.rightPressed == true){
                right = true;
                moving = true;  
            } 
            
            // if powerupEnabled 
            if (powerupEnabled){
                this.speed = 4;
                // disable powerup, remove clock
                if (powerupTimer == 0){
                    powerupEnabled = false;
                    this.speed = 2;
                    for(Powerup p : temp){
                        Levels.addPowerUps(p);
                    }
                }
            }
        }

        // set moving to true
        if(moving == true){            
            if(up){
                this.image = App.wizardUp;
            } else if (down){
                this.image = App.wizardDown;
            } else if (left){
                this.image = App.wizardLeft;
            } else if (right){
                this.image = App.wizardRight;
            } 
        
            // move the wizard
            if(left && isFree(this.x - speed, this.y)) {
                this.x -= speed; 
            } else if(right && isFree(this.x + speed, this.y)) {
                this.x += speed;
            } else if(down && isFree(this.x, this.y + speed)) {
                this.y += speed;
            } else if(up && isFree(this.x, this.y - speed)){
                this.y -= speed; 
            }

            // increment the pixelCounter
            pixelCounter += speed; 

            // stop moving when the pixelCounter equals tilewidth
            if (pixelCounter == Levels.getTileWidth()){
                moving = false; 
                pixelCounter = 0;
                up = false; 
                down = false; 
                left = false; 
                right = false; 
            }
        }
        
    }   
}