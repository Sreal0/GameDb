package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbCompany;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 27.06.2017.
 */

public class GameDetailsFragment extends Fragment {
    public static final String TAG = "GameDetailsFragment";
    private static final String ARGS_ID = "id";
    private int mID ;
    private ImageView mGameCover;
    private TextView mGameTitle;
    private TextView mGameScore;
    private TextView mGameDeveloper;
    private TextView mGamePublisher;
    private TextView mGameGenre;
    private TextView mGameDescription;
    private IgdbClientSingleton sIgdbClientSingleton;
    private IgdbGame mIgdbGame;
    private IgdbCompany mIgdbCompany;
    private boolean gotDev = false;

    public static GameDetailsFragment newInstance(int gameId) {
        GameDetailsFragment fragment = new GameDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, gameId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mID = getArguments().getInt(ARGS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_details, container, false);

        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        mGameDeveloper = (TextView)view.findViewById(R.id.txt_developer);
        mGamePublisher = (TextView)view.findViewById(R.id.txt_game_publisher);
        mGameCover = (ImageView)view.findViewById(R.id.imageView);
        mGameTitle = (TextView)view.findViewById(R.id.txt_game_title);
        mGameScore = (TextView)view.findViewById(R.id.txt_game_score);
        mGameGenre = (TextView)view.findViewById(R.id.txt_game_genre);

        //mGameDescription =(TextView)view.findViewById(R.id.txt_description);
        //gets the request from the singleton...
        String url = sIgdbClientSingleton.getSingleGameDetails(mID);
        //gets the game object from the API. GameDbLauncher will call showGameDetails at the end
        getASingleGameFromAPI(url);
        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText("");
        return view;
    }

    public void showGameDetails(){
        mGameTitle.setText(mIgdbGame.getName());
        int rat = ((int) mIgdbGame.getAggregated_rating());
        if(rat == 0){
            mGameScore.setText("-");
        }else{
            mGameScore.setText(rat + "");
        }
        String protocol = "";
        try{
            protocol = sIgdbClientSingleton.getUrlCoverBig() +
                    mIgdbGame.getIgdbGameCover().getCloudinaryId() + sIgdbClientSingleton.getImageFormatJpg();
        }catch (NullPointerException e){
            Log.d(TAG, e.toString());
        }
        if ( !protocol.equals("")){
            Picasso.with(getContext()).
                    load(protocol).
                    error(R.drawable.ic_error).
                    placeholder(R.drawable.ic_img_placeholder).
                    into(mGameCover);
        }else{
            mGameCover.setImageResource(R.drawable.ic_error);
        }
        mGameGenre.setText(((GameDbLauncher)getActivity()).resolveGenreNames(mIgdbGame.getGenre()));
    }

    public void resolveCompanyNameFromAPI(String url){
        Log.d(TAG, url);
        final JsonArrayRequest req = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String data = object.toString();
                        mIgdbCompany = mapper.readValue(data, IgdbCompany.class);
                        if (!gotDev){
                            mGameDeveloper.setText(mIgdbCompany.getName());
                            gotDev = true;
                        }else {
                            mGamePublisher.setText(mIgdbCompany.getName());
                            showGameDetails();
                        }
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        req.setShouldCache(true);
        sIgdbClientSingleton.addToRequestQueue(req);
    }

    public void getASingleGameFromAPI(String url){
        Log.d("Single", url);

        final JsonArrayRequest req = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                for(int i = 0; i < response.length(); i++){
                    try {
                        Log.d("Single", i + "");
                        JSONObject object = response.getJSONObject(i);
                        String data = object.toString();
                        mIgdbGame = mapper.readValue(data, IgdbGame.class);
                        String url2 = sIgdbClientSingleton.getCompanyNamesURL(mIgdbGame.getDevelopers()[0]);
                        resolveCompanyNameFromAPI(url2);
                        url2 = sIgdbClientSingleton.getCompanyNamesURL(mIgdbGame.getPublishers()[0]);
                        resolveCompanyNameFromAPI(url2);

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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        req.setShouldCache(true);
        sIgdbClientSingleton.addToRequestQueue(req);
    }
}
