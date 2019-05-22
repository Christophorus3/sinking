package at.ac.tuwien.ims.sinking.GameEngine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import at.ac.tuwien.ims.sinking.R;

public class SpriteAnimation {

    private GameView gameView;
    public Bitmap bitmap;
    public int frameWidth = 64;
    public int frameHeight = 111;
    public int frameCount = 5;

    private int ResId = -1;

    public SpriteAnimation(GameView inGameView, int id, int inframeWidth, int inframeHeight, int inframeCount){
        ResId = id;
        gameView = inGameView;
        frameWidth = inframeWidth;
        frameHeight = inframeHeight;
        frameCount = inframeCount;
    }

    public void loadBitmap()
    {
        if(ResId != -1)
        {
            bitmap = BitmapFactory.decodeResource(gameView.getContext().getResources(), ResId);
            bitmap = Bitmap.createScaledBitmap(bitmap, frameWidth * frameCount, frameHeight, false);
        }
    }

}
