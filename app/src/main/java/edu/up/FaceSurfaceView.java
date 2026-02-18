package edu.up;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * View class that draws a Face on a SurfaceView.
 * DATE: 2/17/2026
 * @author Farid Socias
 */
public class FaceSurfaceView extends SurfaceView
        implements SurfaceHolder.Callback {

    private Face face;

    public FaceSurfaceView(Context context) {
        super(context);
        initialization();
    }

    public FaceSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialization();
    }

    public FaceSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialization();
    }

    private void initialization() {
        face = new Face();
        getHolder().addCallback(this);
        setFocusable(true);
    }

    public Face getFace() {
        return face;
    }

    public void drawNow() {
        SurfaceHolder holder = getHolder();
        Canvas canvas = null;

        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                face.draw(canvas);
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawNow();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawNow();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}
}