package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.GameDetailsPair;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbCompany;
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

    private IgdbClientSingleton sIgdbClientSingleton;
    private IgdbGame mIgdbGame;
    private List<String> mCompanies;
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private GameDetailsAdapter mGameDetailsAdapter;
    private List<Integer> mItemsList = new ArrayList<>();
    private List<GameDetailsPair> mGameDetailsPairs;

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

        resolveGame();

        return view;
    }


    private void resolveGame(){
        GameDetailsPair pair = new GameDetailsPair();
        mItemsList = new ArrayList<>();
        mItemsList.add(TOP);

        if (mIgdbGame != null){
            pair.setTitle("NotUsable");
            pair.setDetail("Something went wrong if this is seen");
            mGameDetailsPairs.add(pair);
            pair = new GameDetailsPair();
            pair.setTitle("Genre");
            pair.setDetail(((GameDbLauncher)getActivity()).resolveGenreNames(mIgdbGame.getGenre()));
            mItemsList.add(DETAIL);
            mGameDetailsPairs.add(pair);
            pair = new GameDetailsPair();
            pair.setTitle("Release date");
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(mIgdbGame.getReleaseDate()));
            pair.setDetail(date);
            mItemsList.add(DETAIL);
            mGameDetailsPairs.add(pair);
            pair = new GameDetailsPair();
            pair.setTitle("Developer");
            pair.setDetail(mCompanies.get(0));
            mItemsList.add(DETAIL);
            mGameDetailsPairs.add(pair);
            pair = new GameDetailsPair();
            pair.setTitle("Publisher");
            pair.setDetail(mCompanies.get(1));
            mItemsList.add(DETAIL);
            mGameDetailsPairs.add(pair);
            pair = new GameDetailsPair();
            pair.setTitle("Summary");
            pair.setDetail(mIgdbGame.getSummary());
            mItemsList.add(DETAIL);
            mGameDetailsPairs.add(pair);
            pair = new GameDetailsPair();
            Log.d("Sizes", mItemsList.size() + " " + mGameDetailsPairs.size() + "");
        }
        initialiseAdapter();
    }

    private void initialiseAdapter(){
        mGameDetailsAdapter = new GameDetailsAdapter(mIgdbGame, getContext(), mItemsList, mGameDetailsPairs);
        mRecyclerView.setAdapter(mGameDetailsAdapter);
        mGameDetailsAdapter.notifyDataSetChanged();
    }
}
