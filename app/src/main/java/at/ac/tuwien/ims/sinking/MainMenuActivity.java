package at.ac.tuwien.ims.sinking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.ac.tuwien.ims.sinking.GameEngine.GameEngineActivity;

/**
 * Main menu activity.
 *
 * @author Matthias H&uuml;rbe
 */
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void level1ButtonClicked(View view) {
        Intent gameIntent = new Intent(this, GameEngineActivity.class);
        gameIntent.putExtra("LevelName", "submarine_1");
        startActivity(gameIntent);
    }

    public void level2ButtonClicked(View view) {
        Intent gameIntent = new Intent(this, GameEngineActivity.class);
        gameIntent.putExtra("LevelName", "submarine_2");
        startActivity(gameIntent);
    }

    public void highscoreButtonClicked(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    public void quitButtonClicked(View view) {
        finish();
    }
}
