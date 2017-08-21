package gamedb.abelsantos.com.gamedb.IGDB;

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Created by Abel Cruz dos Santos on 21.08.2017.
 */

public class IGDBTimeToBeat {
    private long mHastly;
    private long mNormally;
    private long mCompletely;

    public long getHastly() {
        return mHastly;
    }
    @JsonSetter("hastly")
    public void setHastly(long hastly) {
        mHastly = hastly;
    }

    public long getNormally() {
        return mNormally;
    }
    @JsonSetter("normally")
    public void setNormally(long normally) {
        mNormally = normally;
    }

    public long getCompletely() {
        return mCompletely;
    }
    @JsonSetter("completely")
    public void setCompletely(long completely) {
        mCompletely = completely;
    }
}
