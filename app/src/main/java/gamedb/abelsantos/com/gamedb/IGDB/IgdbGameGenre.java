package gamedb.abelsantos.com.gamedb.IGDB;

/**
 * Created by Abel Cruz dos Santos on 29.05.2017.
 */
import com.fasterxml.jackson.annotation.JsonSetter;

public class IgdbGameGenre {

    private long id;
    private String name;
    private String slug;
    private String url;
    private long createdAt;
    private long updatedAt;
    private int[] gamesIDs;

    public IgdbGameGenre(long id, String name, String slug, String url, long createdAt, long updatedAt, int[] gamesIDs) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.gamesIDs = gamesIDs;
    }

    public IgdbGameGenre(){

    }

    public long getId() {
        return id;
    }
    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }
    @JsonSetter("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }
    @JsonSetter("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatedAt() {
        return createdAt;
    }
    @JsonSetter("created_at")
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
    @JsonSetter("updated_at")
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int[] getGamesIDs() {
        return gamesIDs;
    }
    @JsonSetter("games")
    public void setGamesIDs(int[] gamesIDs) {
        this.gamesIDs = gamesIDs;
    }
}
