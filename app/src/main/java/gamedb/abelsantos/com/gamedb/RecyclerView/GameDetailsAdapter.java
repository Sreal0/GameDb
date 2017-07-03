package gamedb.abelsantos.com.gamedb.RecyclerView;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 03.07.2017.
 */

public class GameDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "GameDetailsAdapter";

    private static final int TOP = 0;
    private static final int DETAIL = 2;
    private IgdbGame mIgdbGame;
    private IgdbClientSingleton sIgdbClientSingleton;
    private Context mContext;
    private List<Integer> mListItems;
    private JSONArray mJSONArray;

    public GameDetailsAdapter(IgdbGame game, Context context, JSONArray array, List<Integer> list) {
        mIgdbGame = game;
        mContext = context;
        mJSONArray = array;
        mListItems = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();


        switch (viewType){
            case TOP:
                View viewTop = inflater.inflate(R.layout.list_view_game_detail_header, parent, false);
                viewHolder = new TopHolder(viewTop);
                break;
            case DETAIL:
                View viewDetail = inflater.inflate(R.layout.list_view_game_info_item, parent, false);
                viewHolder = new DetailsHolder(viewDetail);
                break;
            default:
                Log.d("GameDetailHolder", "nothing");
                return null;
        }
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemPosition = holder.getItemViewType();
        final TopHolder topHolder;
        final DetailsHolder detailsHolder;

        switch (itemPosition){
            case TOP:
                topHolder = (TopHolder)holder;
                configureTopHolder(topHolder);
                break;
            case DETAIL:
                detailsHolder = (DetailsHolder)holder;
                configureDetailsHolder(position, detailsHolder);
                break;
        }
    }

    public void configureDetailsHolder(int position, DetailsHolder detailsHolder){
        try {
            JSONObject object = mJSONArray.getJSONObject(position);
            String key = object.keys().next();
            detailsHolder.getTitle().setText(key);
            detailsHolder.getDetails().setText(object.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void configureTopHolder(TopHolder topHolder){
        String protocol = "";
        try{
            protocol = sIgdbClientSingleton.getUrlCoverBig() +
                    mIgdbGame.getIgdbGameCover().getCloudinaryId() + sIgdbClientSingleton.getImageFormatJpg();
        }catch (NullPointerException e){
            Log.d(TAG, e.toString());
        }

        if ( !protocol.equals("")){
            Picasso.with(mContext).
                    load(protocol).
                    error(R.drawable.ic_error).
                    placeholder(R.drawable.ic_img_placeholder).
                    into(topHolder.getGameCover());
        }else{
            topHolder.getGameCover().setImageResource(R.drawable.ic_error);
        }
        topHolder.getGameTitle().setText(mIgdbGame.getName());
        int rat = ((int) mIgdbGame.getAggregated_rating());
        if(rat == 0){
            topHolder.getGameScore().setText("-");
        }else{
            topHolder.getGameScore().setText(rat + "");
        }

    }
    @Override
    public int getItemViewType(int position){
        return mListItems.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TopHolder extends RecyclerView.ViewHolder{
        private TextView mGameTitle;
        private TextView mGameScore;
        private ImageView mGameCover;

        public TopHolder(View itemView) {
            super(itemView);
            mGameCover = (ImageView)itemView.findViewById(R.id.imageView);
            mGameTitle = (TextView)itemView.findViewById(R.id.txt_game_title);
            mGameScore = (TextView)itemView.findViewById(R.id.txt_game_score);
        }

        public TextView getGameTitle() {
            return mGameTitle;
        }

        public void setGameTitle(TextView gameTitle) {
            mGameTitle = gameTitle;
        }

        public TextView getGameScore() {
            return mGameScore;
        }

        public void setGameScore(TextView gameScore) {
            mGameScore = gameScore;
        }

        public ImageView getGameCover() {
            return mGameCover;
        }

        public void setGameCover(ImageView gameCover) {
            mGameCover = gameCover;
        }
    }

    public class DetailsHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDetails;

        public DetailsHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.txt_game_title);
            mDetails = (TextView)itemView.findViewById(R.id.txt_game_detail_content);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public void setTitle(TextView title) {
            mTitle = title;
        }

        public TextView getDetails() {
            return mDetails;
        }

        public void setDetails(TextView details) {
            mDetails = details;
        }
    }

}
