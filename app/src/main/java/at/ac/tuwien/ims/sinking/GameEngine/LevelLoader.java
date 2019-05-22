package at.ac.tuwien.ims.sinking.GameEngine;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * deserializes a level file
 *
 * @author Matthias Huerbe
 */
public class LevelLoader {

    // ref to the gameview
    private GameView gameView;
    private String levelName;

    public LevelLoader(GameView gameView, String LevelName)
    {
        this.gameView = gameView;
        this.levelName = LevelName;
    }

    // triggers the loading of the level
    public boolean loadLevel()
    {
        if(gameView == null || levelName == "")
            return false;

        LevelBackground levelBackground = (LevelBackground)gameView.spawnEntity(LevelBackground.class, 0,0, null );
        levelBackground.setLevelBitmap(levelName);

        String jsonString = loadJsonString();

        try {
            JSONObject levelJSON = new JSONObject(jsonString);

            LoadEntities(levelJSON);
            LoadLanes(levelJSON);
            LoadPlayerStart(levelJSON);

        }
        catch (JSONException ex)
        {
            Log.e("Sinking", ex.getMessage());
            ex.printStackTrace();
        }

        return true;
    }

    /**
     * loads the level string from a file in the assets folder
     * @return string of the json
     */
    private String loadJsonString()
    {
        String json = null;
        try{
            AssetManager assetManager = gameView.getContext().getAssets();
            InputStream input = assetManager.open(levelName + ".json");
            int size = input.available();

            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            json = new String(buffer);
        }
        catch ( Exception ex)
        {
            ex.printStackTrace();
        }
        return json;
    }

    private void LoadEntities(JSONObject levelJSON) throws JSONException
    {
        JSONArray jsonarray = levelJSON.getJSONArray("entities");

        for(int i = 0; i < jsonarray.length(); i++)
        {
            JSONObject obj = jsonarray.getJSONObject(i);
            String classname = obj.getString("class");

            try{
                Class c = Class.forName("at.ac.tuwien.ims.sinking.GameEngine." + classname);
                gameView.spawnEntity(c, 0, 0,  obj);
                //spawnEntity(BouncingBall.class, 500, 200);
            } catch (Exception ex)
            {
                Log.e("Sinking", ex.toString());
            }
        }
    }

    private void LoadLanes(JSONObject levelJSON) throws JSONException{
        JSONArray jsonArray = levelJSON.getJSONArray("lanes");

        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject obj = jsonArray.getJSONObject(i);

            String name = obj.getString("name");
            int height = obj.getInt("height");
            int start = obj.getInt("start");
            int end = obj.getInt("end");

            Lane lane = new Lane(name, height, start, end);
            gameView.lanes.put(name, lane);
        }
    }

    private void LoadPlayerStart(JSONObject levelJSON) throws JSONException{
        JSONObject obj = levelJSON.getJSONObject("playerspawn");

        String lane = obj.getString("lane");
        int pos = obj.getInt("pos");

        gameView.playerStart = new PlayerStart(lane, pos);
    }
}
