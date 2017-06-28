package gamedb.abelsantos.com.gamedb.IGDB;

/**
 * Created by Abel Cruz dos Santos on 28.06.2017.
 */
import com.fasterxml.jackson.annotation.JsonSetter;

public class IgdbCompany {

    private int mID;
    private String mName;
    private String mSlug;
    private String mUrl;

    public IgdbCompany() {
    }


    public int getID() {
        return mID;
    }
    @JsonSetter("id")
    public void setID(int ID) {
        mID = ID;
    }

    public String getName() {
        return mName;
    }
    @JsonSetter("name")
    public void setName(String name) {
        mName = name;
    }

    public String getSlug() {
        return mSlug;
    }
    @JsonSetter("slug")
    public void setSlug(String slug) {
        mSlug = slug;
    }

    public String getUrl() {
        return mUrl;
    }
    @JsonSetter("url")
    public void setUrl(String url) {
        mUrl = url;
    }
}
