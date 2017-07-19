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

    public String resolveWebsiteCategoryToName(int category){
        String name;
        switch (category){
            case 1:
                name = "Official";
                break;
            case 2:
                name = "Wikia";
                break;
            case 3:
                name = "Wikipedia";
                break;
            case 4:
                name = "Facebook";
                break;
            case 5:
                name = "Twitter";
                break;
            case 6:
                name = "Twitch";
                break;
            case 8:
                name = "Instagram";
                break;
            case 9:
                name = "Youtube";
                break;
            case 10:
                name = "Iphone";
                break;
            case 11:
                name = "Ipad";
                break;
            case 12:
                name = "Android";
                break;
            case 13:
                name = "Steam";
                break;
            default:
                name = "No Websites";
                break;
        }
        return name;
    }


}
