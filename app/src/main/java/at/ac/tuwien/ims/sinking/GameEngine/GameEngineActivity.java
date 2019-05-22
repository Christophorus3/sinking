package at.ac.tuwien.ims.sinking.GameEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import at.ac.tuwien.ims.sinking.GameoverActivity;
import at.ac.tuwien.ims.sinking.R;
import at.ac.tuwien.ims.sinking.WinActivity;

/**
 * This activity loads and shows a {@link GameView} to actually run the game.
 *
 * It is calls pause and resume on the gameView when the activity itself is paused or resumed.
 *
 * @author Christoph Wottawa
 */

public class GameEngineActivity extends AppCompatActivity {

    private GameView gameView;
    private boolean gamePaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_engine);

        FloatingActionButton leftButton = findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameView.goLeft();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gameView.stop();
                }
                v.performClick();
                return false;
            }
        });
        FloatingActionButton rightButton = findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == (MotionEvent.ACTION_DOWN)) {
                    gameView.goRight();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gameView.stop();
                }
                v.performClick();
                return false;
            }
        });

        // get level to load and pass it to the game view
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String levelName = "";
        if (bundle != null)
        {
            levelName = (String)bundle.get("LevelName");
            Log.v("Sinking", levelName);
        }

        //get canvas view to add the GameView
        LinearLayout surface = findViewById(R.id.surface);

        gameView = new GameView(this, levelName);
        gameView.setGameOverListener(new GameView.OnGameOverListener() {
            @Override
            public void onGameOver(long time, boolean won) {
                //if won start win screen
                if (won) {
                    Intent winIntent = new Intent(getApplicationContext(), WinActivity.class);
                    winIntent.putExtra("time", time);
                    startActivity(winIntent);
                } else {
                    Intent loseIntent = new Intent(getApplicationContext(), GameoverActivity.class);
                    loseIntent.putExtra("time", time);
                    startActivity(loseIntent);
                }
            }
        });

        surface.addView(gameView);
    }



    public void onActionPress(View view) {
        gameView.takeAction();
    }

    public void onPausePress(View view) {
        gameView.pauseGame();
    }

    public void onMusicPress(View view) {
        gameView.pausePlaySound();
    }

    public void onHelpPress(View view) {
        gameView.pauseGame();
        //show Help dialog:
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.help_text);
        dialogBuilder.setTitle(R.string.help_title);
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                gameView.pauseGame();
            }
        });
        dialogBuilder.show();

        //AlertDialog helpDialog = dialogBuilder.create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
    }
}
