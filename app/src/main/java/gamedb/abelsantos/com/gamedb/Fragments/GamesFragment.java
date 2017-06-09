package gamedb.abelsantos.com.gamedb.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Fragments.Helpers.EndlessRecyclerViewScrollListener;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 20.01.2017.
 */

public class GamesFragment extends Fragment {
    public static final String TAG = "GamesFragment";
    private static final int TAG_DATABASE = 1;
    private static final int TAG_WISHLIST = 2;

    private RecyclerView mRecyclerView;
    private GameListAdapter mGameListAdapter;

    private static final String tag_json_arry = "json_array_req";


    private TextView mGameTitle;
    private TextView mGameAggregatedRating;
    private TextView mGameReleaseDate;
    private TextView mGamePlatforms;
    private ProgressDialog mProgressDialog;
    private IgdbClientSingleton sIgdbClientSingleton;
    private ImageLoader mImageLoader;
    private NetworkImageView mNetworkImageView;
    private ImageView mThumbnail;
    private ImageButton mAddToDatabaseButton;
    private ImageButton mAddToWishlistButton;
    private CoordinatorLayout mCoordinatorLayout;

    private List<IgdbGame> mItems = new ArrayList<>();
    private EndlessRecyclerViewScrollListener mScrollListener;
    private String mStringURL;
    private int offset = 0;
    private int counter = 0;

    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        mCoordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout_games_fragment);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewGames);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        //Infinite Scrolling
        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                offset += 20;
                //Same method as before, just sets the offset to + 20
                getGames(offset);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        //Setting up the adapter to avoid getting -> No adapter attached; skipping layout
        //When data was gotten then the adapter will be notified

        //This is how I call a method from the activity on a fragment.
        //((GameDbLauncher)getActivity()).thisIsAMethod();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            mItems = (List<IgdbGame>)savedInstanceState.getSerializable("games");
            offset = savedInstanceState.getInt("offset");
            counter = savedInstanceState.getInt("counter");
            mGameListAdapter.notifyDataSetChanged();
            Log.d(TAG, "Loaded stuff");
        }else{
            Log.d(TAG, "did everything again");
            setupAdapter();
            getGames(offset);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("games", (Serializable)mItems);
        savedInstanceState.putInt("offset", offset);
        savedInstanceState.putInt("counter", counter);
        Log.d("TAg", "Saved the instance" + savedInstanceState.toString());
    }

    private void setupAdapter(){
        mGameListAdapter = new GameListAdapter();
        mRecyclerView.setAdapter(mGameListAdapter);
    }

    public void getGames(int offset){
        //Standard request
        mStringURL = sIgdbClientSingleton.getGamesOrderedByPopularityURL(offset);
        counter++;
        Toast.makeText(getContext(), counter + "", Toast.LENGTH_SHORT).show();
        Log.d(TAG, mStringURL);
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
                //Notify that the data changed.
                mGameListAdapter.notifyDataSetChanged();
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

    private class GameHolder extends RecyclerView.ViewHolder{

        private GameHolder(View itemView) {
            super(itemView);
            mGameAggregatedRating = (TextView)itemView.findViewById(R.id.txt_game_rating);
            mGameTitle =  (TextView) itemView.findViewById(R.id.txt_gameTitle);
            mGamePlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform);
            mGameReleaseDate = (TextView)itemView.findViewById(R.id.txt_game_release_date);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mAddToDatabaseButton = (ImageButton)itemView.findViewById(R.id.button_add_database);
            mAddToWishlistButton = (ImageButton)itemView.findViewById(R.id.button_add_wishlist);
            //mNetworkImageView = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
        }

        private void bindGameListItem(final IgdbGame igdbGame){
            mGameTitle.setText(igdbGame.getName());
            int rat = ((int) igdbGame.getAggregated_rating());
            mGameAggregatedRating.setText(getString(R.string.game_rating)+ " " + rat);
            //Only release year will be shown here. A more detailed date can be shown in the details.
            mGameReleaseDate.setText(igdbGame.resolveFirstReleaseYear());
            String consoles = ((GameDbLauncher)getActivity()).resolvePlatformNames(igdbGame.getIgdbReleaseDates(), igdbGame.getName());
            mGamePlatforms.setText(consoles);
            mAddToDatabaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GameDbLauncher) getActivity()).saveGames(igdbGame, TAG_DATABASE);
                    Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.game_added_to_database), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
            mAddToWishlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GameDbLauncher) getActivity()).saveGames(igdbGame, TAG_WISHLIST);
                    Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.game_added_to_wishlist), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
            /*To avoid getting an Exception because of the protocol.
             Note that the Url from the IgdbGameCover already has // in it.
             Same games still have no cover. */
            String protocol = "";
            try{
                 protocol = "https:" + igdbGame.getIgdbGameCover().getUrl();
                Log.d(TAG, protocol);
            }catch (NullPointerException e){
                Log.d(TAG, e.toString());
            }
            //mImageLoader = sIgdbClientSingleton.getImageLoader();
            //mNetworkImageView.setImageUrl(protocol, mImageLoader);
            /*Picasso needs a valid url. If the returned url is not valid
            * then I will set the thumbnail with the default error image.
            * Else, picasso will do its thing*/
            if (protocol != ""){
                Picasso.with(getContext()).
                        load(protocol).
                        error(R.drawable.ic_error).
                        placeholder(R.drawable.ic_img_placeholder).
                        resize(75, 100).
                        into(mThumbnail);
            }else{
                mThumbnail.setImageResource(R.drawable.ic_error);
            }
        }
    }

    private class GameListAdapter extends RecyclerView.Adapter<GameHolder>{
        public GameListAdapter(){
        }
        @Override
        public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_view_game, parent, false);
            return new GameHolder(view);
        }
        @Override
        public void onBindViewHolder(GameHolder holder, int position) {
            final IgdbGame igdbGame = mItems.get(position);
            holder.bindGameListItem(igdbGame);
        }
        @Override
        public int getItemCount() {
            return mItems.size();
        }
        /*Super important: I must override getItemViewType otherwise items in the RV will
        * change order and I will get a strange behavior (Items were repeating and there was
        * one crazy item that kept changing to another items).*/
        @Override
        public int getItemViewType(int position){
            return position;
        }
    }

}
