package gamedb.abelsantos.com.gamedb.IGDB;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public List<IgdbGame> getGames(){
        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(getBaseContext());
        final List<IgdbGame> igdbGames = new ArrayList<>();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getGamesURL +  API_KEY + gameAttributes + sortGamesByreleaseDateDESC , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ObjectMapper mapper = new ObjectMapper();

                try{
                    IgdbGame igdbGame = mapper.readValue(response, IgdbGame.class);
                    igdbGames.add(igdbGame);
                }catch (IOException ex){
                    Log.d(TAG, ex.toString());
                }
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error");
            }
        });

        mRequestQueue.add(stringRequest);
        return igdbGames;
    }

}
