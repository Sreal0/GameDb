package gamedb.abelsantos.com.gamedb.activities;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Database.GameListObject;
import gamedb.abelsantos.com.gamedb.R;
import gamedb.abelsantos.com.gamedb.fragments.AboutFragment;
import gamedb.abelsantos.com.gamedb.fragments.MyGamesFragment;
import gamedb.abelsantos.com.gamedb.fragments.PreferencesFragment;
import gamedb.abelsantos.com.gamedb.fragments.SearchFragment;
import gamedb.abelsantos.com.gamedb.helper.GameFetcher;

public class GameDbLauncher extends AppCompatActivity {

    private static String TAG = "gamedb.abelsantos";

    ListView mDrawerList;
    LinearLayout mDrawerPane;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayout;
    private TextView mGameTitle;
    private TextView mGameRating;

    private List<NavItem> mNavItems = new ArrayList<>();
    private List<GameListObject> mItems = new ArrayList<>();

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_db_launcher);
        //Setting up NavMenu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavItems.add(new NavItem("My Games", "Personal Game Collection", R.drawable.app_icon));
        mNavItems.add(new NavItem("Search", "Search for games", R.drawable.app_icon));
        mNavItems.add(new NavItem("Preferences", "Change Settings", R.drawable.app_icon));
        mNavItems.add(new NavItem("About", "Get to know the Dev", R.drawable.app_icon));

        linearLayout = (LinearLayout)findViewById(R.id.drawerPane);

        //DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        //Populate the Navigation Drawer with Options
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Log.d(TAG, "Called onItemClicked");
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_closed){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //TODO: add recyclerView to layout
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void selectItemFromDrawer(int position) {
        Log.d(TAG, "Fragment started from Activity");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (position){
            case 0:
                Fragment myGamesFragment = new MyGamesFragment();
                transaction.replace(R.id.mainContent, myGamesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 1:
                Fragment searchFragment = new SearchFragment();
                transaction.replace(R.id.mainContent, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 2:
                Fragment preferencesFragment = new PreferencesFragment();
                transaction.replace(R.id.mainContent, preferencesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 3:
                Fragment aboutFragment = new AboutFragment();
                transaction.replace(R.id.mainContent, aboutFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            default:
                break;
        }



        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        //Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    // Called when invalidateOptionsMenu() is invoked
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(linearLayout);
        //menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class NavItem{
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    //RecyclerView and FetchItemTask

    private void setupAdapter(){

    }
    private class FetchItemsTask extends AsyncTask<Void, Void, List<GameListObject>>{

        @Override
        protected List<GameListObject> doInBackground(Void... voids) {
            return new GameFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GameListObject> gameListObjects) {
            mItems = gameListObjects;
        }
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


    //Slide Menu
    private class DrawerListAdapter extends BaseAdapter{
        Context mContext;
        List<NavItem> mNavItems;

        public DrawerListAdapter(Context context, List<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mNavItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1;

            if (view == null){
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }else{
                view1 = view;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(i).mTitle );
            subtitleView.setText( mNavItems.get(i).mSubtitle );
            iconView.setImageResource(mNavItems.get(i).mIcon);

            return view;
        }
    }
}
