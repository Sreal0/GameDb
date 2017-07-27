package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 29.05.2017.
 */

public class MyGamesFragment extends Fragment{
    public static final String TAG = "MyGamesFragment";
    private static final String URL_COVER_BIG = "https://images.igdb.com/igdb/image/upload/t_cover_big/";
    private static final String IMAGE_FORMAT_PNG = ".png";
    private static final String IMAGE_FORMAT_JPG = ".jpg";
    private static final int TAG_DATABASE = 0;
    private static final int TAG_WISHLIST = 1;


    private RecyclerView mRecyclerView;
    private MyGamesAdapter mMyGamesAdapter;
    private List<Game> mGames;
    private ImageView mThumbnail;
    private TextView mGameName;
    private TextView mScore;
    private TextView mGameGenre;
    private TextView mGamePlatforms;
    private TextView mReleaseDate;
    private ImageButton mRemoveGame;
    private Snackbar mSnackbar;
    private CoordinatorLayout mCoordinatorLayout;

    public static MyGamesFragment newInstance() {
        MyGamesFragment fragment = new MyGamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mygames, container, false);
        mCoordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout_my_games);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewMyGames);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mGames = ((GameDbLauncher) getActivity()).getGamesFromRealm(TAG_DATABASE);
        setupAdapter();
        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText("");
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void setupAdapter(){
        mMyGamesAdapter = new MyGamesAdapter();
        mRecyclerView.setAdapter(mMyGamesAdapter);
    }

    private class MyGamesHolder extends RecyclerView.ViewHolder{

        public MyGamesHolder(View itemView) {
            super(itemView);
            mGameName = (TextView)itemView.findViewById(R.id.txt_gameTitle_wishlist);
            mScore = (TextView)itemView.findViewById(R.id.txt_game_rating_wishlist);
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail_wishlist);
            mRemoveGame = (ImageButton)itemView.findViewById(R.id.button_remove_game);
            mGamePlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform_wishlist);
            mGameGenre = (TextView)itemView.findViewById(R.id.txt_game_genre_wishlist) ;
            mReleaseDate = (TextView) itemView.findViewById(R.id.txt_game_release_date_wishlist);

        }

        private void bingGameListItem(final Game game){
            mGameName.setText(game.getGameName());
            int rating = (int) game.getAggregated_rating();
            if(rating == 0){
                mScore.setText("N/A");
            }else{
                //Changes text color based on score.
                if(rating < 50){
                    mScore.setTextColor(ContextCompat.getColor(getContext(), R.color.color_review_5));
                }else if(rating < 65){
                    mScore.setTextColor(ContextCompat.getColor(getContext(), R.color.color_review_4));
                }else if(rating < 80){
                    mScore.setTextColor(ContextCompat.getColor(getContext(), R.color.color_review_3));
                }else if(rating < 91){
                    mScore.setTextColor(ContextCompat.getColor(getContext(), R.color.color_review_2));
                }else{
                    mScore.setTextColor(ContextCompat.getColor(getContext(), R.color.color_review_1));
                }
                mScore.setText(String.valueOf(rating));
            }
            mScore.setText(String.valueOf(rating));
            String protocol = "";
            try{
                protocol = URL_COVER_BIG + game.getCloudinaryId() + IMAGE_FORMAT_JPG;
                Log.d(TAG, protocol);
            }catch (NullPointerException e){
                Log.d(TAG, e.toString());
            }
            if (protocol != ""){
                Picasso.with(getContext()).
                        load(protocol).
                        error(R.drawable.ic_error).
                        placeholder(R.drawable.ic_img_placeholder).
                        into(mThumbnail);
            }else{
                mThumbnail.setImageResource(R.drawable.ic_error);
            }
            //Not final - games should only be removed from inside the details fragment.
            mRemoveGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Removes the game from the database, reloads the games list and
                    //notifies the recycler view about the changes.
                    ((GameDbLauncher) getActivity()).removeGameFromDatabase(game.getId());
                    mGames = ((GameDbLauncher)getActivity()).getGamesFromRealm(TAG_DATABASE);
                    setupAdapter();
                }
            });
        }
    }

    private class MyGamesAdapter extends RecyclerView.Adapter<MyGamesHolder>{

        @Override
        public MyGamesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_view_my_games, parent, false);
            return new MyGamesHolder(view);
        }

        @Override
        public void onBindViewHolder(MyGamesHolder holder, int position) {
            final Game game = mGames.get(position);
            holder.bingGameListItem(game);
        }

        @Override
        public int getItemCount() {
            return mGames.size();
        }
        @Override
        public int getItemViewType(int position){
            return position;
        }
    }
}
