package gamedb.abelsantos.com.gamedb.IGDB;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abel Cruz dos Santos on 18.07.2017.
 */

public class IgdbWebsite {
    private int mCategory;
    private String mUrl;

    public IgdbWebsite() {
    }

    public int getCategory() {
        return mCategory;
    }
    @JsonSetter("category")
    public void setCategory(int category) {
        mCategory = category;
    }

    public String getUrl() {
        return mUrl;
    }
    @JsonSetter("url")
    public void setUrl(String url) {
        mUrl = url;
    }

    
}
