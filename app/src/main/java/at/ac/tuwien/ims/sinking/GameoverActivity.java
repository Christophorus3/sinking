package at.ac.tuwien.ims.sinking;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import at.ac.tuwien.ims.sinking.Persistence.AppDatabase;
import at.ac.tuwien.ims.sinking.Persistence.HighScore;

import static at.ac.tuwien.ims.sinking.Util.formatTime;

/**
 *  The Game Over Screen - animated.
 *
 * @author Christoph Wottawa
 */
public class GameoverActivity extends AppCompatActivity {

    private long time;
    private String name;

    TextView resultTimeView;
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        resultTimeView = findViewById(R.id.resultTime);
        nameText = findViewById(R.id.nameText);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        time = (long)bundle.get("time");
        //time = intent.getIntExtra("time", 0);

        resultTimeView.setText(formatTime(time));

        Animation viewAnimation = AnimationUtils.loadAnimation(this, R.anim.loseanimation);
        TextView text = (TextView) findViewById(R.id.lostTitleText);
        text.startAnimation(viewAnimation);
    }

    /**
     * Saves the users score to the database
     * @param view
     */
    public void onOkayClicked(View view) {
        final AppDatabase appDatabase = AppDatabase.getInstance(this);
        final HighScore newScore = new HighScore();
        newScore.setPlayerName(nameText.getText().toString());
        newScore.setScore((int)time);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.highScoreDao().insertAll(newScore);
                return null;
            }
        }.execute();

        finish();
    }
}
