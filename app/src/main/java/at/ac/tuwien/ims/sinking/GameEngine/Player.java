package at.ac.tuwien.ims.sinking.GameEngine;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import at.ac.tuwien.ims.sinking.R;

/**
 * Player managing movement, animations and climbing and interactions
 *
 * @author Matthias H&uuml;rbe
 */
public class Player extends Entity {

    private boolean isMoving = false;
    private double speed = 250;
    private int input = 0;
    private Lane lane;

    // climbing
    boolean isClimbing = false;
    // target to climb to
    int climbingTarget;

    private SpriteAnimationPlayer animationPlayer = new SpriteAnimationPlayer();
    private SpriteAnimation walkRightAnim, walkLeftAnim;

    // ref to an interactable
    private Entity interactable = null;

    public Player(GameView context, int x, int y) {
        super(context, x, y);
        depth = 5;

        walkRightAnim = new SpriteAnimation(context,R.drawable.player_right, 64, 111, 5);
        walkRightAnim.loadBitmap();
        walkLeftAnim = new SpriteAnimation(context, R.drawable.player_left, 64, 111, 5);
        walkLeftAnim.loadBitmap();

        animationPlayer.playAnimation(walkRightAnim);
        animationPlayer.setPlaying(true);
    }

    @Override
    public void draw(Canvas canvas, Matrix view) {

        Matrix matrix = new Matrix();
        matrix.setTranslate(x, y);
        matrix.setConcat(matrix, view);

        if(animationPlayer != null)
        {
            animationPlayer.draw(canvas, matrix);
        }

    }

    @Override
    public void update(double dt)
    {
        if(isClimbing)
        {
            doClimbing(dt);
            return;
        }

        isMoving = input != 0;

        // move player and clamp to lane
        int oldPos = x;
        x += input * speed * dt;
        x = Math.max( lane.start, Math.min(lane.end, x));

        int dx = x - oldPos;

        // update animation system
        if(input >= 1 && animationPlayer.isPlaying == false){
            animationPlayer.playAnimation(walkRightAnim);
        }
        else if (input <= -1 && animationPlayer.isPlaying == false){
            animationPlayer.playAnimation(walkLeftAnim);
        }
        else if(dx == 0)
        {
            animationPlayer.setPlaying(false);
        }

        checkForInteractable();

        Log.e("Sinking", "Interactable :" + (interactable != null ? interactable.toString() : "None") );
    }

    /**
     *  Pass movement input to player
     *  Gets called by the game view
     *  */
    public void SetMovementInput(int newInput)
    {
        input = newInput;
    }

    /**
     * Trigger interactable action
     */
    public void onInteract(){
        if(interactable != null){
            interactable.interact(this);
            interactable = null;
        }
    }

    /**
     * Sets the starting location of the player
     *
     * Gets called during initialisation
     */
    public void setPlayerStart(PlayerStart playerStart){
        lane = context.lanes.get(playerStart.lane);
        int pos = playerStart.pos;

        x = lane.start + pos;
        y = lane.height;

        Log.e("Sinking", "Playerstart x:"+x+" y:"+y);
    }

    /**
     * Check if we are overlapping with an interactable
     */
    private void checkForInteractable()
    {
        AABB selfAABB = getAABB();
        interactable = null;

        // iterate over entities
        for(int i = 0; i < context.entities.size(); i++)
        {
            Entity other = context.entities.get(i);

            // ignore self
            if( this != other){
                AABB otherAABB = other.getAABB();

                // not all entities have an AABB
                if(otherAABB != null)
                {
                    // check for overlap
                    if(AABB.collide(selfAABB, otherAABB)){

                        // cache interactable
                        interactable = other;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public AABB getAABB() {
        return new AABB( x, y,70, 100);
    }

    /**
     * Start climbing. called by the ladder
     * @param ladder
     */
    public  void startClimbing(Ladder ladder){

        isClimbing = true;
        animationPlayer.playAnimation(walkRightAnim);

        if(lane == ladder.lane1){
            climbingTarget = ladder.lane2.height;
            lane = ladder.lane2;
        }
        else{
            climbingTarget = ladder.lane1.height;
            lane = ladder.lane1;
        }

    }

    /**
     *  update player position for climbing
     * @param dt delta time
     */
    private void doClimbing(double dt){

        int direction = lane.height - y;
        direction = Math.max( -1, Math.min( 1, direction));

        // are we still climbing?
        isClimbing = direction != 0;

        y += direction * speed * dt;
        if(direction < 0)
        {
            y = Math.max(y, lane.height);
        }
        if(direction > 0)
        {
            y = Math.min(y, lane.height);
        }
    }


}
