package gamedb.abelsantos.com.gamedb.IGDB;

import android.util.Log;
import android.widget.ImageView;

import com.fasterxml.jackson.annotation.JsonSetter;


/**
 * Created by Abel Cruz dos Santos on 22.05.2017.
 */

public class IgdbGame{

    private int id;
    private String name;
    private String slug;
    private String url;
    private String thumbnailUrl;
    private long releaseDate;
    private int created_at;
    private int updated_at;
    private double rating;
    private double aggregated_rating;
    private double popularity;
    private int[] developers;
    private int[] publishers;
    private int[] category;
    private int[] genre;
    private IgdbGameCover mIgdbGameCover;
    private IgdbReleaseDates[] mIgdbReleaseDates;
    //Both publisher and developer are returned as companies
    private IgdbCompany[] mIgdbCompanies;

    public IgdbGameCover getIgdbGameCover() {
        return mIgdbGameCover;
    }
    @JsonSetter("cover")
    public void setIgdbGameCover(IgdbGameCover igdbGameCover) {
        mIgdbGameCover = igdbGameCover;
    }

    public int getId() {
        return id;
    }
    @JsonSetter("id")
    public void setId(int id) {
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
    @JsonSetter("rating")
    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPopularity() {
        return popularity;
    }
    @JsonSetter("popularity")
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int[] getCategory() {
        return category;
    }
    @JsonSetter("category")
    public void setCategory(int[] category) {
        this.category = category;
    }

    public long getReleaseDate() {
        return releaseDate;
    }
    @JsonSetter("first_release_date")
    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getAggregated_rating() {
        return aggregated_rating;
    }
    @JsonSetter("aggregated_rating")
    public void setAggregated_rating(double aggregated_rating) {
        this.aggregated_rating = aggregated_rating;
    }

    public IgdbReleaseDates[] getIgdbReleaseDates() {
        return mIgdbReleaseDates;
    }
    @JsonSetter("release_dates")
    public void setIgdbReleaseDates(IgdbReleaseDates[] igdbReleaseDates) {
        mIgdbReleaseDates = igdbReleaseDates;
    }

    public IgdbCompany[] getIgdbCompanies() {
        return mIgdbCompanies;
    }

    public void setIgdbCompanies(IgdbCompany[] igdbCompanies) {
        mIgdbCompanies = igdbCompanies;
    }

    public int[] getDevelopers() {
        return developers;
    }
    @JsonSetter("developers")
    public void setDevelopers(int[] developers) {
        this.developers = developers;
    }

    public int[] getPublishers() {
        return publishers;
    }
    @JsonSetter("publishers")
    public void setPublishers(int[] publishers) {
        this.publishers = publishers;
    }

    public String resolveFirstReleaseYear(){
        String dateHuman = "";
        //Gets the earliest release date
        if (mIgdbReleaseDates != null){
            int year = mIgdbReleaseDates[0].getYear();
            for(int i = 0; i < mIgdbReleaseDates.length; i++){
                if (mIgdbReleaseDates[i].getYear() < year){
                    year = mIgdbReleaseDates[i].getYear();
                    dateHuman = mIgdbReleaseDates[i].getDateHuman();
                }
            }
            Log.d("Date", dateHuman);
            return year + "";
        }else{
            Log.d("NoDate", dateHuman);
            return "N/A";
        }
    }



    public int[] getGenre() {
        return genre;
    }
    @JsonSetter("genres")
    public void setGenre(int[] genre) {
        this.genre = genre;
    }
}
