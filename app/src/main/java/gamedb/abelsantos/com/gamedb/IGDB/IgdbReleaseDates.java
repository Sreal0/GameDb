package gamedb.abelsantos.com.gamedb.IGDB;

/**
 * Created by Abel Cruz dos Santos on 01.06.2017.
 * This class will resolve the console code based on
 * the release_date Object returned by the API
 * ATTENTION: it returns an array
 */
import com.fasterxml.jackson.annotation.JsonSetter;

public class IgdbReleaseDates {
    private int mGameId;
    private int mCategory;
    private int mPlatform;
    private String mDateHuman;
    private long mUpdatedAt;
    private long mCreatedAt;
    private long mDate;
    private int mRegion;
    private int mYear;
    private int mMonth;

    public IgdbReleaseDates(){

    }

    public IgdbReleaseDates(int gameId, int category, int platform, String dateHuman, long updatedAt, long createdAt, long date, int region, int year, int month) {
        mGameId = gameId;
        mCategory = category;
        mPlatform = platform;
        mDateHuman = dateHuman;
        mUpdatedAt = updatedAt;
        mCreatedAt = createdAt;
        mDate = date;
        mRegion = region;
        mYear = year;
        mMonth = month;
    }

    public int getGameId() {
        return mGameId;
    }
    @JsonSetter("game")
    public void setGameId(int gameId) {
        mGameId = gameId;
    }

    public int getCategory() {
        return mCategory;
    }
    @JsonSetter("category")
    public void setCategory(int category) {
        mCategory = category;
    }

    public int getPlatform() {
        return mPlatform;
    }
    @JsonSetter("platform")
    public void setPlatform(int platform) {
        mPlatform = platform;
    }

    public String getDateHuman() {
        return mDateHuman;
    }
    @JsonSetter("human")
    public void setDateHuman(String dateHuman) {
        mDateHuman = dateHuman;
    }

    public long getUpdatedAt() {
        return mUpdatedAt;
    }
    @JsonSetter("updated_at")
    public void setUpdatedAt(long updatedAt) {
        mUpdatedAt = updatedAt;
    }
    public long getCreatedAt() {
        return mCreatedAt;
    }
    @JsonSetter("created_at")
    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public long getDate() {
        return mDate;
    }
    @JsonSetter("date")
    public void setDate(long date) {
        mDate = date;
    }

    public int getRegion() {
        return mRegion;
    }
    @JsonSetter("region")
    public void setRegion(int region) {
        mRegion = region;
    }

    public int getYear() {
        return mYear;
    }
    @JsonSetter("y")
    public void setYear(int year) {
        mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }
    @JsonSetter("m")
    public void setMonth(int month) {
        mMonth = month;
    }
}
