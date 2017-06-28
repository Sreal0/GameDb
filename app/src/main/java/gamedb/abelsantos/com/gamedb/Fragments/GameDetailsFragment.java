package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
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
    private TextView mGameDeveloper;
    private TextView mGamePublisher;
    private TextView mGameDescription;
    private IgdbClientSingleton sIgdbClientSingleton;

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

        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        mGameDeveloper = (TextView)view.findViewById(R.id.txt_developer);
        mGamePublisher = (TextView)view.findViewById(R.id.txt_publisher);
        mGameCover = (ImageView)view.findViewById(R.id.imageView);
        mGameTitle = (TextView)view.findViewById(R.id.txt_game_title);
        mGameScore = (TextView)view.findViewById(R.id.txt_game_score);

        //mGameDescription =(TextView)view.findViewById(R.id.txt_description);
        //gets the request from the singleton...
        String url = sIgdbClientSingleton.getSingleGameDetails(mID);
        //gets the game object from the API. GameDbLauncher will call showGameDetails at the end
        ((GameDbLauncher)getActivity()).getASingleGameFromAPI(url);
        ((GameDbLauncher)getActivity()).changeToolbarSubtitleText("");
        return view;
    }

    public void showGameDetails(final IgdbGame game){
        mGameTitle.setText(game.getName());
        int rat = ((int) game.getAggregated_rating());
        if(rat == 0){
            mGameScore.setText("-");
        }else{
            mGameScore.setText(rat + "");
        }
        String protocol = "";
        try{
            protocol = sIgdbClientSingleton.getUrlCoverBig() +
                    game.getIgdbGameCover().getCloudinaryId() + sIgdbClientSingleton.getImageFormatJpg();
        }catch (NullPointerException e){
            Log.d(TAG, e.toString());
        }
        if ( !protocol.equals("")){
            Picasso.with(getContext()).
                    load(protocol).
                    error(R.drawable.ic_error).
                    placeholder(R.drawable.ic_img_placeholder).
                    into(mGameCover);
            mGameCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GameDbLauncher)getActivity()).onClickThumbnail( game.getIgdbGameCover().getCloudinaryId());
                }
            });
        }else{
            mGameCover.setImageResource(R.drawable.ic_error);
        }

    }
}
