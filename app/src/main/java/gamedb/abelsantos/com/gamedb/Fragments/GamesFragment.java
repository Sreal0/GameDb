package gamedb.abelsantos.com.gamedb.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 20.01.2017.
 */

public class GamesFragment extends Fragment {
    private static final String TAG = "GamesFragment";

    private RecyclerView mRecyclerView;
    private GameListAdapter mGameListAdapter;

    private static final String tag_json_arry = "json_array_req";


    private TextView mGameTitle;
    private TextView mGameAggregatedRating;
    private ImageView mGameCover;
    private TextView mGameReleaseDate;
    private TextView mGamePlatforms;
    private ProgressDialog mProgressDialog;
    private IgdbClientSingleton sIgdbClientSingleton;
    private ImageLoader mImageLoader;
    private NetworkImageView mNetworkImageView;

    private List<IgdbGame> mItems = new ArrayList<>();

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

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewGames);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        //Setting up the adapter to avoid getting -> No adapter attached; skipping layout
        //When data was gotten then the adapter will be notified
        setupAdapter();
        getGames();

        //This is how I call a method from the activity on a fragment.
        //((GameDbLauncher)getActivity()).thisIsAMethod();
        return view;
    }

    private void setupAdapter(){
        mGameListAdapter = new GameListAdapter();
        mRecyclerView.setAdapter(mGameListAdapter);
    }

    public void getGames(){
        String url = sIgdbClientSingleton.getGamesURLGames();
        // Request an Array response from the provided URL.
        final JsonArrayRequest req = new JsonArrayRequest(
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                //Log.d(TAG, response.toString());
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
            mNetworkImageView = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
        }

        private void bindGameListItem(IgdbGame igdbGame){
            mGameTitle.setText(igdbGame.getName());
            int rat = ((int) igdbGame.getAggregated_rating());
            mGameAggregatedRating.setText(getString(R.string.game_rating)+ " " + rat);
            String date = android.text.format.DateFormat.format("MM/dd/yyyy", new Date(igdbGame.getReleaseDate())).toString();
            mGameReleaseDate.setText(date);
            String consoles = ((GameDbLauncher)getActivity()).resolveConsoleNames(igdbGame.getIgdbReleaseDates(), igdbGame.getName());
            mGamePlatforms.setText(consoles);
            //To avoid getting an Exception because of the protocol. Note that the Url from the
            //IgdbGameCover already has // in it.
            String protocol = "https:" + igdbGame.getIgdbGameCover().getUrl();
            mImageLoader = sIgdbClientSingleton.getImageLoader();
            mNetworkImageView.setImageUrl(protocol, mImageLoader);

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
            final
            IgdbGame igdbGame = mItems.get(position);
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
