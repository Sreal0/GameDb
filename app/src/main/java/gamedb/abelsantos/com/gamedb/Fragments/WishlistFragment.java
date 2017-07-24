package gamedb.abelsantos.com.gamedb.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.R;
import gamedb.abelsantos.com.gamedb.RecyclerView.WishlistAdapter;

/**
 * Created by Abel Cruz dos Santos on 09.05.2017.
 */

public class WishlistFragment extends Fragment {
    public static final String TAG = "WishlistFragment";
    private static final int TAG_DATABASE = 0;
    private static final int TAG_WISHLIST = 1;

    private RecyclerView mRecyclerView;
    private CoordinatorLayout mCoordinatorLayout;
    private List<Game> mGames;
    private WishlistAdapter mWishlistAdapter;

    public static WishlistFragment newInstance() {
        WishlistFragment fragment = new WishlistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        mCoordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout_wishlist);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewWishlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText("");
        mGames = ((GameDbLauncher) getActivity()).getGamesFromRealm(TAG_WISHLIST);
        initialiseAdapter();

        return view;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public void initialiseAdapter(){
        mWishlistAdapter = new WishlistAdapter(mGames, getContext());
        mRecyclerView.setAdapter(mWishlistAdapter);
        mWishlistAdapter.notifyDataSetChanged();
    }

    public void showAlertDialog(final long id){
        String[] items = {getString(R.string.text_move_game_to_database), getString(R.string.text_remove_game_from_wishlist)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.add_game_alert_dialog_title));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    ((GameDbLauncher)getActivity()).saveGamesToRealm(id, i);
                    mGames = ((GameDbLauncher)getActivity()).getGamesFromRealm(TAG_WISHLIST);
                    initialiseAdapter();
                    dialogInterface.dismiss();
                }//Removes game from wishlist
                else{
                    ((GameDbLauncher)getActivity()).removeGameFromDatabase(id);
                    mGames = ((GameDbLauncher)getActivity()).getGamesFromRealm(TAG_WISHLIST);
                    mWishlistAdapter.notifyDataSetChanged();
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
