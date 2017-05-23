package gamedb.abelsantos.com.gamedb.IGDB;

import android.app.Application;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Abel Cruz dos Santos on 22.05.2017.
 */

public class IgdbClient extends Application{

    private static IgdbClient sIgdbClient;
    private static final String tag_json_arry = "json_array_req";

    private static final String TAG = "IgdbClient";
    private static final String API_KEY = "?mashape-key=spjH1mZDLmmsh2xi8l8E4sz5dRFBp1FexQhjsnEsNlSCIqVzS0";
    private static final String getGamesURL = "https://igdbcom-internet-game-database-v1.p.mashape.com/games/";
    private static final String gameAttributes = "&fields=name%2Cpopularity%2Cfirst_release_date";
    private static final String sortGamesByreleaseDateDESC = "&limit=10&offset=0&order=release_dates.date%3Adesc";

    private RequestQueue mRequestQueue;
    private JsonArrayRequest mJsonArrayRequest;

    @Override
    public void onCreate(){
        super.onCreate();
        //Db initialisation
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        sIgdbClient = this;
    }

    public static synchronized IgdbClient getInstance(){
        if(sIgdbClient == null){
            sIgdbClient = new IgdbClient();
            Log.d(TAG, "New Instance");
        }
        return sIgdbClient;
    }

    public List<IgdbGame>  getGames(){
        final List<IgdbGame> igdbGames = new ArrayList<>();

        // Request a string response from the provided URL.
        final JsonArrayRequest req = new JsonArrayRequest(
                getGamesURL +  API_KEY + gameAttributes + sortGamesByreleaseDateDESC , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String data = object.toString();
                        IgdbGame igdbGame = mapper.readValue(data, IgdbGame.class);
                        Log.d(TAG, igdbGame.getName());
                        igdbGames.add(igdbGame);
                    } catch (JSONException e) {
                        Log.d(TAG, "JSONException: " + e);
                    } catch (JsonParseException e) {
                        Log.d(TAG, "JsonParseException: " + e);
                    } catch (JsonMappingException e) {
                        Log.d(TAG, "JsonMappingException: " + e);
                    } catch (IOException e) {
                        Log.d(TAG, "IOException: " + e);
                    }
                }
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        addToRequestQueue(req, tag_json_arry);
        Log.d(TAG, "Size of list " + igdbGames.size());
        return igdbGames;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
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

}
