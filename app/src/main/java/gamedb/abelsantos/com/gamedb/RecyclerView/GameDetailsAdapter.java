package gamedb.abelsantos.com.gamedb.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import gamedb.abelsantos.com.gamedb.Database.GameDetailsPair;
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
    private static final int WEBSITE = 4;
    private IgdbGame mIgdbGame;
    private IgdbClientSingleton sIgdbClientSingleton;
    private Context mContext;
    private List<Integer> mListItems;
    private List<GameDetailsPair> mGameDetailsPairs;

    public GameDetailsAdapter(IgdbGame game, Context context, List<GameDetailsPair> gameDetails, List<Integer> listItems) {
        mIgdbGame = game;
        mContext = context;
        mListItems = listItems;
        mGameDetailsPairs = gameDetails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();
        switch (viewType){
            case TOP:
                View viewTop = inflater.inflate(R.layout.list_view_game_detail_header, parent, false);
                viewHolder = new TopHolder(viewTop);
                Log.d("Top", "Top");
                break;
            case DETAIL:
                View viewDetail = inflater.inflate(R.layout.list_view_game_info_item, parent, false);
                viewHolder = new DetailsHolder(viewDetail);
                Log.d("Detail", "detail");
                break;
            case WEBSITE:
                View viewWebsite = inflater.inflate(R.layout.list_view_game_info_web_links, parent, false);
                viewHolder = new WebHolder(viewWebsite);
                Log.d("Web", "Web");
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
        final WebHolder webHolder;

        switch (itemPosition){
            case TOP:
                topHolder = (TopHolder)holder;
                configureTopHolder(topHolder);
                break;
            case DETAIL:
                detailsHolder = (DetailsHolder)holder;
                configureDetailsHolder(position, detailsHolder);
                break;
            case WEBSITE:
                webHolder = (WebHolder)holder;
                configureWebHolder(position, webHolder);

        }
    }

    private void configureWebHolder(final int position, WebHolder webHolder){
        webHolder.getLink().setText(mGameDetailsPairs.get(position).getKey());
        webHolder.getLink().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                if (!mGameDetailsPairs.get(position).getValue().startsWith("http://") &&
                        !mGameDetailsPairs.get(position).getValue().startsWith("https://") ){
                    url = "http://" + mGameDetailsPairs.get(position).getValue();
                }else {
                    url = mGameDetailsPairs.get(position).getValue();
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });
    }

    private void configureDetailsHolder(int position, DetailsHolder detailsHolder){
        Log.d(TAG, "Position confi detal " + position + "");
        detailsHolder.getTitle().setText(mGameDetailsPairs.get(position).getKey());
        detailsHolder.getDetails().setText(mGameDetailsPairs.get(position).getValue());
    }

    private void configureTopHolder(TopHolder topHolder){
        //Search for detv
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
            topHolder.getGameScore().setText("N/A");
        }else{
            //Changes text color based on score.
            if(rat < 50){
                topHolder.getGameScore().setTextColor(ContextCompat.getColor(mContext, R.color.color_review_5));
            }else if(rat < 65){
                topHolder.getGameScore().setTextColor(ContextCompat.getColor(mContext, R.color.color_review_4));
            }else if(rat < 80){
                topHolder.getGameScore().setTextColor(ContextCompat.getColor(mContext, R.color.color_review_3));
            }else if(rat < 91){
                topHolder.getGameScore().setTextColor(ContextCompat.getColor(mContext, R.color.color_review_2));
            }else{
                topHolder.getGameScore().setTextColor(ContextCompat.getColor(mContext, R.color.color_review_1));
            }
            topHolder.getGameScore().setText(String.valueOf(rat));
        }
    }

    @Override
    public int getItemViewType(int position){
        return mListItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    private class TopHolder extends RecyclerView.ViewHolder{
        private TextView mGameTitle;
        private TextView mGameScore;
        private ImageView mGameCover;
        private TextView mGameScoreTitle;
        //private TextView mDev;

        private TopHolder(View itemView) {
            super(itemView);
            mGameCover = (ImageView)itemView.findViewById(R.id.iv_game_cover);
            mGameTitle = (TextView)itemView.findViewById(R.id.txt_game_title);
            mGameScore = (TextView)itemView.findViewById(R.id.txt_game_score);
            mGameScoreTitle = (TextView)itemView.findViewById(R.id.txt_score_title);
            //mDev = (TextView)itemView.findViewById(R.id.txt_developer);
        }

        private TextView getGameTitle() {
            return mGameTitle;
        }

        private TextView getGameScore() {
            return mGameScore;
        }

        private ImageView getGameCover() {
            return mGameCover;
        }

    }

    public class DetailsHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDetails;

        public DetailsHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.txt_game_detail_title);
            mDetails = (TextView)itemView.findViewById(R.id.txt_game_detail_content);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getDetails() {
            return mDetails;
        }

    }

    public class WebHolder extends RecyclerView.ViewHolder{
        private TextView mLink;

        public WebHolder(View itemView){
            super(itemView);
            mLink = (TextView)itemView.findViewById(R.id.txt_link);
        }

        public TextView getLink() {
            return mLink;
        }

        public void setLink(TextView link) {
            mLink = link;
        }
    }

}
