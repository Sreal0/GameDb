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

import java.util.List;

import gamedb.abelsantos.com.gamedb.Database.Game;
import gamedb.abelsantos.com.gamedb.IGDB.IgdbClientSingleton;
import gamedb.abelsantos.com.gamedb.R;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Abel Cruz dos Santos on 16.08.2017.
 */

public class MyGameGeneralInfoFragment extends Fragment {
    public static final String TAG = "MyGameGeneralInfoFragment";
    private static final String KEY = "id";
    private final String NO_INFO = "-";
    private int mId;
    //UI components
    private TextView mGameTitle;
    private TextView mGameScore;
    private TextView mReleaseDate;
    private TextView mGenre;
    private TextView mPlatforms;
    private TextView mGameModes;
    private ImageView mGameCover;
    //Database
    private Realm mRealm;
    private Game mGame;
    //Singleton
    private IgdbClientSingleton sIgdbClientSingleton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_view_general_info, container, false);

        //Database
        mRealm = Realm.getDefaultInstance();

        //Singleton
        sIgdbClientSingleton = IgdbClientSingleton.getInstance();

        //Initialise UI
        mGameTitle = (TextView)view.findViewById(R.id.txt_game_title);
        mGameScore = (TextView)view.findViewById(R.id.txt_game_score_general_info);
        mReleaseDate = (TextView)view.findViewById(R.id.txt_release_date);
        mGenre = (TextView)view.findViewById(R.id.txt_genre);
        mPlatforms = (TextView)view.findViewById(R.id.txt_platforms);
        mGameModes = (TextView)view.findViewById(R.id.txt_game_modes);
        mGameCover = (ImageView)view.findViewById(R.id.iv_game_cover);

        //Load game from database
        loadGameFromDatabase();

        //Populate UI
        populateUI();

        return view;
    }
    

    public static MyGameGeneralInfoFragment newInstance() {
        MyGameGeneralInfoFragment fragment = new MyGameGeneralInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            mId = bundle.getInt(KEY);
        }
    }

    private void loadGameFromDatabase(){
        RealmResults<Game> realmResults = mRealm.where(Game.class).equalTo("id", mId).findAll();
        mGame = realmResults.first();
    }

    public void populateUI(){
        if (mGame.getGameName() != null){
            mGameTitle.setText(mGame.getGameName());
        }
        if (mGame.getAggregated_rating() > 0){
            mGameScore.setText( mGame.getAggregated_rating() + "");
        }else {
            mGameScore.setText(NO_INFO);
        }
        if(mGame.getGenre() != null){
            mGenre.setText(mGame.getGenre());
        }else {
            mGenre.setText(NO_INFO);
        }
        if (mGame.getReleasedate() != null){
            mReleaseDate.setText(mGame.getReleasedate());
        }else {
            mReleaseDate.setText(NO_INFO);
        }
        if (mGame.getPlatforms() != null){
            mPlatforms.setText(mGame.getPlatforms());
        }else {
            mPlatforms.setText(NO_INFO);
        }
        if (mGame.getGameModes() != null){
            mGameModes.setText(mGame.getGameModes());
        }else {
            mGameModes.setText(NO_INFO);
        }

        //Load game cover
        loadGameCover();
    }

    private void loadGameCover(){
        String protocol = "";
        try{
            protocol = sIgdbClientSingleton.getUrlCoverBig() +
                    mGame.getCloudinaryId() + sIgdbClientSingleton.getImageFormatJpg();
        }catch (NullPointerException e){
            Log.d("GameGeneralInfoFragment", e.toString());
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
    }

}
