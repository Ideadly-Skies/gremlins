package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.ArrayList;

public class App extends PApplet {

    // gamepanel attributes
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;
    
    // image attributes
    public static PImage brickwall;
    public static PImage brickwall_destroyed0;
    public static PImage brickwall_destroyed1;
    public static PImage brickwall_destroyed2;
    public static PImage brickwall_destroyed3;
    public static PImage emptytile; 
    public static PImage fireball;
    public static PImage gremlin;
    public static PImage oak_door;
    public static PImage slime; 
    public static PImage stonewall;
    public static PImage wizardLeft;
    public static PImage wizardRight;
    public static PImage wizardUp;
    public static PImage wizardDown;
    public static PImage lives; 
    public static PImage powerup;
    public static PImage chad; 

    // keyPressed attributes 
    public static boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

    // gameresult attributes
    public static boolean lose = false; 
    public static boolean win = false;
    public static boolean reset = true; 
    public static Lives noOfLives; 
    
    // static instances of objects
    public static Wizard wizard;
    public static Levels levels; 
    public static Tile door;
    
    // font used and level description
    public static PFont font; 
    public static String levelDesc;

    // attributes for destruction sequence
    public static boolean initDestructSeq;
    public ArrayList<PImage> destructSeq;
    public static int destructInd;
    public static int xPos; 
    public static int yPos;

    // number of frames to wait for powerup spawn
    public static int powerupFrames; 
    
    // attribute to increment rect length
    public static float xBarInc; 

    // config file path
    public static String configPath = "config.json"; 

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        // setup FPS
        frameRate(FPS);

        // load image resources 
        brickwall = loadImage(this.getClass().getResource("brickwall.png").getPath().replace("%20", " "));
        brickwall_destroyed0 = loadImage(this.getClass().getResource("brickwall_destroyed0.png").getPath().replace("%20", " "));
        brickwall_destroyed1 = loadImage(this.getClass().getResource("brickwall_destroyed1.png").getPath().replace("%20", " "));
        brickwall_destroyed2 = loadImage(this.getClass().getResource("brickwall_destroyed2.png").getPath().replace("%20", " "));
        brickwall_destroyed3 = loadImage(this.getClass().getResource("brickwall_destroyed3.png").getPath().replace("%20", " "));

        emptytile = loadImage(this.getClass().getResource("emptytile.png").getPath().replace("%20", " "));
        fireball = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", " "));
        gremlin = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", " "));
        oak_door = loadImage(this.getClass().getResource("oak_door.png").getPath().replace("%20", " "));
        slime = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", " "));

        stonewall = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", " "));
        wizardLeft = loadImage(this.getClass().getResource("wizard0.png").getPath().replace("%20", " "));
        wizardRight = loadImage(this.getClass().getResource("wizard1.png").getPath().replace("%20", " "));
        wizardUp = loadImage(this.getClass().getResource("wizard2.png").getPath().replace("%20", " "));
        wizardDown = loadImage(this.getClass().getResource("wizard3.png").getPath().replace("%20", " "));

        lives = loadImage(this.getClass().getResource("wizard1.png").getPath().replace("%20", " "));
        powerup = loadImage(this.getClass().getResource("powerup.png").getPath().replace("%20", " "));
        chad = loadImage(this.getClass().getResource("chad.png").getPath().replace("%20", " "));

        // setup levels 
        levels = new Levels();
        levels.setup();

        // init destructSeq
        destructSeq = new ArrayList<PImage>();
        destructSeq.add(brickwall_destroyed0);
        destructSeq.add(brickwall_destroyed1);
        destructSeq.add(brickwall_destroyed2);
        destructSeq.add(brickwall_destroyed3);
        destructInd = 0;
    }

    
    public void keyPressed(){
        
        if (keyCode == UP){
            upPressed = true; 
        } 
        else if (keyCode == DOWN){
            downPressed = true;
        }
        else if (keyCode == LEFT){
            leftPressed = true;
        } 
        else if (keyCode == RIGHT){
            rightPressed = true;
        } 
        else if (keyCode == 32){
            spacePressed = true;
        }
    }
    
    
    public void keyReleased(){    
        
        if (keyCode == UP){
            upPressed = false; 
        } 
        else if (keyCode == DOWN){
            downPressed = false;
        }
        else if (keyCode == LEFT){
            leftPressed = false;
        } 
        else if (keyCode == RIGHT){
            rightPressed = false;
        } 
        else if (keyCode == 32){
            spacePressed = false; 
        }
    }

    
    public void draw() {        
        // set beige-coloured background
        background(191, 153, 114);
        
        if(!win){
            // display level 
            levels.displayLevel(this);

            // display the current level text
            levelDesc = String.format("Level: %d/%d",levels.getCurrentLevel(),levels.getLevelSize());
            this.text(levelDesc, 170, 700);

            // initialize destruction sequence
            if(initDestructSeq && frameCount % 4 == 0){
                image(destructSeq.get(destructInd++), xPos, yPos);
                
                // reset after all destruction sprite loaded
                if (destructSeq.size() == destructInd){
                    initDestructSeq = false; 
                    destructInd = 0;
                }
            }

            // draw number of lives
            noOfLives.draw(this);
            noOfLives.update();
            
            // draw projectiles
            levels.drawProjectile(this);
            levels.updateProjectile();
            
            // spawn powerups after 10 seconds
            if(powerupFrames == 600)
                levels.spawnPowerUps();
            
            // draw powerups
            levels.drawPowerUps(this);
            
            // draw clock
            levels.drawClock(this);
            levels.updateClock();
        
            // draw the entities
            levels.drawEntities(this);
            levels.updateEntities();

            // draw cooldown bar
            if (wizard.getProjectileTimer() > 0){
                xBarInc += (100 / (Levels.getWizardCoolDown() * App.FPS)); 

                if (xBarInc < 100) {
                    fill(250);
                    rect(600, 680, 100, 7);
                    fill(0);
                    rect(600, 680, xBarInc, 7);
                } else if (xBarInc >= 100){
                    fill(250);
                    rect(600, 680, 100, 7);
                    fill(0);
                    rect(600, 680, 100, 7);
                }
            }

            // reset xBarInc
            if (wizard.getProjectileTimer() == 0){
                xBarInc = 0;
            }

            // break
            if (lose){
                win = true; 
            }
            powerupFrames++;
        } 

        // display lose screen
        if (lose && win){
            this.text("YOU LOSE", 360, 360);
        } 
        // display win screen
        else if(win){
            this.text("YOU WIN", 360, 360);
        }
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}