package gamedb.abelsantos.com.gamedb.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import gamedb.abelsantos.com.gamedb.R;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Abel Cruz dos Santos on 29.05.2017.
 */

public class MyGamesFragment extends Fragment{
    private static final String TAG = "MyGamesFragment";
    private RecyclerView mRecyclerView;
    private MyGamesAdapter mMyGamesAdapter;

    private List<Game> mGames;
    private ImageView mThumbnail;
    private TextView mGameName;
    private TextView mScore;
    private ImageButton mRemoveGame;

    public static MyGamesFragment newInstance() {
        MyGamesFragment fragment = new MyGamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mygames, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewMyGames);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mGames = ((GameDbLauncher) getActivity()).getMyGames();
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        mMyGamesAdapter = new MyGamesAdapter();
        mRecyclerView.setAdapter(mMyGamesAdapter);
    }

    private class MyGamesHolder extends RecyclerView.ViewHolder{

        public MyGamesHolder(View itemView) {
            super(itemView);
            mGameName = (TextView)itemView.findViewById(R.id.txt_gameTitle);
            mScore = (TextView)itemView.findViewById(R.id.txt_game_rating);
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            mRemoveGame = (ImageButton)itemView.findViewById(R.id.button_remove_game);
        }

        private void bingGameListItem(final Game game){
            mGameName.setText(game.getGameName());
            mScore.setText(game.getAggregated_rating() + "");
            String protocol = "";
            try{
                protocol = "https:" + game.getThumbnailUrl();
                Log.d(TAG, protocol);
            }catch (NullPointerException e){
                Log.d(TAG, e.toString());
            }
            if (protocol != ""){
                Picasso.with(getContext()).
                        load(protocol).
                        error(R.mipmap.ic_launcher).
                        placeholder(R.mipmap.ic_img_placeholder).
                        resize(75, 100).
                        into(mThumbnail);
            }else{
                mThumbnail.setImageResource(R.mipmap.ic_launcher);
            }
            int flag = 0;
            mRemoveGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: add a check to ask if user is sure he wants to remove game
                    //mGames.remove(getAdapterPosition());
                    ((GameDbLauncher) getActivity()).removeGameFromDatabase(game.getId());
                    mGames = ((GameDbLauncher)getActivity()).getMyGames();
                    mMyGamesAdapter.notifyItemRemoved(getAdapterPosition());
                    mMyGamesAdapter.notifyItemRangeChanged(getAdapterPosition(), mGames.size());
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
