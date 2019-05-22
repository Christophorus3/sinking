package at.ac.tuwien.ims.sinking;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

/**
 * The splash screen of the game.<br/>
 * When started plays the intro video.
 *
 * @author Matthias H&uuml;rbe
 */
public class SplashscreenActivity extends AppCompatActivity {
    VideoView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        view = findViewById(R.id.videoView);
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                toMainMenu();
            }
        });

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        view.setVideoURI(uri);
        view.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        view.stopPlayback();
    }

    public void toMainMenu()
    {
        Intent test = new Intent(this, MainMenuActivity.class);
        startActivity(test);
    }

    protected void onVideoClicked(View view) {
        toMainMenu();
    }
}
