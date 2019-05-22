package at.ac.tuwien.ims.sinking.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Interface for a Data Access Object to handle Persistence of the HighScore list.
 *
 * @author Christoph Wottawa
 */

@Dao
public interface HighScoreDao {

    /**
     * Get all entries in the highscore table.
     * @return List of HighScores
     */
    @Query("SELECT * FROM highscores")
    List<HighScore> getAll();

    /**
     * Get all HighScores with matching IDs.
     * @param ids Array of IDs
     * @return List of HighScores
     */
    @Query("SELECT * FROM highscores WHERE id in (:ids)")
    List<HighScore> loadAllByIds(int[] ids);

    /**
     * Store the given HighScore objects.
     * @param highScores (multiple) HighScore object(s)
     */
    @Insert
    void insertAll(HighScore... highScores);

    /**
     * Delete a given HighScore from the database.
     * @param highScore A player's HighScore.
     */
    @Delete
    void delete(HighScore highScore);
}
