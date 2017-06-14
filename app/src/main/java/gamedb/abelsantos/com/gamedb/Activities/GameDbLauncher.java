package gamedb.abelsantos.com.gamedb.Activities;



import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.Fragments.GamesFragment;
import gamedb.abelsantos.com.gamedb.Fragments.MyGamesFragment;
import gamedb.abelsantos.com.gamedb.Fragments.SearchFragment;
import gamedb.abelsantos.com.gamedb.Fragments.WishlistFragment;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
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
    private Realm mRealm;
    private Game results;
    private RealmResults<Game> mGameRealmQuery;
    private IgdbClientSingleton sIgdbClientSingleton;
    private List<IgdbGame> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        mRealm = Realm.getDefaultInstance();

        mToolbar = (Toolbar)findViewById(R.id.toolbar_launcher);
        mToolbar.setTitle("GameDb");
        mToolbar.setTitleTextColor(0xffffffff);
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        setSupportActionBar(mToolbar);

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
        //Prepare the Consoles file
        loadJSONFromAsset();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.menuGetNewestGames:
                Toast.makeText(this, "Filtered something", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.menuGetHighestRatings6Months:
                Toast.makeText(this, "Filtered something else", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.menuGetHighestRatings12Months:
                Toast.makeText(this, "Filtered something else", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.menuGetHighestRatingsAllTime:
                Toast.makeText(this, "Filtered something else", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.menuGetPopularGames:
                Toast.makeText(this, "Filtered something else", Toast.LENGTH_SHORT)
                        .show();
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

    public void loadJSONFromAsset(){
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

    //Saves a game to the database from the GamesFragment
    public void saveGames (final IgdbGame igdbGame, final int tag){
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

    //Returns games saved in the Database, only MyGames
    public RealmResults<Game> getMyGames(){
        RealmQuery<Game> query = mRealm.where(Game.class);
        query.equalTo("mDatabaseOrWishlist", 1);

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

    public void getGamesFromAPI(int offset, final List<IgdbGame> igdbGames){
        //Standard request
        String mStringURL = sIgdbClientSingleton.getGamesOrderedByPopularityURL(offset);
        Log.d(TAG, mStringURL);
        Log.d(TAG, "Offset " + offset);
        // Request an Array response from the provided URL.
        final JsonArrayRequest req = new JsonArrayRequest(
                mStringURL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String data = object.toString();
                        IgdbGame igdbGame = mapper.readValue(data, IgdbGame.class);
                        igdbGames.add(igdbGame);
                        Log.d(TAG, "Added a new Item to items");
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
                Log.d(TAG, fragment.getClass().getName());
                fragment.showsGamesList(igdbGames);
                Log.d(TAG, "Called method in Activity");
                Log.d(TAG, mItems.size() + "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        //req.setShouldCache(true);
        sIgdbClientSingleton.addToRequestQueue(req);
    }
}
