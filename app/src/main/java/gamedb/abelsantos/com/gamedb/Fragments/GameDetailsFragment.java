package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

/**
 * Created by Abel Cruz dos Santos on 27.06.2017.
 */

public class GameDetailsFragment extends Fragment {
    public static final String TAG = "GameDetailsFragment";
    private static final String ARGS_GAME = "game";
    private static final String ARGS_COMPANIES = "companies";
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

        sIgdbClientSingleton = IgdbClientSingleton.getInstance();
        mCoordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinator_layout_games_detail_fragment);
        mGameDeveloper = (TextView)view.findViewById(R.id.txt_developer);
        mGamePublisher = (TextView)view.findViewById(R.id.txt_game_publisher);
        mGameCover = (ImageView)view.findViewById(R.id.imageView);
        mGameTitle = (TextView)view.findViewById(R.id.txt_game_title);
        mGameScore = (TextView)view.findViewById(R.id.txt_game_score);
        mGameGenre = (TextView)view.findViewById(R.id.txt_game_genre);
        mGameSummary =(TextView)view.findViewById(R.id.txt_summary);
        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText("");
        mIgdbGame = ((GameDbLauncher)getActivity()).getIgdbGame();
        mCompanies = ((GameDbLauncher)getActivity()).getCompanies();

        showGameDetails();
        return view;
    }

    public void showGameDetails(){
        mGameTitle.setText(mIgdbGame.getName());
        int rat = ((int) mIgdbGame.getAggregated_rating());
        if(rat == 0){
            mGameScore.setText("-");
        }else{
            mGameScore.setText(rat + "");
        }
        String protocol = "";
        try{
            protocol = sIgdbClientSingleton.getUrlCoverBig() +
                    mIgdbGame.getIgdbGameCover().getCloudinaryId() + sIgdbClientSingleton.getImageFormatJpg();
        }catch (NullPointerException e){
            Log.d(TAG, e.toString());
        }
        if ( !protocol.equals("")){
            Picasso.with(getContext()).
                    load(protocol).
                    error(R.drawable.ic_error).
                    placeholder(R.drawable.ic_img_placeholder).
                    into(mGameCover);
        }else{
            mGameCover.setImageResource(R.drawable.ic_error);
        }
        if(mIgdbGame.getSummary() != null){
            mGameSummary.append(" " + mIgdbGame.getSummary());
        }
        else{
            mGameSummary.append(" N/A");
        }
        mGameGenre.setText(((GameDbLauncher)getActivity()).resolveGenreNames(mIgdbGame.getGenre()));
        mGameDeveloper.setText(mCompanies.get(0));
        mGamePublisher.setText(mCompanies.get(1));
    }
}
