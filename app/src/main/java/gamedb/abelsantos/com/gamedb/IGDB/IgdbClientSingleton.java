package gamedb.abelsantos.com.gamedb.IGDB;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
    private static final String ORDER_RELEASE_DATES_DATE_DESC = "&order=first_release_date:desc";
    private static final String ORDER_BY_POPULARITY = "&order=popularity:desc";
    private static final String FILTER_NEWEST_RELEASES = "&filter[first_release_date][lte]=";

    private static final String GET_GAMES_KEY_ATTRIBUTES_LIMIT = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/" +
                                                            "?mashape-key=spjH1mZDLmmsh2xi8l8E4sz5dRFBp1FexQhjsnEsNlSCIqVzS0" +
                                                            "&fields=name,cover,release_dates,aggregated_rating,first_release_date" +
                                                            "&limit=20&offset=";

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate(){
        super.onCreate();

        sIgdbClientSingleton = this;
    }

    public static synchronized IgdbClientSingleton getInstance(){
        if(sIgdbClientSingleton == null){
            sIgdbClientSingleton = new IgdbClientSingleton();
            Log.d(TAG, "New Instance");
        }
        return sIgdbClientSingleton;
    }
    //This Method will return a list of games ordered by popularity from the past 6 months. Offset is 20
    public String getGamesOrderedByPopularityURL(int offset){
        return GET_GAMES_KEY_ATTRIBUTES_LIMIT + offset + ORDER_BY_POPULARITY;
    }

    //This Method will return a list of game ordered by release date. Offset is 20
    public String getGamesOrderedByNewestReleases(int offset){
        long timestamp = System.currentTimeMillis();
        //String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
        return GET_GAMES_KEY_ATTRIBUTES_LIMIT + offset + FILTER_NEWEST_RELEASES
                + timestamp + ORDER_RELEASE_DATES_DATE_DESC ;
    }

    //Get new releases last 6 months
    public String getNewReleasesLast6Months(int offset){
        //Wrong - not returning any values. check the parameters
        long timestamp = System.currentTimeMillis()/1000;
        return GET_GAMES_KEY_ATTRIBUTES_LIMIT + offset +
                + timestamp + ORDER_RELEASE_DATES_DATE_DESC;
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
}
