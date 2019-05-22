package at.ac.tuwien.ims.sinking.Persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * A Room Persistence Entity.<br/>
 *
 * This is used to persist a players 'highscore' to a Room Database.<br/>
 *
 * Stores an ID, Player Name and Score.
 *
 * @author Christoph Wottawa
 */

@Entity(tableName = "highscores")
public class HighScore {

    /**
     * Primary Key
     */
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * Player's Name
     */
    private String playerName;

    /**
     * achieved score
     */
    private Integer score;

    /**
     * Default constructor
     */
    public HighScore() {
    }

    /**
     * Convenience constructor
     */
    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    /**
     * Returns the primary key of this record.
     * @return primary key
     */
    @NonNull
    public int getId() {
        return id;
    }

    /**
     * Sets a primary key for this record.
     * @param id primary key
     */
    public void setId(@NonNull int id) {
        this.id = id;
    }

    /**
     * Returns the content of the playerName field.
     * @return player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the content for the playerName field.
     * @param playerName player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the content of the score field
     * @return player's score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the content of the score field.
     * @param score player's score
     */
    public void setScore(Integer score) {
        this.score = score;
    }
}
