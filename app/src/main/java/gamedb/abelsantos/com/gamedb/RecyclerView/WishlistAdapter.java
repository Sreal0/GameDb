package gamedb.abelsantos.com.gamedb.RecyclerView;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.Fragments.WishlistFragment;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 20.07.2017.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishListHolder>{
    private static String TAG = "WishlistAdapter";
    private ImageView mThumbnail;
    private TextView mGameName;
    private TextView mGameAggregatedRating;
    private TextView mPlatforms;
    private TextView mReleaseDate;
    private TextView mGameGenre;
    private ImageButton mRemoveOrMoveOptions;
    private List<Game> mGames;
    private Context mContext;
    private IgdbClientSingleton sIgdbClientSingleton;


    private static final String URL_COVER_BIG = "https://images.igdb.com/igdb/image/upload/t_cover_big/";
    private static final String IMAGE_FORMAT_PNG = ".png";
    private static final String IMAGE_FORMAT_JPG = ".jpg";
    private static final int TAG_DATABASE = 0;
    private static final int TAG_WISHLIST = 1;
    private static final String NOT_AVALIABLE = "n/a";

    public WishlistAdapter(List<Game> games, Context context) {
        mGames = games;
        mContext = context;
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();
    }

    @Override
    public WishListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_view_item_wishlist, parent, false);
        return new WishListHolder(view);
    }

    @Override
    public void onBindViewHolder(WishListHolder holder, int position) {
        final Game game = mGames.get(position);
        holder.bindItem(game);
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class WishListHolder extends RecyclerView.ViewHolder{


        public WishListHolder(View itemView) {
            super(itemView);
            mGameName = (TextView)itemView.findViewById(R.id.txt_gameTitle_Games);
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail_Games);
            mGameAggregatedRating = (TextView)itemView.findViewById(R.id.txt_game_rating_Games);
            mPlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform_wishlist);
            mReleaseDate = (TextView)itemView.findViewById(R.id.txt_game_release_date_Games);
            mRemoveOrMoveOptions = (ImageButton)itemView.findViewById(R.id.button_wishlist_options);
            mGameGenre = (TextView)itemView.findViewById(R.id.txt_game_genre_Games);
        }

        private void bindItem(final Game game){
            //Platform
            if (game.getPlatforms() != null){
                mPlatforms.setText(game.getPlatforms());
            }else{
                mPlatforms.setText(NOT_AVALIABLE);
            }
            //Release date
            if (game.getReleasedate() != null){
                mReleaseDate.setText(game.getReleasedate());
            }else{
                mReleaseDate.setText(NOT_AVALIABLE);
            }
            //Platforms
            if (game.getGenre() != null){
                mGameGenre.setText(game.getGenre());
            }else{
                mGameGenre.setText(NOT_AVALIABLE);
            }
            //Name
            mGameName.setText(game.getGameName());
            //Rating
            int rating = ((int)game.getAggregated_rating());
            if(rating == 0){
                mGameAggregatedRating.setText("n/a");
            }else{
                //Changes text color based on score.
                if(rating < 50){
                    mGameAggregatedRating.setTextColor(ContextCompat.getColor(mContext, R.color.color_review_5));
                }else if(rating < 65){
                    mGameAggregatedRating.setTextColor(ContextCompat.getColor(mContext, R.color.color_review_4));
                }else if(rating < 80){
                    mGameAggregatedRating.setTextColor(ContextCompat.getColor(mContext, R.color.color_review_3));
                }else if(rating < 91){
                    mGameAggregatedRating.setTextColor(ContextCompat.getColor(mContext, R.color.color_review_2));
                }else{
                    mGameAggregatedRating.setTextColor(ContextCompat.getColor(mContext, R.color.color_review_1));
                }
                mGameAggregatedRating.setText(String.valueOf(rating));
            }
            //Cover
            String protocol = "";
            try{
                protocol = URL_COVER_BIG + game.getCloudinaryId() + IMAGE_FORMAT_JPG;
                Log.d(TAG, protocol);
            }catch (NullPointerException e){
                Log.d(TAG, e.toString());
            }
            if (protocol != ""){
                Picasso.with(mContext).
                        load(protocol).
                        error(R.drawable.ic_error).
                        placeholder(R.drawable.ic_img_placeholder).
                        into(mThumbnail);
            }else{
                mThumbnail.setImageResource(R.drawable.ic_error);
            }
            mRemoveOrMoveOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Show the alert dialog where the user can choose to either move
                    //a game to the database or to remove it from the wish list
                    FragmentManager fm = ((GameDbLauncher)mContext).getSupportFragmentManager();
                    WishlistFragment fragment = (WishlistFragment) fm.findFragmentByTag(WishlistFragment.TAG);
                    fragment.showAlertDialog(game.getId());

                }
            });

        }
    }



}

