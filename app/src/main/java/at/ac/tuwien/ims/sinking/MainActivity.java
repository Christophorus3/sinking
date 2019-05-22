package at.ac.tuwien.ims.sinking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.ac.tuwien.ims.sinking.GameEngine.GameEngineActivity;

/**
 * A test main screen. Starts the GameEngineActivity.
 *
 * @author Christoph Wottawa
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGameClicked(View view) {
        Intent gameIntent = new Intent(this, GameEngineActivity.class);
        startActivity(gameIntent);
    }
}
