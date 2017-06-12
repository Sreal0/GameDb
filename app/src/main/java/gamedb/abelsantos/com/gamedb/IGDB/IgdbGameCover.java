package gamedb.abelsantos.com.gamedb.IGDB;

/**
 * Created by Abel Cruz dos Santos on 29.05.2017.
 */
import com.fasterxml.jackson.annotation.JsonSetter;

import io.realm.RealmObject;

public class IgdbGameCover{
    private String url;
    private String cloudinaryId;
    private int width;
    private int height;

    public IgdbGameCover(String url, String cloudinaryId, int width, int height) {
        this.url = url;
        this.cloudinaryId = cloudinaryId;
        this.width = width;
        this.height = height;
    }

    public IgdbGameCover(){

    }

    public String getUrl() {
        if (url != null){
            return url;
        }else{
            return "";
        }
    }
    @JsonSetter("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public String getCloudinaryId() {

        return cloudinaryId;
    }
    @JsonSetter("cloudinary_id")
    public void setCloudinaryId(String cloudinaryId) {
        this.cloudinaryId = cloudinaryId;
    }

    public int getWidth() {
        return width;
    }
    @JsonSetter("width")
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    @JsonSetter("height")
    public void setHeight(int height) {
        this.height = height;
    }
}
