package at.ac.tuwien.ims.sinking.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * This is a Room Persistence Database.<br/>
 * It is used to initialize the persistence database and keeps a singleton instance around.
 *
 * @author Christoph Wottawa
 */

@Database(entities = {HighScore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HighScoreDao highScoreDao();

    private static AppDatabase sInstance;

    /**
     * Returns a singleton instance of the Room database.
     *
     * @param context Android application context
     * @return Room Database instance
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "sinking-db").build();
        }

        return sInstance;
    }
}
