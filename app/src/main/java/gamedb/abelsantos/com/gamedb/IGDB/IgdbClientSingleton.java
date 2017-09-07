package gamedb.abelsantos.com.gamedb.IGDB;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

/**
 * Created by Abel Cruz dos Santos on 22.05.2017.
 */

public class IgdbClientSingleton extends Application{

    private static IgdbClientSingleton sIgdbClientSingleton;
    private static final String tag_json_arry = "json_array_req";

    private static int offset = 0;
    private static final String TAG = "IgdbClientSingleton";
    private static final String API_KEY = "?mashape-key=spjH1mZDLmmsh2xi8l8E4sz5dRFBp1FexQhjsnEsNlSCIqVzS0";
    private static final String GET_GAMES_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/";
    private static final String GET_COMPANY_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/companies/";
    private static final String ORDER_RELEASE_DATES_DATE_DESC = "&order=first_release_date:desc";
    private static final String ORDER_AGGREGATED_RATING_DESC = "&order=aggregated_rating:desc";
    private static final String ORDER_BY_POPULARITY = "&order=popularity:desc";
    private static final String FILTER_NEWEST_RELEASES = "&filter[first_release_date][lte]=";
    private static final String FILTER_FIRST_RELEASE_DATE_GREATER_THAN_LAST_X_MONTHS = "&filter[first_release_date][gte]=";
    private static final String FILTER_FIRST_RELEASE_DATE_LOWER_THAN_TODAY = "&filter[first_release_date][lte]=";
    private static final String GET_GAMES_KEY_ATTRIBUTES_LIMIT = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/" +
                                                            "?mashape-key=spjH1mZDLmmsh2xi8l8E4sz5dRFBp1FexQhjsnEsNlSCIqVzS0" +
                                                            "&fields=name,cover,genres,release_dates,aggregated_rating,first_release_date,game_modes" +
                                                            "&limit=20&offset=%d";
    private static final String URL_SCREENSHOT_BIG = "https://images.igdb.com/igdb/image/upload/t_screenshot_big/";
    private static final String URL_COVER_BIG_2X = "https://images.igdb.com/igdb/image/upload/t_cover_big_2x/";
    private static final String URL_COVER_BIG = "https://images.igdb.com/igdb/image/upload/t_cover_big/";
    private static final String IMAGE_FORMAT_PNG = ".png";
    private static final String IMAGE_FORMAT_JPG = ".jpg";
    private static final String SINGLE_GAME_DETAILS = "&fields=name,cover,genres,release_dates,aggregated_rating,first_release_date" +
            ",developers,publishers,summary,game_modes,websites,time_to_beat";
    private static final String COMPANY_FIELDS = "&fields=name,slug,url";


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private String mCurrentRequestHeadAndOffset;
    private String mCurrentRequestParameters;
    private String mCurrentRequestFull;
    private int mCurrentOffset = 0;

    @Override
    public void onCreate(){
        super.onCreate();
        sIgdbClientSingleton = this;
    }

    public static synchronized IgdbClientSingleton getInstance(){
        if(sIgdbClientSingleton == null){
            sIgdbClientSingleton = new IgdbClientSingleton();
        }
        return sIgdbClientSingleton;
    }

    //For GameDetailsFragment
    public String getSingleGameDetails(int id){
        return GET_GAMES_URL + id + API_KEY + SINGLE_GAME_DETAILS;
    }

    //To resolve company names based on their ids
    public String getCompanyNamesURL(int dev, int pub){
        return GET_COMPANY_URL + dev + "," + pub + API_KEY + COMPANY_FIELDS;
    }

    //-->WORKS!!! <---
    //This Method will return a list of games ordered by popularity from the past 6 months. Offset is 20
    public String getGamesOrderedByPopularityURL(){
        return  ORDER_BY_POPULARITY;
    }


    //--> WORKS!!! <--
    //This Method will return a list of game ordered by release date. Offset is 20
    public String getGamesOrderedByNewestReleasesURL(){
        long timestamp = System.currentTimeMillis();

        return FILTER_NEWEST_RELEASES
                + timestamp + ORDER_RELEASE_DATES_DATE_DESC ;
    }

    //--> WORKS!!! <---
    //Get highest rating last 6 months
    public String getGamesHighestRatingLastXMonths(int months){
        long timestamp = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, - 6);
        long sixMonthsAgo = calendar.getTimeInMillis();
        //This will return the highest rated games of all times.
        if (months == 0){
            return FILTER_FIRST_RELEASE_DATE_LOWER_THAN_TODAY +
                    timestamp +
                    ORDER_AGGREGATED_RATING_DESC;
        }
        return FILTER_FIRST_RELEASE_DATE_GREATER_THAN_LAST_X_MONTHS +
                sixMonthsAgo +
                FILTER_FIRST_RELEASE_DATE_LOWER_THAN_TODAY +
                timestamp +
                ORDER_AGGREGATED_RATING_DESC;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if(mImageLoader == null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static int getOffset() {
        return offset;
    }

    public static void setOffset(int offset) {
        IgdbClientSingleton.offset = offset;
    }

    public static void nextPage(){
        offset += 20;
    }

    public static String getUrlScreenshotBig() {
        return URL_SCREENSHOT_BIG;
    }

    public String getUrlCoverBig2x() {
        return URL_COVER_BIG_2X;
    }

    public String getImageFormatJpg() {
        return IMAGE_FORMAT_JPG;
    }

    public String getUrlCoverBig(){
        return URL_COVER_BIG;
    }

    public String getImageFormatPng(){
        return IMAGE_FORMAT_PNG;
    }

    public String getCurrentRequestParameters() {
        return mCurrentRequestParameters;
    }

    public void setCurrentRequestParameters(String currentRequestParameters) {
        //The first part of the request is always the same. What I need is the parameters of the request
        mCurrentRequestParameters =  currentRequestParameters;
    }

    public void updateOffset(){
        //Updates offset
        String url = String.format(GET_GAMES_KEY_ATTRIBUTES_LIMIT, mCurrentOffset);
        mCurrentRequestHeadAndOffset =  url;
    }

    public int getCurrentOffset() {
        return mCurrentOffset;
    }

    public void setCurrentOffset(int currentOffset) {
        mCurrentOffset = currentOffset;
    }

    public void buildFullRequest(){
        mCurrentRequestFull = mCurrentRequestHeadAndOffset + mCurrentRequestParameters;
    }

    public String getCurrentRequestFull() {
        return mCurrentRequestFull;
    }
}
