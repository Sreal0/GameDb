package gamedb.abelsantos.com.gamedb.Activities;


import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

public class GameDbLauncher extends AppCompatActivity {

    private static String TAG = "gamedb.abelsantos";
    private static final String tag_json_arry = "json_array_req";


    private TextView mGameTitle;
    private TextView mGameRating;
    private ImageView mGameCover;
    private TextView mGameReleaseDate;
    private TextView mGamePlatforms;
    private ProgressDialog mProgressDialog;
    private IgdbClientSingleton sIgdbClientSingleton;
    private ImageLoader mImageLoader;
    private NetworkImageView mNetworkImageView;

    private List<IgdbGame> mItems = new ArrayList<>();

    private Toolbar mToolbar;
    private TextView mToolbarText;
    private RecyclerView mRecyclerView;
    private GameListAdapter mGameListAdapter;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        //Setting up NavMenu

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewLauncher);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //Navigation Bar onClickListener
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Toast.makeText(getApplicationContext(), "My Games", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_database:
                        Toast.makeText(getApplicationContext(), "Games", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_wishlist:
                        Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                        Toast.makeText(getApplicationContext(), "Wish list", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        getGames();
    }

    private void setupAdapter(){
        mGameListAdapter = new GameListAdapter(mItems);
        mRecyclerView.setAdapter(mGameListAdapter);
        mGameListAdapter.notifyDataSetChanged();
    }

    public List<IgdbGame> getGames(){
        String url = sIgdbClientSingleton.getGamesURLGames();
        // Request a string response from the provided URL.
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
                        mItems.add(igdbGame);
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
                mProgressDialog.hide();
                setupAdapter();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        sIgdbClientSingleton.addToRequestQueue(req, tag_json_arry);
        Log.d(TAG, "Size of list " + mItems.size());
        return mItems;
    }

    private class GameHolder extends RecyclerView.ViewHolder{

        public GameHolder(View itemView) {
            super(itemView);
            mGameRating = (TextView)itemView.findViewById(R.id.txt_game_rating);
            mGameTitle =  (TextView) itemView.findViewById(R.id.txt_gameTitle);
            mGameCover = (ImageView)itemView.findViewById(R.id.thumbnail);
            mGamePlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform);
            mGameReleaseDate = (TextView)itemView.findViewById(R.id.txt_game_release_date);
            if (mImageLoader == null){
                mImageLoader = sIgdbClientSingleton.getImageLoader();
            }
            mNetworkImageView = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
        }

        public void bindGameListItem(IgdbGame igdbGame){
            mGameTitle.setText(igdbGame.getName());
            int rat = ((int) igdbGame.getRating());
            mGameRating.setText(getString(R.string.game_rating)+ " " + rat);
            String date = android.text.format.DateFormat.format("MM/dd/yyyy", new Date(igdbGame.getReleaseDate())).toString();
            mGameReleaseDate.setText(date);
            //To avoid getting an Exception because of the protocol. Note that the Url from the
            //IgdbGameCover already has // in it.
            String protocol = "https:" + igdbGame.getIgdbGameCover().getUrl();
            Log.d(TAG, protocol);
            mNetworkImageView.setImageUrl(protocol, mImageLoader);
        }
    }

    private class GameListAdapter extends RecyclerView.Adapter<GameHolder>{

        private List<IgdbGame> mIgdbGames;

        public GameListAdapter(List<IgdbGame> igdbGames){
            mIgdbGames = igdbGames;
        }
        @Override
        public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplication());
            View view = layoutInflater.inflate(R.layout.list_view_game, parent, false);
            return new GameHolder(view);
        }

        @Override
        public void onBindViewHolder(GameHolder holder, int position) {
            IgdbGame igdbGame = mIgdbGames.get(position);
            holder.bindGameListItem(igdbGame);

        }

        @Override
        public int getItemCount() {
            return mIgdbGames.size();
        }
    }

}
