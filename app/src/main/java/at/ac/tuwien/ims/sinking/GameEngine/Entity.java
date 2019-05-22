package at.ac.tuwien.ims.sinking.GameEngine;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Base class of every entity in game
 *
 * @author Matthias H&uuml;rbe
 */
public class Entity implements Comparable {

    /**
     * Location of entity in x
     */
    public int x;

    /**
     * Location fo entity in y
     */
    public int y;

    /**
     * depth of entity for depth sorting
     */
    public int depth = 10;

    /**
     * Reference to the game view
     */
    protected GameView context;

    /**
     * Enity Ctor
     * @param context reference to the game view
     * @param x entity location in x
     * @param y entity location in y
     */
    public Entity(GameView context, int x, int y) {
        this.context = context;
        this.x = x;
        this.y = y;
    }

    /**
     * Draws the entity to a canvas
     * @param canvas
     * @param view
     */
    public void draw( Canvas canvas, Matrix view) {
    }

    /**
     * Updates the logic of the entity
     * @param dt
     */
    public void update(double dt) {
    }

    /**
     * Called after the level finished loading. Override to setup game logic
     */
    public void postLoad(){}

    public void loadFromJson(JSONObject jsonObject) throws JSONException
    {
            x = jsonObject.getInt("x");
            y = jsonObject.getInt("y");
    }

    /**
     * compares the depth of the element for draw command sorting
     * @param other reference to the other element;
     * @return
     */
    @Override
    public int compareTo(Object other) {
        return depth - ((Entity)other).depth;
    }

    /**
     * Returns an instance of an AABB for this entity
     * @return null if the entity does not collide
     */
    public AABB getAABB(){
        return null;
    }

    /**
     * Called by the player when he presses the interact button
     */
    public void interact(Player player){

    }
}
