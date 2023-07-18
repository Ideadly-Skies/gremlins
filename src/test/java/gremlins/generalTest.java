package gremlins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import processing.core.PApplet;

import org.junit.jupiter.api.Test;

public class generalTest {
    
    @Test
    // check constructor of wizard
    public void constructor(){
        assertNotNull(new Wizard(30, 200, App.wizardRight));
    }   
    
    @Test
    // test damage
    public void testdie(){
        App app = new App();
        // extensionconfig / powerup
        App.configPath = "projectileTest.json";
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);        
        app.setup();
        app.rightPressed = true;
        app.spacePressed = true; 
        app.spacePressed = true;
        app.spacePressed = true;
        app.spacePressed = true;
        app.delay(8000);

        app.spacePressed = true;
        app.spacePressed = true;
        app.spacePressed = true;
    }

    @Test
    // test idle
    public void testMovement(){
        App app = new App();
        // extensionconfig / powerup
        App.configPath = "powerupTest.json";
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.spacePressed = true;
        app.rightPressed = true; 
        app.delay(30000);
        app.spacePressed = true;
        app.spacePressed = true;
        app.spacePressed = true;
    }

    @Test
    // test keyPressed
    public void testkeyPressed(){
        App app = new App();
        // move left
        app.keyCode = 37; 
        app.keyPressed();
        assertEquals(app.leftPressed, true);
        app.keyReleased();
        assertEquals(app.leftPressed, false);
        
        // move right
        app.keyCode = 39; 
        app.keyPressed();
        assertEquals(app.rightPressed, true);
        app.keyReleased();
        assertEquals(app.rightPressed, false);
        
        // move up
        app.keyCode = 38; 
        app.keyPressed();
        assertEquals(app.upPressed, true);
        app.keyReleased();
        assertEquals(app.upPressed, false);
        
        // move down
        app.keyCode = 40; 
        app.keyPressed();
        assertEquals(app.downPressed, true);
        app.keyReleased();
        assertEquals(app.downPressed, false);
        
        // space pressed
        app.keyCode = 32; 
        app.keyPressed();
        assertEquals(app.spacePressed, true);
        app.keyReleased();
        assertEquals(app.spacePressed, false);
    }
}

