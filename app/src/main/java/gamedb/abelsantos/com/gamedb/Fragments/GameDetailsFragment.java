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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
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

    private int mID ;
    private ImageView mGameCover;
    private TextView mGameTitle;
    private TextView mGameScore;
    private TextView mGameDeveloper;
    private TextView mGamePublisher;
    private TextView mGameGenre;
    private TextView mGameSummary;
    private IgdbClientSingleton sIgdbClientSingleton;
    private IgdbGame mIgdbGame;
    private List<String> mCompanies;
    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mRecyclerView;
    private GameDetailsAdapter mGameDetailsAdapter;
    private List<Integer> mItemsList = new ArrayList<>();
    private JSONArray mJSONArray;

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

        populateList();
        initialiseAdapter();
        return view;
    }

    private void populateList(){
        mItemsList = new ArrayList<>();
        mJSONArray = ((GameDbLauncher)getActivity()).getJSONArraySingleGame();
        mItemsList.add(TOP);
        for (int i = 0; i < mJSONArray.length(); i++){
            mItemsList.add(DETAIL);
        }
    }

    private void initialiseAdapter(){
        mGameDetailsAdapter = new GameDetailsAdapter(mIgdbGame, getContext(), mJSONArray, mItemsList);
        mRecyclerView.setAdapter(mGameDetailsAdapter);
        mGameDetailsAdapter.notifyDataSetChanged();
    }
    
}
