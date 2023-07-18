package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Lives extends Object {
    
    // private attributes
    private int lives; 
    private ArrayList<PImage> livesArray;
    
    /**
     * Constructor for Lives object which represents the lives of the wizard, displayed on the topbar of the game's GUI.
     * Lives are displayed 20 pixels apart from each other, and are stored in an arrayList of PImage called livesArray. The 
     * attribute lives represent the number of lives a wizard have as specified in the config.json file. 
     * @param x X-Position of the Lives
     * @param y Y-Position of the Lives
     * @param sprite Image of the Lives 
     * @param lives Number of lives the wizard starts with
     */
    public Lives(int x, int y, PImage sprite, int lives){
        super(x, y, sprite);
        this.lives = lives;

        // initialize array of size lives
        livesArray = new ArrayList<PImage>();
        for (int i = 0; i < lives; i++){
            livesArray.add(App.wizardRight);
        }
    }
    
    // draw live on the screen
    public void draw(PApplet app){
        // display Lives text
        app.text("Lives: ", 50, 700);
        
        int xInc = 0;
        for(int i = 0; i < livesArray.size(); i++){
            // break if xInc equals livesArray.size() * 20
            if (xInc == livesArray.size() * 20){
                break;
            }
            app.image(livesArray.get(i), this.x + xInc, this.y);
            xInc += 20;
        }
    }

    // update live on the screen
    public void update(){
        if (this.lives == 0){
            App.lose = true;
        }
    }

    // decrease live 
    public void decreaseLives(){
        if(this.lives > 0){
            this.lives--;
            // remove lives from livesArray
            livesArray.remove(livesArray.size()-1);
            App.reset = true; 
            App.powerupFrames = 0; 
        }
    }

    // return number of lives
    public int getLives(){
        return this.lives; 
    }
}
