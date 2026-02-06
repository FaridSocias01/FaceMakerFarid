package edu.up;
import android.graphics.Canvas;
import android.graphics.Color;
import java.util.Random;

/**
 * DATE: 2/5/2026
 * @author Farid Socias
 */

public class Face {
    private int skinColor;
    private int eyeColor;
    private int hairColor;
    private int hairStyle;
    private final Random rng = new Random();

    // Simple constructor for Face
    public Face(){
        randomize();
    }

    //Method for constructor use a random color for the skin, eyes, hair. Also a random for hairStyle
    public void randomize() {
        skinColor = randomColorInt();
        eyeColor = randomColorInt();
        hairColor = randomColorInt();
        // Bound: 5 because it could be 5 different styles
        hairStyle = rng.nextInt(5);
    }

    //Method needed for randomize() to use colors
    private int randomColorInt() {
        int r = rng.nextInt(256);
        int g = rng.nextInt(256);
        int b = rng.nextInt(256);
        return Color.rgb(r, g, b);
    }
    public void onDraw(Canvas canvas) {
        // Empty for now because we will use it later for the face
    }
    // Helper methods for onDraw
    private void drawSkin(Canvas canvas) {
        //empty
    }
    private void drawEyes(Canvas canvas) {
        //empty
    }
    private void drawHair(Canvas canvas) {
        //empty
    }

}
