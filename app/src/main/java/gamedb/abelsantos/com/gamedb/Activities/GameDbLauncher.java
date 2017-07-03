package gamedb.abelsantos.com.gamedb.Activities;



import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.Fragments.GameDetailsFragment;
import gamedb.abelsantos.com.gamedb.Fragments.GamesFragment;
import gamedb.abelsantos.com.gamedb.Fragments.MyGamesFragment;
import gamedb.abelsantos.com.gamedb.Fragments.SearchFragment;
import gamedb.abelsantos.com.gamedb.Fragments.WishlistFragment;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbCompany;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbReleaseDates;
import gamedb.abelsantos.com.gamedb.R;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class GameDbLauncher extends AppCompatActivity {

    private static String TAG = "gamedb.abelsantos";
    public static final String TAG_GAMES_FRAGMENT = "GamesFragment";
    private static final String tag_json_arry = "json_array_req";
    private Toolbar mToolbar;
    private TextView mToolbarText;
    private BottomNavigationView mBottomNavigationView;
    private String mConsoleFromAssets;
    private String mGenreFromAssets;
    private Realm mRealm;
    private RealmResults<Game> mGameRealmQuery;
    private IgdbClientSingleton sIgdbClientSingleton;
    private IgdbGame igdbGame;
    private IgdbCompany mIgdbCompany;
    private List<String> mCompanies;
    private JSONArray mJSONArraySingleGame;
    private HashMap<String, String > mItemsHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        mRealm = Realm.getDefaultInstance();

        mToolbar = (Toolbar)findViewById(R.id.toolbar_launcher);
        mToolbarText = (TextView)findViewById(R.id.txt_toolbar_undertitle);
        mToolbarText.setGravity(Gravity.BOTTOM);
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Navigation Bar onClickListener
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final FragmentManager supportFragmentManager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.action_mygames:
                        final Fragment myGamesFragment = supportFragmentManager.findFragmentByTag(MyGamesFragment.TAG);
                        if (myGamesFragment == null) {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, MyGamesFragment.newInstance(), MyGamesFragment.TAG)
                                    .addToBackStack(MyGamesFragment.TAG)
                                    .commit();
                        } else {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, myGamesFragment)
                                    .addToBackStack(MyGamesFragment.TAG)
                                    .commit();
                        }
                        break;
                    case R.id.action_games:
                        final Fragment gamesFragment = supportFragmentManager.findFragmentByTag(GamesFragment.TAG);
                        if (gamesFragment == null) {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, GamesFragment.newInstance(), GamesFragment.TAG)
                                    .addToBackStack(GamesFragment.TAG)
                                    .commit();
                        } else {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, gamesFragment)
                                    .addToBackStack(GamesFragment.TAG)
                                    .commit();
                        }
                        break;
                    case R.id.action_wishlist:
                        final Fragment wishlistFragment = supportFragmentManager.findFragmentByTag(WishlistFragment.TAG);
                        if (wishlistFragment == null) {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, WishlistFragment.newInstance(), WishlistFragment.TAG)
                                    .addToBackStack(WishlistFragment.TAG)
                                    .commit();
                        } else {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, wishlistFragment)
                                    .addToBackStack(WishlistFragment.TAG)
                                    .commit();
                        }
                        break;
                    case R.id.action_search:
                        final Fragment searchFragment = supportFragmentManager.findFragmentByTag(SearchFragment.TAG);
                        if (searchFragment == null) {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, SearchFragment.newInstance(), SearchFragment.TAG)
                                    .addToBackStack(SearchFragment.TAG)
                                    .commit();
                        } else {
                            supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, searchFragment)
                                    .addToBackStack(SearchFragment.TAG)
                                    .commit();
                        }
                        break;
                }
                return true;
            }
        });
        //Manually displaying the first fragment - one time only
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, MyGamesFragment.newInstance(), MyGamesFragment.TAG)
                .addToBackStack(null)
                .commit();
        //Prepare the platform and genre files
        loadJSONAssets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        FragmentManager fm = getSupportFragmentManager();
        GamesFragment gamesFragment = (GamesFragment)fm.findFragmentByTag(GamesFragment.TAG);
        List<IgdbGame> igdbGames = new ArrayList<>();
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menuSortAggregatedRating:
                //Write a callback to the fragment
                break;
            // action with ID action_settings was selected
            case R.id.menuSortRating:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.menuSortReleaseDate:
                Toast.makeText(this, "Sort release date", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.menuGetNewestGames:
                gamesFragment.prepareFragmentForNewData();
                gamesFragment.showProgressDialog();
                sIgdbClientSingleton.setCurrentOffset(0);
                sIgdbClientSingleton.updateOffset();
                sIgdbClientSingleton.setCurrentRequestParameters(sIgdbClientSingleton.
                        getGamesOrderedByNewestReleasesURL());
                sIgdbClientSingleton.buildFullRequest();
                getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), igdbGames);
                changeToolbarSubtitleText(getString(R.string.get_newest_releases));
                break;
            // action with ID action_settings was selected
            case R.id.menuGetHighestRatings6Months:
                gamesFragment.prepareFragmentForNewData();
                gamesFragment.showProgressDialog();
                sIgdbClientSingleton.setCurrentOffset(0);
                sIgdbClientSingleton.updateOffset();
                sIgdbClientSingleton.setCurrentRequestParameters(sIgdbClientSingleton.
                        getGamesHighestRatingLastXMonths(6));
                sIgdbClientSingleton.buildFullRequest();
                getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), igdbGames);
                changeToolbarSubtitleText(getString(R.string.get_highest_rating_6_months));
                break;
            case R.id.menuGetHighestRatings12Months:
                gamesFragment.prepareFragmentForNewData();
                gamesFragment.showProgressDialog();
                sIgdbClientSingleton.setCurrentOffset(0);
                sIgdbClientSingleton.updateOffset();
                sIgdbClientSingleton.setCurrentRequestParameters(sIgdbClientSingleton.
                        getGamesHighestRatingLastXMonths(12));
                sIgdbClientSingleton.buildFullRequest();
                getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), igdbGames);
                changeToolbarSubtitleText(getString(R.string.get_highest_rating_12_months));
                break;
            case R.id.menuGetHighestRatingsAllTime:
                //All time sets the date to 0
                gamesFragment.prepareFragmentForNewData();
                gamesFragment.showProgressDialog();
                sIgdbClientSingleton.setCurrentOffset(0);
                sIgdbClientSingleton.updateOffset();
                sIgdbClientSingleton.setCurrentRequestParameters(sIgdbClientSingleton.
                        getGamesHighestRatingLastXMonths(0));
                sIgdbClientSingleton.buildFullRequest();
                getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), igdbGames);
                changeToolbarSubtitleText(getString(R.string.get_highest_rating_all_time));
                break;
            case R.id.menuGetPopularGames:
                gamesFragment.prepareFragmentForNewData();
                gamesFragment.showProgressDialog();
                sIgdbClientSingleton.setCurrentOffset(0);
                sIgdbClientSingleton.updateOffset();
                sIgdbClientSingleton.setCurrentRequestParameters(sIgdbClientSingleton.
                        getGamesOrderedByPopularityURL());
                sIgdbClientSingleton.buildFullRequest();
                getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), igdbGames);
                changeToolbarSubtitleText(getString(R.string.get_popular_games));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadJSONAssets(){
        //Loads the assets file with the console names
        if (mConsoleFromAssets == null){
            try{
                InputStream is = getAssets().open("platforms.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                mConsoleFromAssets = new String(buffer, "UTF-8");
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Could not resolve console names", Toast.LENGTH_SHORT).show();
            }
        }
        if (mGenreFromAssets == null){
            try{
                InputStream is = getAssets().open("genres.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                mGenreFromAssets = new String(buffer, "UTF-8");
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "Could not resolve genres", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickThumbnail(String clourdinaryID) {
        String protocol = sIgdbClientSingleton.getUrlCoverBig2x() + clourdinaryID + sIgdbClientSingleton.getImageFormatJpg();
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_preview);
        ImageView ivPreview = (ImageView) dialog.findViewById(R.id.iv_preview_image);
        Picasso.with(this).
                load(protocol).
                error(R.drawable.ic_error).
                placeholder(R.drawable.ic_img_placeholder).
                into(ivPreview);
        Log.d(TAG, protocol);
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    //Method to resolve names of the platforms based on the id that is returned by the API.
    //Uses a JSON file from the Assets folder.
    public  String resolvePlatformNames(IgdbReleaseDates[] igdbReleaseDates, String name) {
        String consoles = "";
        if (mConsoleFromAssets != null){
            //Converts the variable into a JSON Object - consoles IS an Object
            JSONArray jsonArray = new JSONArray();
            try {
                JSONObject jsonObject = new JSONObject(mConsoleFromAssets);
                //Gets the array inside it
                jsonArray = jsonObject.getJSONArray("consoles");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Check in the array for the id. If it matches then it will add the name to the to be
            //returned String
            if(igdbReleaseDates != null){    //To avoid an Exception, some games have no release date yet
                for (int i = 0; i < igdbReleaseDates.length; i++){
                    for (int j = 0; j < jsonArray.length(); j++){
                        try{
                            JSONObject in_obj = jsonArray.getJSONObject(j);
                            //SHIT... it worked. AWESOME!!!!
                            if (in_obj.getInt("id") == igdbReleaseDates[i].getPlatform()
                                    && !consoles.contains(in_obj.getString("name"))) {
                                consoles += in_obj.getString("name") + " ";
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "JSONException: " + e);
                        }
                    }
                }
            }
        }
        return consoles;
    }

    public  String resolveGenreNames(int[] genres) {
        String output = "";
        if (mGenreFromAssets != null){
            //Converts the variable into a JSON Object - consoles IS an Object
            JSONArray jsonArray = new JSONArray();
            try {
                JSONObject jsonObject = new JSONObject(mGenreFromAssets);
                //Gets the array inside it
                jsonArray = jsonObject.getJSONArray("genres");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Check in the array for the id. If it matches then it will add the name to the to be
            //returned String
            if(genres != null){    //To avoid an Exception, some games have no release date yet
                for (int i = 0; i < genres.length; i++){
                    for (int j = 0; j < jsonArray.length(); j++){
                        try{
                            JSONObject in_obj = jsonArray.getJSONObject(j);
                            //SHIT... it worked. AWESOME!!!!
                            if (in_obj.getInt("id") == genres[i]
                                    && !output.contains(in_obj.getString("name"))) {
                                output += in_obj.getString("name") + " ";
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "JSONException: " + e);
                        }
                    }
                }
            }
        }
        return output;
    }
    //Saves a game to the database from the GamesFragment
    public void saveGamesToRealm (final IgdbGame igdbGame, final int tag){
        //Tags: 0 for database, 1 for wish list
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Game game = bgRealm.createObject(Game.class, igdbGame.getId());
                game.setGameName(igdbGame.getName());
                game.setAggregated_rating(igdbGame.getAggregated_rating());
                game.setCloudinaryId(igdbGame.getIgdbGameCover().getCloudinaryId());
                game.setDatabaseOrWishlist(tag);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, igdbGame.getName() + " saved to database");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, igdbGame.getName() + " not saved to database");
                Log.d(TAG, error.toString());
            }
        });
    }
    //Returns games saved in the Database, only MyGames - Tag is 0
    public RealmResults<Game> getMyGames(){
        RealmQuery<Game> query = mRealm.where(Game.class);
        query.equalTo("mDatabaseOrWishlist", 0);
        mGameRealmQuery = query.findAll();
        Log.d(TAG, mGameRealmQuery.toString());
        return mGameRealmQuery;
    }

    public void removeGameFromDatabase(final long id){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Game> games = mRealm.where(Game.class)
                        .equalTo("id", id)
                        .findAll();
                games.deleteFirstFromRealm();
            }
        });
    }

    public void getGamesFromAPI(String url, final List<IgdbGame> igdbGames){
        //Standard request
        // Request an Array response from the provided URL.
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
                        IgdbGame igdbGame = mapper.readValue(data, IgdbGame.class);
                        igdbGames.add(igdbGame);
                        Log.d(TAG, igdbGames.size()+"");
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
                FragmentManager fm = getSupportFragmentManager();
                GamesFragment fragment = (GamesFragment)fm.findFragmentByTag(GamesFragment.TAG);
                fragment.showsGamesList(igdbGames);
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

    public IgdbGame getASingleGameFromAPI(int gameId){
        String url = sIgdbClientSingleton.getSingleGameDetails(gameId);
        mItemsHashMap = new HashMap<>();
        Log.d("Single", url);
        final JsonArrayRequest req = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                mJSONArraySingleGame = response;
                for(int i = 0; i < response.length(); i++){
                    try {
                        Log.d("Single", i + "");
                        JSONObject object = response.getJSONObject(i);
                        String key = object.keys().next();
                        mItemsHashMap.put(key, object.getString(key));
                        String data = object.toString();
                        IgdbGame game = mapper.readValue(data, IgdbGame.class);
                        igdbGame = game;
                        resolveCompanyNameFromAPI(igdbGame.getDevelopers()[0], igdbGame.getPublishers()[0]);
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
        return igdbGame;
    }

    public JSONArray getJSONArraySingleGame(){
        return mJSONArraySingleGame;
    }

    public HashMap<String, String> getItemsHashMap(){
        return mItemsHashMap;
    }

    public void resolveCompanyNameFromAPI(int dev, int pub){
        //One request, 2 objects: one dev and one pub.
        String url = sIgdbClientSingleton.getCompanyNamesURL(dev, pub);
        mCompanies = new ArrayList<>();
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
                        mCompanies.add(mIgdbCompany.getName());
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
                //Call fragment here
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                Fragment gameDetails = supportFragmentManager.findFragmentByTag(GameDetailsFragment.TAG);
                supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, GameDetailsFragment.newInstance(), GameDetailsFragment.TAG)
                        .addToBackStack(GameDetailsFragment.TAG)
                        .commit();
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

    public IgdbGame getIgdbGame(){
        return igdbGame;
    }

    public List<String> getCompanies(){
        return mCompanies;
    }

    public void showAddGameDialog(final IgdbGame game){
        final String[] items = {getString(R.string.text_add_to_database), getString(R.string.text_add_to_wishlist)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_game_alert_dialog_title));

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentManager fm = getSupportFragmentManager();
                GamesFragment fragment = (GamesFragment)fm.findFragmentByTag(GamesFragment.TAG);
                fragment.showSnackBar(i);
                saveGamesToRealm(game, i);
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void changeToolbarSubtitleText(String text){
        mToolbarText.setText(text);
    }

}
