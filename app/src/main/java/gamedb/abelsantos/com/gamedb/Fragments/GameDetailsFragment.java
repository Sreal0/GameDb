package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.GameDetailsPair;
import gamedb.abelsantos.com.gamedb.IGDB.Resolver.GameDetailsResolver;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;
import gamedb.abelsantos.com.gamedb.RecyclerView.GameDetailsAdapter;

/**
 * Created by Abel Cruz dos Santos on 27.06.2017.
 */

public class GameDetailsFragment extends Fragment {
    public static final String TAG = "GameDetailsFragment";
    private static final String ARGS_GAME = "game";
    private static final String ARGS_COMPANIES = "companies";
    private static final int TOP = 0;
    private static final int DETAIL = 2;
    private static final int WEBSITE = 4;

    private IgdbClientSingleton sIgdbClientSingleton;
    private IgdbGame mIgdbGame;
    private List<String> mCompanies;
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private GameDetailsAdapter mGameDetailsAdapter;
    private List<Integer> mItemsList = new ArrayList<>();
    private List<GameDetailsPair> mGameDetailsPairs;
    private List<GameDetailsPair> gameDetails;
    private GameDetailsResolver mGameDetailsResolver;

    public static GameDetailsFragment newInstance() {
        GameDetailsFragment fragment = new GameDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_details, container, false);

        //RV
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewGameDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        sIgdbClientSingleton = IgdbClientSingleton.getInstance();
        mCoordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout_games_detail_fragment);
        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText("");
        mIgdbGame = ((GameDbLauncher)getActivity()).getIgdbGame();
        mCompanies = ((GameDbLauncher)getActivity()).getCompanies();
        mGameDetailsPairs = new ArrayList<>();

        mGameDetailsResolver = new GameDetailsResolver(mIgdbGame, getContext());
        gameDetails = mGameDetailsResolver.resolveGameDetails();
        mItemsList = mGameDetailsResolver.getViewTypeItems();
        initialiseAdapter();
        return view;
    }

    private void initialiseAdapter(){
        mGameDetailsAdapter = new GameDetailsAdapter(mIgdbGame, getContext(), gameDetails, mItemsList);
        mRecyclerView.setAdapter(mGameDetailsAdapter);
        mGameDetailsAdapter.notifyDataSetChanged();
    }
}
