package at.ac.tuwien.ims.sinking.GameEngine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import at.ac.tuwien.ims.sinking.R;

public class LevelBackground extends Entity {

    Bitmap bitmap;

    public LevelBackground(GameView context, int x, int y)
    {
        super(context, x, y);
        x = y = 0; // dont move the level
        depth = 0;
    }

    @Override
    public void draw(Canvas canvas, Matrix view) {

        if(bitmap == null)
            return;

        Matrix matrix = new Matrix();
        matrix.setTranslate(x, y);
        matrix.setConcat(matrix, view);

        canvas.drawBitmap(bitmap, matrix, new Paint());
    }

    public void setLevelBitmap(String levelname)
    {
        AssetManager assetManager = context.getContext().getAssets();
        InputStream is;
        try{
            is = assetManager.open(levelname + ".png");
            bitmap = BitmapFactory.decodeStream(is);
        }
        catch (IOException ex)
        {}
    }

}
