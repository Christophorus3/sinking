package at.ac.tuwien.ims.sinking.GameEngine;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.Currency;

public class SpriteAnimationPlayer {

    private SpriteAnimation animation;

    private int currentFrame = 0;
    private long lastFrameChangeTime = 0;
    private int frameLengthMillis = 100;

    private RectF whereToDraw;
    private Rect frameToDraw;

    boolean isPlaying = false;

    public SpriteAnimationPlayer(){
        whereToDraw = new RectF();
        frameToDraw = new Rect();
    }

    public void draw(Canvas canvas, Matrix matrix){

        if(animation == null)
            return;

        // map into view space
        float[] newPoints = new float[]{0, 0};
        matrix.mapPoints(newPoints);
        float newX = newPoints[0];
        float newY = newPoints[1];
        whereToDraw.set(newX, newY, newX + animation.frameWidth, newY + animation.frameHeight);

        // update the currently selected frame
        updateCurrentFrame();

        // draw command

        frameToDraw.left = currentFrame * animation.frameWidth;
        frameToDraw.right = frameToDraw.left + animation.frameWidth;

        canvas.drawBitmap(animation.bitmap, frameToDraw, whereToDraw, new Paint());
    }

    private void updateCurrentFrame() {
        long time = System.currentTimeMillis();
        if (isPlaying) {
            if (time > lastFrameChangeTime + frameLengthMillis) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= animation.frameCount) {
                    currentFrame = 0;
                }
            }
        }
    }

    public void playAnimation(SpriteAnimation newAnimation){
        animation = newAnimation;
        currentFrame = 0;
        lastFrameChangeTime = 0;
        frameToDraw.set(0, 0, animation.frameWidth, animation.frameHeight);
        isPlaying = true;
    }

    public void setPlaying(boolean enable){ isPlaying = enable;}

    public SpriteAnimation getCurrentAnim() { return animation;}
}
