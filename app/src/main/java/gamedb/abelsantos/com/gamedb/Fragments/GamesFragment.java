package gamedb.abelsantos.com.gamedb.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private static final int FLAG_GAMES_BY_RATING = 1;
    private static final int FLAG_GAMES_BY_AGGREGATED_RATING = 2;
    private static final int FLAG_GAMES_BY_NEWEST_RELEASES = 3;

    private RecyclerView mRecyclerView;
    private GameListAdapter mGameListAdapter;

    private static final String tag_json_arry = "json_array_req";


    private TextView mGameTitle;
    private TextView mGameGenre;
    private TextView mGameAggregatedRating;
    private TextView mGameReleaseDate;
    private TextView mGamePlatforms;
    private ProgressDialog mProgressDialog;
    private IgdbClientSingleton sIgdbClientSingleton;
    private ImageLoader mImageLoader;
    private NetworkImageView mNetworkImageView;
    private ImageView mThumbnail;
    private ImageButton mAddButton;
    private CoordinatorLayout mCoordinatorLayout;

    private List<IgdbGame> mItems = new ArrayList<>();
    private EndlessRecyclerViewScrollListener mScrollListener;
    private int mCounter = 1;
    private Fragment mContent;

    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
                Activity activity = (GameDbLauncher)getActivity();
                if (activity instanceof GameDbLauncher){
                    //Updates the offset in the current request to +20
                    sIgdbClientSingleton.setCurrentOffset(sIgdbClientSingleton.getCurrentOffset() + 20);
                    sIgdbClientSingleton.updateOffset();
                    //Request Parameters has already been set
                    sIgdbClientSingleton.buildFullRequest();
                    ((GameDbLauncher) activity).getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), mItems);
                    mCounter++;
                    Toast.makeText(getContext(), "Page " + mCounter + "", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        showProgressDialog();
        setupAdapter();
        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText(getString(R.string.get_popular_games));
        return view;
    }

    public void showSnackBar(int radioButton){
        String text = getString(R.string.game_added_to_database);
        if (radioButton == 1){
            text = getString(R.string.game_added_to_wishlist);
        }
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void showProgressDialog(){
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.loading));
        //To avoid showing the progress dialog when the user goes back to the fragment.
        if (mItems.size() < 1){
            mProgressDialog.show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.action_sort).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            mContent = getFragmentManager().getFragment(savedInstanceState, TAG);
            Log.d(TAG, "Recovered");
        }else{
            Activity activity = getActivity();
            if (activity instanceof GameDbLauncher){
                sIgdbClientSingleton.setCurrentRequestParameters(sIgdbClientSingleton.
                        getGamesOrderedByPopularityURL());
                sIgdbClientSingleton.updateOffset();
                sIgdbClientSingleton.buildFullRequest();
                ((GameDbLauncher) activity).getGamesFromAPI(sIgdbClientSingleton.getCurrentRequestFull(), mItems);
                Log.d(TAG, sIgdbClientSingleton.getCurrentRequestParameters());
            }
        }
    }

    public void prepareFragmentForNewData(){
        mItems.clear();
        mGameListAdapter.notifyDataSetChanged();
        mRecyclerView.removeAllViewsInLayout();
    }

    public void showsGamesList(List<IgdbGame> items){
        mItems = items;
        mProgressDialog.hide();
        mGameListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        getFragmentManager().putFragment(savedInstanceState, TAG, mContent);
    }

    private void setupAdapter(){
        mGameListAdapter = new GameListAdapter();
        mRecyclerView.setAdapter(mGameListAdapter);
    }


    private class GameHolder extends RecyclerView.ViewHolder{


        private GameHolder(final View itemView) {
            super(itemView);
            mGameAggregatedRating = (TextView)itemView.findViewById(R.id.txt_game_rating);
            mGameTitle = (TextView) itemView.findViewById(R.id.txt_gameTitle);
            mGameGenre = (TextView)itemView.findViewById(R.id.txt_game_genre) ;
            mGamePlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform);
            mGameReleaseDate = (TextView)itemView.findViewById(R.id.txt_game_release_date);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mAddButton = (ImageButton)itemView.findViewById(R.id.button_add_database);
            //mNetworkImageView = (NetworkImageView)itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = mItems.get(getLayoutPosition()).getId();
                    ((GameDbLauncher)getActivity()).getASingleGameFromAPI(id);
                }
            });
        }

        private void bindGameListItem(final IgdbGame igdbGame){
            mGameTitle.setText(igdbGame.getName());
            int rat = ((int) igdbGame.getAggregated_rating());
            if(rat == 0){
                mGameAggregatedRating.setText("-");
            }else{
                mGameAggregatedRating.setText(rat + "");
            }
            //Only release year will be shown here. A more detailed date can be shown in the details.
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(igdbGame.getReleaseDate()));
            mGameReleaseDate.setText(date);
            String consoles = ((GameDbLauncher)getActivity()).resolvePlatformNames(igdbGame.getIgdbReleaseDates(), igdbGame.getName());
            mGamePlatforms.setText(consoles);
            final String genres = ((GameDbLauncher)getActivity()).resolveGenreNames(igdbGame.getGenre());
            mGameGenre.setText(genres);
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GameDbLauncher)getActivity()).showAddGameDialog(igdbGame);
                }
            });
            String protocol = "";
            try{
                //Get the cloudniranyID... this is the id I need to access the images I need.
                //I then need to change the parameter in the query url to get the size i need delivered.
                 protocol = sIgdbClientSingleton.getUrlCoverBig() +
                         igdbGame.getIgdbGameCover().getCloudinaryId() + sIgdbClientSingleton.getImageFormatJpg();
                //Log.d(TAG, protocol);
            }catch (NullPointerException e){
                Log.d(TAG, e.toString());
            }
            //mImageLoader = sIgdbClientSingleton.getImageLoader();
            //mNetworkImageView.setImageUrl(protocol, mImageLoader);
            /*Picasso needs a valid url. If the returned url is not valid
            * then I will set the thumbnail with the default error image.
            * Else, picasso will do its thing*/
            /*ATTENTION: CHANGE. I will request a higher resolution of the picture and rescale it
            * */
            if ( !protocol.equals("")){
                Picasso.with(getContext()).
                        load(protocol).
                        error(R.drawable.ic_error).
                        placeholder(R.drawable.ic_img_placeholder).
                        into(mThumbnail);
                mThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GameDbLauncher)getActivity()).onClickThumbnail( igdbGame.getIgdbGameCover().getCloudinaryId());
                    }
                });
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
