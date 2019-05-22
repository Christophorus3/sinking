package at.ac.tuwien.ims.sinking.GameEngine;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import org.json.JSONObject;

/**
 * sets the view port
 *
 * @author Matthias H&uuml;rbe
 */
public class Camera extends Entity{

    private Player player;
    private int canvasWidth = 100;
    private int canvasHeight = 100;

    /**
     * Ctor
     * @param context game context
     * @param x location
     * @param y location
     */
    public Camera(GameView context, int x, int y) {
        super(context, x, y);
    }

    @Override
    public void update(double dt) {
        // follow player
        if(player != null){
            x = player.x - canvasWidth / 2;
            y = player.y - canvasHeight / 2;
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix view) {
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
    }

    /**
     * Returns the view matrix of the camera
     * @return view matrix
     */
    public Matrix getViewMatrix() {
        Matrix view = new Matrix();
        view.setTranslate( x, y);
        view.invert(view);

        return view;
    }

    public void setPlayer(Player newPlayer){
        player = newPlayer;
    }

}

