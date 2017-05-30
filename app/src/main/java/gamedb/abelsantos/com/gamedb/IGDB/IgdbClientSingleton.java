package gamedb.abelsantos.com.gamedb.IGDB;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Abel Cruz dos Santos on 22.05.2017.
 */

public class IgdbClientSingleton extends Application{

    private static IgdbClientSingleton sIgdbClientSingleton;
    private static final String tag_json_arry = "json_array_req";

    private static final String TAG = "IgdbClientSingleton";
    private static final String API_KEY = "?mashape-key=spjH1mZDLmmsh2xi8l8E4sz5dRFBp1FexQhjsnEsNlSCIqVzS0";
    private static final String GET_GAMES_URL = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/";
    private static final String GAME_ATTRIBUTES = "&fields=name%2Ccover%2Crating%2Cfirst_release_date";
    private static final String LIMIT_OFFSET = "&limit=20&offset=";
    private static int offset = 0;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate(){
        super.onCreate();
        //Db initialisation
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        sIgdbClientSingleton = this;
    }

    public static synchronized IgdbClientSingleton getInstance(){
        if(sIgdbClientSingleton == null){
            sIgdbClientSingleton = new IgdbClientSingleton();
            Log.d(TAG, "New Instance");
        }
        return sIgdbClientSingleton;
    }

    public String  getGamesURLGames(){

        return GET_GAMES_URL +  API_KEY + GAME_ATTRIBUTES + LIMIT_OFFSET + offset;
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
        offset++;
    }
}
