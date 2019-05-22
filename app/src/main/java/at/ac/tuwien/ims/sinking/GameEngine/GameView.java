package at.ac.tuwien.ims.sinking.GameEngine;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Map;


import at.ac.tuwien.ims.sinking.GameEngine.UI.InputHandle;
import at.ac.tuwien.ims.sinking.GameEngine.UI.UIElement;
import at.ac.tuwien.ims.sinking.GameEngine.UI.UIProgress;
import at.ac.tuwien.ims.sinking.R;

import static at.ac.tuwien.ims.sinking.Util.formatTime;

/**
 * This class is shows the actual canvas the game loop is drawing to.</br>
 *
 * Code for the basic game loop is used from: <a href="http://gamecodeschool.com/android/building-a-simple-game-engine/">gamecodeschool.com</a>
 *
 * @author Christoph Wottawa, Matthias Huerbe
 */

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;

    private SoundPool soundPool;
    private boolean soundPoolLoaded = false;
    private int soundIdBackground;

    private MediaPlayer mp;

    private SurfaceHolder surfaceHolder;

    private OnGameOverListener gameOverListener;

    //is set while game thread is running
    private volatile boolean running;

    private boolean isPaused = false;
    private boolean isMusicPlaying = true;

    private boolean allDestroyed = false;

    private Canvas canvas;
    private Paint paint;

    private long fps;

    private long frameTime;

    private long levelTime = 23046;
    private long timeLeft = levelTime;

    /** Holds all entities in the level */
    public ArrayList<Entity> entities;
    /** Holds a list of all UI elements */
    ArrayList<UIElement> uiElements;
    UIProgress progress;

    /** Holds a list of lanes for player navigation */
    public HashMap<String, Lane> lanes;
    /** Position the Player starts at */
    public PlayerStart playerStart;

    /** Reference to the camera */
    private Camera camera;

    /** Reference to the player */
    private Player player;

    double lastObjectDamagedTime = 0;
    double delayBetweenDamaging = 5;

    public GameView(Context context, String levelName) {
        super(context);

        surfaceHolder = getHolder();
        paint = new Paint();

        // create entity array
        entities = new ArrayList<>();
        lanes = new HashMap<>();

        // load level from file
        LevelLoader levelLoader = new LevelLoader(this, levelName);
        levelLoader.loadLevel();

        // create camera and player
        player = (Player)spawnEntity(Player.class, 90, 704, null);
        player.setPlayerStart(playerStart);
        camera = (Camera)spawnEntity(Camera.class, 0, 0, null);
        camera.setPlayer(player);

        // create game UI
        createGameUI();

        // call post load on the entities
        for (int i = 0; i < entities.size(); i++){
            entities.get(i).postLoad();
        }
    }

    @Override
    public void run() {
        playSound();
        long startFrameTime = System.currentTimeMillis();
        while (running) {
            // calculate delta time in seconds
            long now = System.currentTimeMillis();
            frameTime = isPaused? 0 : now - startFrameTime;
            //frameTime = frameTime * scale;
            double deltaTime = frameTime / 1000.0;

            startFrameTime = now;

            //subtract from levelTime
            timeLeft = timeLeft - frameTime;
            if (timeLeft < 0) {
                if (gameOverListener != null) {
                    gameOverListener.onGameOver(0, true);
                }
            }
            //update progress bar:
            float prog = (float)timeLeft / (float)levelTime;
            progress.setProgress(prog);

            // update all entities
            update(deltaTime);

            // draw entities and debug
            draw(deltaTime);

            if (frameTime > 0) {
                fps = 1000 / frameTime;
            }
        }
    }

    /**
     * Updates the game logic on the entities
     * @param dt delta time from the last frame
     */
    public void update(double dt) {

        for (int i = 0; i< entities.size(); i++)
        {
            entities.get(i).update(dt);
        }


        ArrayList<Destructable> undamagedDestructables = new ArrayList<>();

        int count = 0;
        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i) instanceof Destructable)
            {
                Destructable dest = (Destructable)entities.get(i);
                if( dest != null && dest.health > 0)
                {
                    count++;

                    if(dest.isDamaged == false)
                        undamagedDestructables.add(dest);
                }
            }
        }

        // all destructables ar
        if(count == 0)
        {
            // game over
            Log.e("Sinking", "GAME OVER");
            allDestroyed = true;
            pauseGame();
            if (gameOverListener != null) {
                gameOverListener.onGameOver(timeLeft, false);
            }
        }

        lastObjectDamagedTime += dt;

        if(lastObjectDamagedTime > delayBetweenDamaging)
        {
            lastObjectDamagedTime = 0;

            if(undamagedDestructables.size() != 0)
            {
                Random r = new Random();
                int selection = r.nextInt(undamagedDestructables.size());
                undamagedDestructables.get(selection).isDamaged = true;
            }

        }
    }

    /**
     *  Renders all entities to the canvas
     * @param dt delta time from the last frame
     */
    public void draw(double dt) {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            //clear color
            canvas.drawColor(Color.argb(255, 17, 52, 229));

            Matrix view = camera.getViewMatrix();

            //sort entities by depth before rendering
            Collections.sort(entities);

            // iterate over entities and draw them
            for (int i = 0; i< entities.size(); i++)
            {
                entities.get(i).draw(canvas, view);
            }

            // draw ui elements
            for(int i = 0; i < uiElements.size(); i++)
            {
                uiElements.get(i).draw(canvas);
            }
            /*
            // Debug info
            int fontSize = 40;
            paint.setTextSize(fontSize);
            canvas.drawText("FrameTime: " + dt * 1000 + "ms", 20, 40, paint);
            canvas.drawText("FPS: " + fps, 20, 45+fontSize, paint);
            canvas.drawText("Time left: " + formatTime(timeLeft), 20, 70+fontSize, paint);


            // debug draw lanes
            Paint lanePaint = new Paint();
            lanePaint.setColor(Color.RED);
            lanePaint.setStrokeWidth(5);
            for(Map.Entry<String, Lane> entry : lanes.entrySet()){
                Lane lane = entry.getValue();
                Matrix matrix = new Matrix();
                matrix.setTranslate(0, 0);
                matrix.setConcat(matrix, view);

                float[] points = new float[]{lane.start, lane.height, lane.end, lane.height};
                matrix.mapPoints(points);
                canvas.drawLine(points[0], points[1], points[2], points[3], lanePaint);
            }*/

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * set a listener for when the game is over.
     *
     * @param listener a listener that gets called with time and win values.
     */
    public void setGameOverListener(OnGameOverListener listener) {
        this.gameOverListener = listener;
    }

    /**
     * Call to pause and resume the game.<br />
     * Used by the pause button and the help dialog.
     */

    public void pauseGame() {
        isPaused = !isPaused;
    }

    /**
     * Called by the parent activity, when activity is left
     */
    public void pause() {
        running = false;
        stopSound();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error", "Joining game thread");
        }
    }

    /**
     * Called by the parent activity to resume the thread.
     */
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.setName("GameThread");
        gameThread.start();
    }

    /**
     * Sets the player speed to -1 to walk to the left
     *
     * Gets called by GameEngineActivity's buttons
     */
    public void goLeft() {
        player.SetMovementInput(-1);
    }

    /**
     * Sets the player speed to 1 to walk to the right
     *
     * Gets called by GameEngineActivity's buttons
     */
    public void goRight() {
        player.SetMovementInput(1);
    }

    /**
     * Sets the player speed to 0 to stop moving
     *
     * Gets called by GameEngineActivity's buttons
     */
    public void stop() {
        player.SetMovementInput(0);
    }

    /**
     * Makes the player character take an action, depending on position. <br />
     * Actions include climbing ladder or using item.
     */
    public void takeAction() {
        player.onInteract();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        for(int i = 0; i < uiElements.size(); i++)
        {
            if(uiElements.get(i).setInput(event) == InputHandle.Handled){
                return true;
            }
        }

        return true;
    }

    private void playSound() {
        mp = MediaPlayer.create(this.getContext(), R.raw.sinking);
        mp.setLooping(true);
        mp.start();
    }

    private void stopSound() {
        mp.release();
    }

    public void pausePlaySound() {
        if (isMusicPlaying) {
            mp.pause();
        } else {
            mp.start();
        }
        isMusicPlaying = !isMusicPlaying;
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(20);

            this.soundPool = builder.build();
        }

        //When soundPool is loaded
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
                playSoundtrack();
            }
        });

        soundIdBackground = soundPool.load(this.getContext(), R.raw.sinking, 1);
    }

    public void playSoundtrack() {
        if (soundPoolLoaded) {
            soundPool.play(soundIdBackground, 0.8f, 0.8f, 1, -1, 1f);
        }
    }

    /////////////////////////////////////////////////////////

    /**
     * spawns a new entity into the game world
     * @param entityClass entity class to spawn
     */
    public Entity spawnEntity(Class<? extends Entity> entityClass, int x, int y, JSONObject jsonObject)
    {
        try {
            // Ctor signature
            Class[] cArgs = new Class[3];
            cArgs[0] = this.getClass();
            cArgs[1] = int.class;
            cArgs[2] = int.class;

            // find ctor and construct new object of class T
            Entity entity = entityClass.getDeclaredConstructor(cArgs).newInstance(this, x, y);

            if(jsonObject != null)
            {
                try{
                    entity.loadFromJson(jsonObject);
                }catch (JSONException ex)
                {
                    Log.e("Sinking", "Failed to parse Json");
                    Log.e("Sinking", ex.toString());
                    ex.printStackTrace();
                }
            }

            // add new entity to entity array
            entities.add(entity);

            return entity;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /** Creates the game UI */
    private void createGameUI()
    {
        uiElements = new ArrayList<>();

        /*
        // right move button
        UIElement rightMoveButton = new UIButton();
        rightMoveButton.x = 350;
        rightMoveButton.y = 800;
        rightMoveButton.height = rightMoveButton.width = 100;
        ((UIButton) rightMoveButton).addOnClickEvent(new ButtonEvent() {
            @Override
            public void execute() {
                player.moveSpeed(1);
            }
        });
        ((UIButton) rightMoveButton).addOnReleasedEvent(new ButtonEvent() {
            @Override
            public void execute() {
                player.moveSpeed(0);
            }
        });

        // left move button
        UIElement leftMoveButton = new UIButton();
        leftMoveButton.x = 200;
        leftMoveButton.y = 800;
        leftMoveButton.height = leftMoveButton.width = 100;
        ((UIButton) leftMoveButton).addOnClickEvent(new ButtonEvent() {
            @Override
            public void execute() {
                player.moveSpeed(-1);
            }
        });
        ((UIButton) leftMoveButton).addOnReleasedEvent(new ButtonEvent() {
            @Override
            public void execute() {
                player.moveSpeed(0);
            }
        });

        // action button
        UIElement actionButton = new UIButton();
        actionButton.x = 1500;
        actionButton.y = 800;
        actionButton.height = actionButton.width = 100;

        uiElements.add(rightMoveButton);
        uiElements.add(leftMoveButton);
        uiElements.add(actionButton);
        */

        // time bar
        UIProgress timeBar = new UIProgress();
        timeBar.x = 1000;
        timeBar.y = 100;
        timeBar.width = 200;
        timeBar.height = 40;
        timeBar.setColor(Color.RED);
        timeBar.setSecondaryColor(Color.GREEN);
        //timeBar.setProgress(1.f);

        //store in class var to change percentage during game
        progress = timeBar;
        uiElements.add(timeBar);
    }

    public interface OnGameOverListener {
        public void onGameOver(long time, boolean won);
    }
}
