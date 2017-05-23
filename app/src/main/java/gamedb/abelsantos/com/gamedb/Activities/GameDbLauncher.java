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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.IGDB.IgdbClient;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

public class GameDbLauncher extends AppCompatActivity {

    private static String TAG = "gamedb.abelsantos";


    private TextView mGameTitle;
    private TextView mGameRating;
    private ImageView mGameCover;
    private TextView mGameDeveloper;
    private TextView mGameReleaseDate;
    private TextView mGamePlatforms;

    private List<IgdbGame> mItems = new ArrayList<>();

    private Toolbar mToolbar;
    private TextView mToolbarText;
    private RecyclerView mRecyclerView;
    private GameListAdapter mGameListAdapter;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        //Setting up NavMenu

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewLauncher);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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

        IgdbClient igdbClient = IgdbClient.getInstance();
        mItems = igdbClient.getGames();
        Toast.makeText(this, mItems.size() + "size", Toast.LENGTH_SHORT).show();

        setupAdapter();

    }

    private void setupAdapter(){
        mGameListAdapter = new GameListAdapter(mItems);
        mRecyclerView.setAdapter(mGameListAdapter);
        mGameListAdapter.notifyDataSetChanged();
    }


    private class GameHolder extends RecyclerView.ViewHolder{

        public GameHolder(View itemView) {
            super(itemView);
            mGameRating = (TextView)itemView.findViewById(R.id.txt_game_rating);
            mGameTitle =  (TextView) itemView.findViewById(R.id.txt_gameTitle);
            mGameCover = (ImageView)itemView.findViewById(R.id.img_thumbnail_game);
            mGameDeveloper = (TextView)itemView.findViewById(R.id.txt_developer);
            mGamePlatforms = (TextView)itemView.findViewById(R.id.txt_game_platform);
            mGameReleaseDate = (TextView)itemView.findViewById(R.id.txt_game_release_date);
        }

        public void bindGameListItem(IgdbGame igdbGame){
            mGameTitle.setText(igdbGame.getName());
            String rat = igdbGame.getRating() + "";
            mGameRating.setText(rat);
            String date = igdbGame.getReleaseDate() + "";
            mGameReleaseDate.setText(date);
        }
    }

    private class GameListAdapter extends RecyclerView.Adapter<GameHolder>{

        private List<IgdbGame> mIgdbGames;

        public GameListAdapter(List<IgdbGame> igdbGames){
            mIgdbGames = igdbGames;
        }
        @Override
        public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplication());
            View view = layoutInflater.inflate(R.layout.list_view_game, parent, false);
            return new GameHolder(view);
        }

        @Override
        public void onBindViewHolder(GameHolder holder, int position) {
            IgdbGame igdbGame = mIgdbGames.get(position);
            holder.bindGameListItem(igdbGame);

        }

        @Override
        public int getItemCount() {
            return mIgdbGames.size();
        }
    }

}
