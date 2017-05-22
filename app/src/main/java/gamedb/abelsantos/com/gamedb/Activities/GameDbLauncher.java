package gamedb.abelsantos.com.gamedb.Activities;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Database.GameListObject;
import gamedb.abelsantos.com.gamedb.R;

public class GameDbLauncher extends AppCompatActivity {

    private static String TAG = "gamedb.abelsantos";

    private TextView mGameTitle;
    private TextView mGameRating;

    private List<GameListObject> mItems = new ArrayList<>();

    private Toolbar mToolbar;
    private TextView mToolbarText;
    private RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        //Setting up NavMenu

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewLauncher);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //Navigation Bar onClickListener
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Toast.makeText(getApplicationContext(), "My Games", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_database:
                        Toast.makeText(getApplicationContext(), "Games", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_wishlist:
                        Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                        Toast.makeText(getApplicationContext(), "Wish list", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        setupAdapter();
    }

    private void setupAdapter(){
        mRecyclerView.setAdapter(new GameListAdapter(mItems));
    }


    private class GameHolder extends RecyclerView.ViewHolder{

        public GameHolder(View itemView) {
            super(itemView);
            mGameRating = (TextView)itemView.findViewById(R.id.txt_game_rating);
            mGameTitle =  (TextView) itemView.findViewById(R.id.txt_gameTitle);
        }

        public void bindGamelListItem(GameListObject gameListObject){
            mGameTitle.setText(gameListObject.getGame());
            mGameRating.setText(gameListObject.getRating() + "");
        }
    }

    private class GameListAdapter extends RecyclerView.Adapter<GameHolder>{

        private List<GameListObject> mGameListObjects;

        public GameListAdapter(List<GameListObject> gameListObjects){
            mGameListObjects = gameListObjects;
        }
        @Override
        public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.list_view_game, parent, false);
            return new GameHolder(view);
        }

        @Override
        public void onBindViewHolder(GameHolder holder, int position) {
            GameListObject gameListObject = mGameListObjects.get(position);
            holder.bindGamelListItem(gameListObject);

        }

        @Override
        public int getItemCount() {
            return mGameListObjects.size();
        }
    }

}
