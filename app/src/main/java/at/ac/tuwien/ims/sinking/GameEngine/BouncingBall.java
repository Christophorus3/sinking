package at.ac.tuwien.ims.sinking.GameEngine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.json.JSONObject;

import at.ac.tuwien.ims.sinking.R;

/**
 * Bouncing ball entity for testing
 * @author Matthias H&uuml;rbe
 */
public class BouncingBall extends Entity{

    Bitmap bitmap;
    float mass;
    float v;
    float a;


    public BouncingBall(GameView context, int x, int y){
        super(context, x, y);
        depth = 1;

        bitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.dest);
        mass = 3;
        v = 0;
        a = 0;
    }

    @Override
    public void draw( Canvas canvas, Matrix view) {

        Matrix matrix = new Matrix();
        matrix.setTranslate(x, y);
        matrix.setConcat(matrix, view);

        canvas.drawBitmap(bitmap, matrix, new Paint());
    }

    @Override
    public void update(double dt) {
        float force = 0.5f;
        float a1 = force / mass;
        a += a1;

        v += a;
        y += v * dt;

        a = 0;

        checkBoundary();
    }

    private void checkBoundary() {
        if(this.context.getHeight() == 0)
        {
            return;
        }

        if(y > this.context.getHeight() - 128)
        {
            y = this.context.getHeight() - 128;
            v *= -1;
        }
        else if(y <= 0)
        {
            y = 0;
            v *= -1;
        }
    }
}
