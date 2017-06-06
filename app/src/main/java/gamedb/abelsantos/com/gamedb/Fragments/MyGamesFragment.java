package gamedb.abelsantos.com.gamedb.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.R;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Abel Cruz dos Santos on 29.05.2017.
 */

public class MyGamesFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private MyGamesAdapter mMyGamesAdapter;

    private RealmResults<Game> mGames;
    private ImageView mThumbnail;
    private TextView mGameName;
    private TextView mScore;
    private ProgressDialog mProgressDialog;


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

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        mGames = ((GameDbLauncher) getActivity()).getMyGames();
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        mMyGamesAdapter = new MyGamesAdapter();
        mRecyclerView.setAdapter(mMyGamesAdapter);
        mProgressDialog.hide();
    }

    private class MyGamesHolder extends RecyclerView.ViewHolder{

        public MyGamesHolder(View itemView) {
            super(itemView);
            mGameName = (TextView)itemView.findViewById(R.id.txt_gameTitle);
            mScore = (TextView)itemView.findViewById(R.id.txt_game_rating);
        }

        private void bingGameListItem(final Game game){
            mGameName.setText(game.getGameName());
            mScore.setText(game.getRating() + "");
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
