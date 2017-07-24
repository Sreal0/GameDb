package gamedb.abelsantos.com.gamedb.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView mScore;
    private TextView mPlatforms;
    private TextView mReleaseDate;
    private ImageButton mRemoveOrMoveOptions;
    private List<Game> mGames;
    private Context mContext;
    private IgdbClientSingleton sIgdbClientSingleton;


    private static final String URL_COVER_BIG = "https://images.igdb.com/igdb/image/upload/t_cover_big/";
    private static final String IMAGE_FORMAT_PNG = ".png";
    private static final String IMAGE_FORMAT_JPG = ".jpg";
    private static final int TAG_DATABASE = 0;
    private static final int TAG_WISHLIST = 1;

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
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail_wishlist);
            mGameName = (TextView)itemView.findViewById(R.id.txt_gameTitle_wishlist);
            mScore = (TextView)itemView.findViewById(R.id.txt_game_rating_wishlist);
            mPlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform_wishlist);
            mReleaseDate = (TextView)itemView.findViewById(R.id.txt_game_release_date_wishlist);
            mRemoveOrMoveOptions = (ImageButton)itemView.findViewById(R.id.button_wishlist_options);
        }

        private void bindItem(final Game game){
            mGameName.setText(game.getGameName());
            int rating = ((int)game.getAggregated_rating());
            mScore.setText(String.valueOf(rating));
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
                    showAlertDialog(game.getId());
                    mGames = ((GameDbLauncher)mContext).getGamesFromRealm(TAG_WISHLIST);
                }
            });
        }
    }

    public void showAlertDialog(final long id){
        String[] items = {mContext.getString(R.string.text_move_game_to_database), mContext.getString(R.string.text_remove_game_from_wishlist)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext.getApplicationContext(), R.style.MyDialogTheme);
        builder.setTitle(mContext.getString(R.string.add_game_alert_dialog_title));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    ((GameDbLauncher)mContext).saveGamesToRealm(id, i);
                    notifyDataSetChanged();
                    dialogInterface.dismiss();
                }//Removes game from wishlist
                else{
                    ((GameDbLauncher)mContext).removeGameFromDatabase(id);
                    notifyDataSetChanged();
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

