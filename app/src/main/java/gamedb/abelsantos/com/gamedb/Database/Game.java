package gamedb.abelsantos.com.gamedb.Database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Abel Cruz dos Santos on 09.05.2017.
 */

public class Game extends RealmObject {

    private static final int TAG_DATABASE = 1;
    private static final int TAG_WISHLIST = 2;

    @PrimaryKey
    private long id;

    private String  mGameName;
    private String  mSlug;
    private String  mCloudinaryId;
    private long    mCreatedAt;
    private long    mUpdatedAt;
    private String  mSummary;
    private String  mStoryline;
    private double  mRating;
    private double  mAggregated_rating;
    private int     mRatingCount;
    //private IgdbGameCover mIgdbGameCover;
    //private IgdbGameGenre mIgdbGameGenre;
    private int     mDatabaseOrWishlist;

    //Default empty Constructor must be declared
    public Game(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String gameName) {
        mGameName = gameName;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

    public String getCloudinaryId() {
        return mCloudinaryId;
    }

    public void setCloudinaryId(String cloudinaryId) {
        mCloudinaryId = cloudinaryId;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public long getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getStoryline() {
        return mStoryline;
    }

    public void setStoryline(String storyline) {
        mStoryline = storyline;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public double getAggregated_rating() {
        return mAggregated_rating;
    }

    public void setAggregated_rating(double aggregated_rating) {
        mAggregated_rating = aggregated_rating;
    }

    public int getRatingCount() {
        return mRatingCount;
    }

    public void setRatingCount(int ratingCount) {
        mRatingCount = ratingCount;
    }

    /*public IgdbGameCover getIgdbGameCover() {
        return mIgdbGameCover;
    }

    public void setIgdbGameCover(IgdbGameCover igdbGameCover) {
        mIgdbGameCover = igdbGameCover;
    }

    public IgdbGameGenre getIgdbGameGenre() {
        return mIgdbGameGenre;
    }

    public void setIgdbGameGenre(IgdbGameGenre igdbGameGenre) {
        mIgdbGameGenre = igdbGameGenre;
    }*/

    public int getDatabaseOrWishlist() {
        return mDatabaseOrWishlist;
    }

    public void setDatabaseOrWishlist(int databaseOrWishlist) {
        mDatabaseOrWishlist = databaseOrWishlist;
    }
}
