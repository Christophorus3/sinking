package at.ac.tuwien.ims.sinking;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.ims.sinking.Persistence.AppDatabase;
import at.ac.tuwien.ims.sinking.Persistence.HighScore;

/**
 * This activity shows the achieved highscores.<br/>
 *
 * It uses the apps Room database to read the saved scores.
 *
 * @author Christoph Wottawa
 */

public class HighscoreActivity extends AppCompatActivity {

    private ListView highScoreListView;
    private HighScoreAdapter highScoreAdapter;
    private ArrayList<HighScore> highScoresList;

    //private static final String DATABASE_NAME = "highscore_db";
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highScoresList = new ArrayList<>();
        highScoreAdapter = new HighScoreAdapter(this, highScoresList);

        highScoreListView = findViewById(R.id.scoreListView);
        highScoreListView.setAdapter(highScoreAdapter);

        appDatabase = AppDatabase.getInstance(this.getApplicationContext());

        //uncomment to set example data
        //setData();

        loadData();

        Log.d("HighScoreActivitiy", "onCreate: data lodaded: \n" + highScoresList.toString());
    }

    /**
     * sets some example data
     */
    private void setData() {
        final ArrayList<HighScore> newScores = new ArrayList<>();
        newScores.add(new HighScore("Chris", 20));
        newScores.add(new HighScore("Tom", 40));
        newScores.add(new HighScore("Matt", 60));
        newScores.add(new HighScore("Superman", 200));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.highScoreDao().insertAll(newScores.toArray(new HighScore[0]));
                return null;
            }
        }.execute();

    }

    /**
     * Load data from Room database
     */
    private void loadData() {
        new AsyncTask<Void, Void, List<HighScore>>() {

            @Override
            protected List<HighScore> doInBackground(Void... voids) {
                return appDatabase.highScoreDao().getAll();
            }

            @Override
            protected void onPostExecute(List<HighScore> highScores) {
                highScoresList.clear();
                highScoresList.addAll(highScores);
                highScoreAdapter.notifyDataSetChanged();
                Log.d("HighScoreActivitiy", "onCreate: data lodaded: \n" + highScoresList.toString());
            }
        }.execute();
    }

    // Source: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
    class HighScoreAdapter extends ArrayAdapter<HighScore> {
        public HighScoreAdapter(Context context, List<HighScore> highScores) {
            super(context, 0, highScores);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            HighScore highScore = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_highscore, parent, false);
            }
            // Lookup view for data population
            TextView userNameTxtField = convertView.findViewById(R.id.nameText);
            TextView scoreTxtField = convertView.findViewById(R.id.scoreText);

            // Populate the data into the template view using the data object
            userNameTxtField.setText(highScore.getPlayerName());
            scoreTxtField.setText(Util.formatTime(highScore.getScore()));
            // Return the completed view to render on screen
            return convertView;
        }
    }
}

