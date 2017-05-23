package gamedb.abelsantos.com.gamedb.IGDB;

/**
 * Created by Abel Cruz dos Santos on 22.05.2017.
 */

public class IgdbGame {

    private int id;
    private String name;
    private String slug;
    private String url;
    private int created_at;
    private int updated_at;
    private double rating;
    private double popularity;
    private int[] developers;
    private int[] category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int[] getDevelopers() {
        return developers;
    }

    public void setDevelopers(int[] developers) {
        this.developers = developers;
    }

    public int[] getCategory() {
        return category;
    }

    public void setCategory(int[] category) {
        this.category = category;
    }
}
