package gamedb.abelsantos.com.gamedb.Database;

import java.util.List;

import io.realm.RealmList;
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
    private int     mDatabaseOrWishlist;
    private RealmList<RealmString> mResolvedDevsPubs;
    private String mPlatforms;
    private String mReleasedate;
    private String mGenre;


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

    public int getDatabaseOrWishlist() {
        return mDatabaseOrWishlist;
    }

    public void setDatabaseOrWishlist(int databaseOrWishlist) {
        mDatabaseOrWishlist = databaseOrWishlist;
    }

    public RealmList<RealmString> getResolvedDevsPubs() {
        return mResolvedDevsPubs;
    }

    public void setResolvedDevsPubs(RealmList<RealmString> resolvedDevsPubs) {
        mResolvedDevsPubs = resolvedDevsPubs;
    }

    public String getPlatforms() {
        return mPlatforms;
    }

    public void setPlatforms(String platforms) {
        mPlatforms = platforms;
    }

    public String getReleasedate() {
        return mReleasedate;
    }

    public void setReleasedate(String releasedate) {
        mReleasedate = releasedate;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }
}
