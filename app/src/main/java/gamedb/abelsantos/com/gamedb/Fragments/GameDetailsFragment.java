package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import gamedb.abelsantos.com.gamedb.IGDB.IgdbGame;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 27.06.2017.
 */

public class GameDetailsFragment extends Fragment {
    public static final String TAG = "GameDetailsFragment";
    private static final String ARGS_ID = "id";
    private int mID ;
    private ImageView mGameCover;
    private TextView mGameTitle;
    private TextView mGameScore;
    private TextView mGameDescription;

    public static GameDetailsFragment newInstance(int gameId) {
        GameDetailsFragment fragment = new GameDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, gameId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mID = getArguments().getInt(ARGS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_details, container, false);

        mGameCover = (ImageView)view.findViewById(R.id.imageView);
        mGameTitle = (TextView)view.findViewById(R.id.txt_game_title);
        mGameScore = (TextView)view.findViewById(R.id.txt_game_score);
        mGameDescription =(TextView)view.findViewById(R.id.txt_description);
        mGameTitle.setText(mID + "");
        return view;
    }
}
