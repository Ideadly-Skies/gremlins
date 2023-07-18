package gremlins;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

// class levels used to store our map
public class Levels {
    
    // attributes
    public static String jsonFileLocation;

    // stores level related information
    private static int currentLevel;
    private static int levelLoaded; 
    private static ArrayList<String> getLevels;
    private static ArrayList<ArrayList<String>> levelString; 
    private static ArrayList<ArrayList<Tile>> levelTile; 

    // entities and projectiles ArrayList
    private static ArrayList<Entity> allEntities; 
    private static ArrayList<Projectile> projectileList; 
    private static final int tileWidth = 20;

    // powerup arrayList
    private static ArrayList<Powerup> powerupArray;

    // clock arrayList
    private static ArrayList<Clock> clockArray; 
    
    // stores wizard cooldown information
    private static ArrayList<Float> wizardCoolDownValues;
    // stores gremlin cooldown information
    private static ArrayList<Float> gremlinCoolDownValues;

    // grab the current level's cooldown value
    private static float wizardCoolDown;
    private static float gremlinCoolDown;

    // constructor for map class
    public Levels() {
        // index for level
        currentLevel = 1;
        getLevels = new ArrayList<String>();
        wizardCoolDownValues = new ArrayList<Float>();
        gremlinCoolDownValues = new ArrayList<Float>();
    }

    // setup the level
    public void setup(){
        // change level based off from configPath
        jsonFileLocation = App.configPath;
        loadJson();
    }

    // return tile width
    public static int getTileWidth(){
        return tileWidth;
    }

    // return the map's level layout
    public static ArrayList<ArrayList<String>> getLevelString() {
        return levelString;
    }

    // return the current Level from the level instance
    public int getCurrentLevel(){
        return currentLevel;
    }

    // get level size
    public int getLevelSize(){
        return getLevels.size();
    }

    // get wizard cooldown
    public static float getWizardCoolDown(){
        return wizardCoolDown;
    }

    // get gremlin cooldown
    public static float getGremlinCoolDown(){
        return gremlinCoolDown;
    }
    
    // load config
    public void loadJson(){
        try{
            File readConfig = new File(jsonFileLocation);
            Scanner sc = new Scanner(readConfig);

            String configurations = "";

            while(sc.hasNext()){
                configurations = configurations + sc.nextLine();
            }

            JSONObject config = JSONObject.parse(configurations);
            
            JSONArray Extractlevels = config.getJSONArray("levels");
            
            // instantiate lives Object in App
            int lives = config.getInt("lives");
            App.noOfLives = new Lives(85, 685, App.lives, lives);
            
            for (int i = 0; i < Extractlevels.size(); i++){
                String level = Extractlevels.getJSONObject(i).getString("layout");
                Float wizardFloat = Extractlevels.getJSONObject(i).getFloat("wizard_cooldown");
                Float gremlinFloat = Extractlevels.getJSONObject(i).getFloat("enemy_cooldown"); 
                getLevels.add(level);
                wizardCoolDownValues.add(wizardFloat);
                gremlinCoolDownValues.add(gremlinFloat);
            }
            
            sc.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    // extract level 
    public static ArrayList<ArrayList<String>> loadTextLevel() {
        ArrayList<ArrayList<String>> layout = new ArrayList<ArrayList<String>>();
        
        // get current levels 
        String level = getLevels.get(currentLevel - 1);
        // assign cooldown values
        wizardCoolDown = wizardCoolDownValues.get(currentLevel - 1);
        gremlinCoolDown = gremlinCoolDownValues.get(currentLevel - 1);

        try{
            File readLevel = new File(level);
            Scanner getRows = new Scanner(readLevel);
            int rows = 0;
            
            while(getRows.hasNextLine()) {
                getRows.nextLine();
                rows++;
            }

            getRows.close();

            for(int i = 0; i < rows; i++) {
                layout.add(new ArrayList<String>());
            }

            int rowNum = 0;

            Scanner readLayout = new Scanner(readLevel);
            while(readLayout.hasNextLine()) {
                String[] row = readLayout.nextLine().split("");
                for(int i = 0; i < row.length; i++) {
                    String letter = row[i];
                    layout.get(rowNum).add(letter);
                }
                rowNum++;
            }
            readLayout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return layout;
    }

    // we are creating a level tile so we can draw it
    public ArrayList<ArrayList<Tile>> createLevelTile() {
        levelTile = new ArrayList<ArrayList<Tile>>();
        allEntities = new ArrayList<Entity>();
        projectileList = new ArrayList<Projectile>();
        powerupArray = new ArrayList<Powerup>();
        clockArray = new ArrayList<Clock>();
        ArrayList<ArrayList<String>> layout = loadTextLevel();
        
        for(int i = 0; i < layout.size(); i++) {

            levelTile.add(new ArrayList<Tile>());

            for(int j = 0; j < layout.get(i).size(); j++) {
                if(layout.get(i).get(j).equalsIgnoreCase("x")){
                    levelTile.get(i).add(new StoneWall(j * tileWidth, i * tileWidth, App.stonewall));
                } else if (layout.get(i).get(j).equalsIgnoreCase("b")) {
                    levelTile.get(i).add(new BrickWall(j * tileWidth, i * tileWidth, App.brickwall));
                } else if (layout.get(i).get(j).equalsIgnoreCase("e")) {
                    levelTile.get(i).add(new Door(j * tileWidth, i * tileWidth, App.oak_door));
                    App.door = new Door(j * tileWidth, i * tileWidth, App.oak_door);
                } else if (layout.get(i).get(j).equalsIgnoreCase("w")) {
                    App.wizard = new Wizard(j * tileWidth, i * tileWidth, App.wizardRight);
                    allEntities.add(App.wizard);
                } 
                else if (layout.get(i).get(j).equalsIgnoreCase("g")){
                    Gremlins grem = new Gremlins(j * tileWidth, i * tileWidth, App.gremlin);
                    allEntities.add(grem);
                
                    // choose random start direction for gremlin
                    ArrayList<String> moves = new ArrayList<String>();
                    moves.add("up");moves.add("down");moves.add("left");moves.add("right");
                    Random rand = new Random();
                    int ind = rand.nextInt(moves.size());
                    String direction = moves.get(ind);

                    // set the appropriate direction
                    switch(direction){
                        case "up" : grem.up = true; 
                        case "down": grem.down = true; 
                        case "left": grem.left = true; 
                        case "right": grem.right = true; 
                    }
                }
                else if (layout.get(i).get(j).equalsIgnoreCase("c")){
                    ChadGremlin chad = new ChadGremlin(j * tileWidth, i * tileWidth, App.chad);
                    allEntities.add(chad);
                
                    // choose random start direction for gremlin
                    ArrayList<String> moves = new ArrayList<String>();
                    moves.add("up");moves.add("down");moves.add("left");moves.add("right");
                    Random rand = new Random();
                    int ind = rand.nextInt(moves.size());
                    String direction = moves.get(ind);

                    // set the appropriate direction
                    switch(direction){
                        case "up" : chad.up = true; 
                        case "down": chad.down = true; 
                        case "left": chad.left = true; 
                        case "right": chad.right = true; 
                    }
                }
            }
        // update levelString 
        levelString = layout;
        }
        return levelTile;
    }
    
    // display the level on the PApplet
    public void displayLevel(PApplet app) {
        
        if((currentLevel > levelLoaded && !App.win)) {
            App.reset = false;
            levelTile = createLevelTile();
            levelLoaded++;
        } else if(App.reset) {
            App.reset = false;
            levelTile = createLevelTile();
        }
        
        for(int i = 0; i < levelTile.size(); i++) {
            for(int j = 0; j < levelTile.get(i).size(); j++) {
                levelTile.get(i).get(j).draw(app);
            }
        }
    }

    // change the current level
    public void changeLevel(){
        App.leftPressed = false; 
        App.rightPressed = false; 
        App.downPressed = false; 
        App.upPressed = false; 
        App.powerupFrames = 0;
        
        if(currentLevel != getLevels.size()){
            currentLevel++; 
        } else {
            App.win = true;
        }
    }

    // getter method for tile 
    public static ArrayList<ArrayList<Tile>> getLevelTile(){
        return levelTile;
    }

    /*
     * Entities related methods
    */

    // draws all entities on the PApplet screen
    public void drawEntities(PApplet app){
        for(Entity entity : allEntities){
            entity.draw(app);
        }
    }

    // calls the update method for people in the game
    public void updateEntities(){
        int i = 0;
        while (i < allEntities.size()){
            Entity entity = allEntities.get(i);
            entity.update();
            i++;
        }
    }

    // getter method for entities
    public static ArrayList<Entity> getEntities(){
        return allEntities;
    }

    // spawn gremlin 
    public static void addGremlin(Gremlins gremlin){
        for(int i = 0; i < levelString.size(); i++) {
            for(int j = 0; j < levelString.get(i).size(); j++){
                if (levelString.get(i).get(j).equalsIgnoreCase(" ")){
                    int respawnX = j * tileWidth; 
                    int respawnY = i * tileWidth; 
                    // check if this is true
                    if (((respawnX) >= App.wizard.getX() + 200 || (respawnX) <= App.wizard.getX() - 200) &&
                    ((respawnY) >= App.wizard.getY() + 200 || (respawnY) <= App.wizard.getY() - 200)){
                        gremlin.x = respawnX; 
                        gremlin.y = respawnY;
                    }
                }
            }
        }
        
        // choose random start direction for gremlin
        ArrayList<String> moves = new ArrayList<String>();
        moves.add("up");moves.add("down");moves.add("left");moves.add("right");
        Random rand = new Random();
        int ind = rand.nextInt(moves.size());
        String direction = moves.get(ind);

        // set the appropriate direction
        switch(direction){
            case "up" : gremlin.up = true; gremlin.setCondition(gremlin.isFree(gremlin.x, gremlin.y - gremlin.speed) && gremlin.down);
            case "down": gremlin.down = true; gremlin.setCondition(gremlin.isFree(gremlin.x, gremlin.y + gremlin.speed) && gremlin.down);
            case "left": gremlin.left = true; gremlin.setCondition(gremlin.isFree(gremlin.x - gremlin.speed, gremlin.y) && gremlin.left);
            case "right": gremlin.right = true; gremlin.setCondition(gremlin.isFree(gremlin.x + gremlin.speed, gremlin.y) && gremlin.right);
        }

        // set condition of gremlin



        allEntities.add(gremlin);
    }

    // remove gremlin from the list (using object)
    public static void removeGremlin(Gremlins gremlin){
        allEntities.remove(gremlin);
    }

    // draw projectile into the app
    public void drawProjectile(PApplet app){
        // draw projectile
        int i = 0; 
        while (i < projectileList.size()){
            Projectile currentProjectile = projectileList.get(i);
            currentProjectile.draw(app);
            i++;
        }
    }

    // calls the update method for projectile in the game
    public void updateProjectile(){
        
        int i = 0;
        while (i < projectileList.size()){
            Projectile currentProjectile = projectileList.get(i);
            if (currentProjectile.getCondition() == false){
                projectileList.remove(projectileList.indexOf(currentProjectile));
            } 
            currentProjectile.update();
            i++; 
        }

    }
    
    // reset projectile condition
    public static void resetProjectile(Projectile projectile){
        int i = 0;
        while (i < projectileList.size()){
            Projectile currentProjectile = projectileList.get(i);
            if (currentProjectile.equals(projectile)){
                currentProjectile.resetCondition();
                projectileList.set(i, currentProjectile);
            }
            i++;
        }
    }

    // get projectile list
    public static ArrayList<Projectile> getProjectileList(){
        return projectileList;
    }

    // add projectile into the list 
    public static void addProjectile(Projectile projectile){
        projectileList.add(projectile);
    } 

    // clash of projectiles (fireball vs slime)
    public void clashProjectiles(){
        int i = 0;
        while (i < projectileList.size()){
            Projectile fireball = projectileList.get(i);
            int j = 0;
            while (j < projectileList.size()){
                Projectile slime = projectileList.get(j);
                if (fireball.type == "fireball"){
                    // grab information for fireball (main)
                    int fireballRight = fireball.x + fireball.getWidth();
                    int fireballBottom = fireball.y + fireball.getHeight();
                    if (slime.type == "slime"){
                        // grab information from slime (target)
                        int slimeLeft = slime.getX();
                        int slimeRight = slime.getX() + slime.getWidth();
                        int slimeTop = slime.getY();
                        int slimeBottom = slime.getY() + slime.getHeight();

                        // remove both titans!!!
                        if(fireballRight > slimeLeft && fireball.x < slimeRight && fireballBottom > slimeTop && fireball.y < slimeBottom){
                            projectileList.remove(fireball);
                            projectileList.remove(slime);
                        }
                    }
                }
                j++;
            }
            i++;
        }
    }

    // get powerup arrayList
    public static ArrayList<Powerup> getPowerUps(){
        return powerupArray;
    }

    // remove powerup from the arrayList
    public static void removePowerUps(Powerup powerup){
        powerupArray.remove(powerup);
    }

    // add powerup to the arrayList
    public static void addPowerUps(Powerup powerup){
        powerupArray.add(powerup);
    }

    // draw powerup into app (using level)
    public void drawPowerUps(PApplet app){
        int i = 0; 
        // loop through array and draw powerup
        while (i < powerupArray.size()){
            Powerup powerup = powerupArray.get(i);
            powerup.draw(app);
            i++;
        }
    }

    // spawn powerups in app class
    public void spawnPowerUps(){
        ArrayList<ArrayList<String>> layout = loadTextLevel();
        for(int i = 0; i < layout.size(); i++) {
            for(int j = 0; j < layout.get(i).size(); j++) {
                if (layout.get(i).get(j).equalsIgnoreCase("p")){
                    powerupArray.add(new Powerup(j * tileWidth, i * tileWidth, App.powerup));
                }
            }
        }
    }

    // remove clock 
    public static ArrayList<Clock> getClock(){
        return clockArray;
    }

    // add clock into the arrayList clock
    public static void addClock(Clock clock){
        clockArray.add(clock);
    }

    // remove clock from the arrayList clock
    public static void removeClock(Clock clock){
        clockArray.remove(clock);
    }

    // update clock from the arrayList clock
    public void updateClock(){
        int i = 0; 
        while (i < clockArray.size()){
            Clock clock = clockArray.get(i);
            clock.update();

            // remove clock if timer is <= 0
            if (clock.getTimer() <= 0){
                removeClock(clock);
            }
            
            i++; 
        }
    }

    // draw clock from the arrayList clock
    public void drawClock(PApplet app){
        int i = 0; 
        while (i < clockArray.size()){
            Clock clock = clockArray.get(i);
            clock.draw(app);
            i++;
        }
    }

    /**
     * Remove brickwall upon colliding with fireball
     * @param j, index j for the inner arrayList inside levelString
     * @param i, index i for the outer arrayList of levelString
     */
    public void removeWall(int j, int i){
        // if wall is of type brickwall
        if (levelString.get(i).get(j).equalsIgnoreCase("b")){
                App.initDestructSeq = true;
                App.xPos = j * tileWidth;
                App.yPos = i * tileWidth;
                levelTile.get(i).add(new EmptyTile(j * tileWidth, i * tileWidth, App.emptytile));
                levelString.get(i).set(j, " ");
            }
    }

}   