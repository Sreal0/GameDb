package gamedb.abelsantos.com.gamedb.Database;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Abel Cruz dos Santos on 09.05.2017.
 */

public class Game extends RealmObject {

    @PrimaryKey
    private long mGameId;

    private String mGameName;
    private Date mReleaseDate;
    private int mPlatformId;
    private double mScore;
    private String mUrl;

    public Game(String gameName, Date releaseDate, int platformId, String url) {
        mGameName = gameName;
        mReleaseDate = releaseDate;
        mPlatformId = platformId;
        mUrl = url;
    }

    //Default empty Contructor must be declared
    public Game(){

    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String gameName) {
        mGameName = gameName;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }

    public int getPlatformId() {
        return mPlatformId;
    }

    public void setPlatformId(int platformId) {
        mPlatformId = platformId;
    }

    public double getScore() {
        return mScore;
    }

    public void setScore(double score) {
        mScore = score;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
