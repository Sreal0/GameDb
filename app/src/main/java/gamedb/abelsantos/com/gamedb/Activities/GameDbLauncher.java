package gamedb.abelsantos.com.gamedb.Activities;


import android.app.ProgressDialog;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
    private static final String tag_json_arry = "json_array_req";
    private static final int TAG_DATABASE = 1;
    private static final int TAG_WISHLIST = 2;

    private Toolbar mToolbar;
    private TextView mToolbarText;
    private BottomNavigationView mBottomNavigationView;
    private String mConsoleFromAssets;
    private Realm mRealm;
    private RealmResults<Game> mGameRealmQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        mRealm = Realm.getDefaultInstance();

        //Navigation Bar onClickListener
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_mygames:
                        fragment = MyGamesFragment.newInstance();
                        break;
                    case R.id.action_games:
                        fragment = GamesFragment.newInstance();
                        break;
                    case R.id.action_wishlist:
                        fragment = WishlistFragment.newInstance();
                        break;
                    case R.id.action_search:
                        fragment = SearchFragment.newInstance();
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MyGamesFragment.newInstance());
        transaction.commit();
        //Prepare the Consoles file
        loadJSONFromAsset();

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

    public  String resolveConsoleNames(IgdbReleaseDates[] igdbReleaseDates, String name) {
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

    public void saveGames (final IgdbGame igdbGame, final int tag){
        //TODO: add a check to see if the game ID is already saved.
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Game game = bgRealm.createObject(Game.class, igdbGame.getId());
                game.setGameName(igdbGame.getName());
                game.setRating(igdbGame.getRating());
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

    public RealmResults<Game> getMyGames(){
        RealmQuery<Game> query = mRealm.where(Game.class);
        query.equalTo("mDatabaseOrWishlist", 1);

        mGameRealmQuery =  query.findAll();
        Log.d(TAG, mGameRealmQuery.toString());

        return mGameRealmQuery;
    }

}
