package at.ac.tuwien.ims.sinking.GameEngine;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import at.ac.tuwien.ims.sinking.R;

/**
 * Destructable entity <br>
 * These get "destroyed" by the game and the player has to interact with them
 *
 * @author Matthias Huerbe
 */
public class Destructable extends Entity {

    public float health = 1;
    private Bitmap destructableBitmap;
    public boolean isDamaged = false;
    final float dps = 0.05f; // 20s

    public Destructable(GameView context, int x, int y) {
        super(context, x, y);
        depth = 1;

        destructableBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dest);
    }

    @Override
    public void update(double dt) {
        super.update(dt);

        if(isDamaged){
            health -= dps * dt;

            if(health <= 0)
            {
                destructableBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dest_dest);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix view) {

        Matrix matrix = new Matrix();
        matrix.setTranslate(x, y);
        matrix.setConcat(matrix, view);

        long time = System.currentTimeMillis();
        double timeInSec = (double)time / 1000.0;

        double value = Math.sin(timeInSec * (0.2 + (1 - health) * 5)) ;

        Paint p = new Paint();
        if( value < 0.0 && isDamaged && health > 0)
        {
            p.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));
        }

        canvas.drawBitmap(destructableBitmap, matrix, p);
    }

    @Override
    public AABB getAABB() {
        return new AABB(x, y, destructableBitmap.getWidth(), destructableBitmap.getHeight());
    }

    @Override
    public void interact(Player player) {
        super.interact(player);

        if(isDamaged)
        {
            isDamaged = false;
        }
    }
}
