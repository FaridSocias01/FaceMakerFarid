package edu.up;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.Random;

/**
 *
 * Model class for a face. Stores appearance settings and draws itself
 * DATE: 2/17/2026
 *
 * @author Farid Socias
 */

public class Face {
    private int skinColor;
    private int eyeColor;
    private int hairColor;
    private int hairStyle;
    private final Random rng = new Random();
    private static final int HAIR_STYLE_COUNT = 3;
    private static final int HAIR_STYLE_MIN = 0;
    private static final int HAIR_STYLE_MAX = HAIR_STYLE_COUNT - 1;

    // Paint is reused to avoid creating new objects during drawing.
    //https://developer.android.com/reference/android/graphics/Paint
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Simple constructor for Face
    public Face(){
        randomize();
    }

    //Method for constructor use a random color for the skin, eyes, hair. Also a random for hairStyle
    public void randomize() {
        skinColor = randomColorInt();
        eyeColor = randomColorInt();
        hairColor = randomColorInt();
        // Bound: 3 because it could be 3 different styles
        hairStyle = rng.nextInt(3);
    }

    //Method needed for randomize() to use colors
    private int randomColorInt() {
        int r = rng.nextInt(256);
        int g = rng.nextInt(256);
        int b = rng.nextInt(256);
        return Color.rgb(r, g, b);
    }
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }

        canvas.drawColor(Color.WHITE);

        drawSkin(canvas);
        drawHair(canvas);
        drawEyes(canvas);
        drawSmile(canvas);
    }

    private void drawSkin(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        float centerX = w * 0.5f;
        float centerY = h * 0.5f;
        float radius = Math.min(w, h) * 0.35f;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(skinColor);

        canvas.drawCircle(centerX, centerY, radius, paint);
    }
    private void drawSmile(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        float centerX = w * 0.5f;
        float centerY = h * 0.5f;
        float faceRadius = Math.min(w, h) * 0.35f;

        float smileWidth = faceRadius * 0.55f;
        float smileHeight = faceRadius * 0.35f;

        float left = centerX - smileWidth * 0.5f;
        float right = centerX + smileWidth * 0.5f;

        float top = centerY + faceRadius * 0.10f;
        float bottom = top + smileHeight;

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(faceRadius * 0.06f);
        paint.setColor(Color.BLACK);

        RectF smileRect = new RectF(left, top, right, bottom);

        canvas.drawArc(smileRect, 0, 180, false, paint);

        paint.setStyle(Paint.Style.FILL);
    }
    private void drawEyes(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        float centerX = w * 0.5f;
        float centerY = h * 0.5f;
        float faceRadius = Math.min(w, h) * 0.35f;

        float eyeY = centerY - faceRadius * 0.15f;
        float eyeOffsetX = faceRadius * 0.38f;
        float irisRadius = faceRadius * 0.10f;

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(
                centerX - eyeOffsetX, eyeY, irisRadius * 1.35f, paint
        );
        canvas.drawCircle(
                centerX + eyeOffsetX, eyeY, irisRadius * 1.35f, paint
        );

        paint.setColor(eyeColor);
        canvas.drawCircle(centerX - eyeOffsetX, eyeY, irisRadius, paint);
        canvas.drawCircle(centerX + eyeOffsetX, eyeY, irisRadius, paint);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(
                centerX - eyeOffsetX, eyeY, irisRadius * 0.35f, paint
        );
        canvas.drawCircle(
                centerX + eyeOffsetX, eyeY, irisRadius * 0.35f, paint
        );
    }
    private void drawHair(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        float centerX = w * 0.5f;
        float centerY = h * 0.5f;
        float faceRadius = Math.min(w, h) * 0.35f;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(hairColor);

        float top = centerY - faceRadius * 1.05f;
        float left = centerX - faceRadius;
        float right = centerX + faceRadius;

        float bottom;
        if (hairStyle == 0) {
            bottom = centerY - faceRadius * 0.15f;
        } else if (hairStyle == 1) {
            bottom = centerY + faceRadius * 0.10f;
        } else {
            bottom = centerY + faceRadius * 0.45f;
        }

        RectF hairRect = new RectF(left, top, right, bottom);
        canvas.drawArc(hairRect, 180, 180, true, paint);
    }

    // Getters
    public int getSkinColor() {
        return skinColor;
    }

    public int getEyeColor() {
        return eyeColor;
    }

    public int getHairColor() {
        return hairColor;
    }

    public int getHairStyle() {
        return hairStyle;
    }

    public void setSkinColor(int color) {
        skinColor = color;
    }

    public void setEyeColor(int color) {
        eyeColor = color;
    }

    public void setHairColor(int color) {
        hairColor = color;
    }

    /* [12% Part B]
     * Spinner changes hair style.
     */
    public void setHairStyle(int style) {
        if (style < HAIR_STYLE_MIN) {
            hairStyle = HAIR_STYLE_MIN;
            return;
        }
        if (style > HAIR_STYLE_MAX) {
            hairStyle = HAIR_STYLE_MAX;
            return;
        }
        hairStyle = style;
    }

}
