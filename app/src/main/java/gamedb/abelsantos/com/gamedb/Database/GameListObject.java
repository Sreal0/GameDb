package gamedb.abelsantos.com.gamedb.Database;

import android.media.Image;

import java.util.Date;

/**
 * Created by Abel Cruz dos Santos on 15.05.2017.
 */

public class GameListObject {

    private String mGame;
    private long mGameId;
    private Date mReleaseDate;
    private double mRating;
    private String mPublisher;
    private Image mThumbnail;

    public GameListObject(String game, long gameId, Date releaseDate, double rating, String publisher, Image thumbnail) {
        mThumbnail = thumbnail;
        mGame = game;
        mGameId = gameId;
        mReleaseDate = releaseDate;
        mRating = rating;
        mPublisher = publisher;
    }

    public GameListObject(){

    }

    public String getGame() {
        return mGame;
    }

    public void setGame(String game) {
        mGame = game;
    }

    public long getGameId() {
        return mGameId;
    }

    public void setGameId(long gameId) {
        mGameId = gameId;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String publisher) {
        mPublisher = publisher;
    }

    public Image getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        mThumbnail = thumbnail;
    }
}
