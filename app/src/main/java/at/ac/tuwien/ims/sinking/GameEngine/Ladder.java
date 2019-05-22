package at.ac.tuwien.ims.sinking.GameEngine;


import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;

import org.json.JSONException;
import org.json.JSONObject;

import at.ac.tuwien.ims.sinking.R;

/**
 * Entity that defines a ladder for the player, connects two lanes
 *
 * @author Matthias Huerbe
 */
public class Ladder extends Entity {

    private String lane1Name;
    private String lane2Name;
    public Lane lane1;
    public Lane lane2;
    private int ladderPos;
    private final int ladderWidth = 100;
    private final int ladderOverhang = 100;
    private int ladderLeft;
    private int ladderTop;
    private int ladderRight;
    private int ladderBottom;
    private AABB ladderAABB;

    /**
     * Drawable for the ladder visuals
     */
    private BitmapDrawable bd;

    public Ladder(GameView context, int x, int y) {
        super(context, x, y);
        depth = 1;

        bd = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ladder_step) );
        bd.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    @Override
    public void loadFromJson(JSONObject jsonObject) throws JSONException {
        //super.loadFromJson(jsonObject);

        lane1Name = jsonObject.getString("lane1Name");
        lane2Name = jsonObject.getString("lane2Name");
        ladderPos = jsonObject.getInt("ladderPos");
    }

    @Override
    public void postLoad() {
        super.postLoad();

        lane1 = context.lanes.get(lane1Name);
        lane2 = context.lanes.get(lane2Name);

        ladderLeft = ladderPos;
        ladderTop = (lane1.height < lane2.height ? lane1.height : lane2.height);
        ladderRight = ladderLeft + ladderWidth;
        ladderBottom = (lane1.height < lane2.height ? lane2.height : lane1.height) + ladderOverhang;

        ladderAABB = new AABB( ladderLeft, ladderTop, 100, ladderBottom - ladderTop );
    }

    @Override
    public void draw(Canvas canvas, Matrix view) {
        super.draw(canvas, view);

        Matrix matrix = new Matrix();
        matrix.setTranslate(0, 0);
        matrix.setConcat(matrix, view);

        float[] points = new float[]{ladderLeft, ladderTop, ladderRight, ladderBottom};
        matrix.mapPoints(points);

        bd.setBounds((int)points[0], (int)points[1], (int)points[2], (int)points[3]);
        bd.setGravity(Gravity.FILL);
        bd.draw(canvas);

    }

    @Override
    public AABB getAABB() {
        return ladderAABB;
    }

    @Override
    public void interact(Player player) {
        player.startClimbing(this);
    }
}
