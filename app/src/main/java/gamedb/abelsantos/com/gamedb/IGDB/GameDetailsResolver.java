package gamedb.abelsantos.com.gamedb.IGDB;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;
import gamedb.abelsantos.com.gamedb.Database.GameDetailsPair;

/**
 * Created by Abel Cruz dos Santos on 19.07.2017.
 */

public class GameDetailsResolver {
    private static final int TOP = 0;
    private static final int DETAIL = 2;
    private static final int WEBSITE = 4;
    private final String NOT_AVALIABLE = "N/A";

    private IgdbGame mIgdbGame;
    private List<GameDetailsPair> mGameDetailsPairs = new ArrayList<>();
    private List<Integer> mViewTypeItems = new ArrayList<>();
    private Context mContext;
    private List<String> mCompanies = new ArrayList<>();


    public GameDetailsResolver(IgdbGame game, Context context){
        mIgdbGame = game;
        mContext = context;
    }

    public void setIgdbGame(IgdbGame igdbGame) {
        mIgdbGame = igdbGame;
    }

    //FIXME -> if a dataset is avaliable, add it to the list. Else, don´t
    public List<GameDetailsPair> resolveGameDetails(){
        GameDetailsPair detailsPair = new GameDetailsPair();
        if (mIgdbGame != null){
            //Top header
            detailsPair.put("TOP", "Top header");
            mGameDetailsPairs.add(detailsPair);
            mViewTypeItems.add(TOP);
            //Genre -> using mContext to get a access to GameDbLauncher Instance
            if(mIgdbGame.getGenre() != null){
                detailsPair = new GameDetailsPair();
                String genres = ((GameDbLauncher)mContext).resolveGenreNames(mIgdbGame.getGenre());
                detailsPair.put("Genre", genres);
                mGameDetailsPairs.add(detailsPair);
                mViewTypeItems.add(DETAIL);
            }
            //GameMode
            if(mIgdbGame.getIgdbGameModes() != null){
                detailsPair = new GameDetailsPair();
                List<String>  igdbGameModes = mIgdbGame.getIgdbGameModes();
                String modes = igdbGameModes.toString();
                String cleanedUp = modes.replace("[", "");
                cleanedUp = cleanedUp.replace("]", "");
                detailsPair.put("Game modes", cleanedUp);
                mGameDetailsPairs.add(detailsPair);
                mViewTypeItems.add(DETAIL);
            }
            //Release date
            if(mIgdbGame.getReleaseDate() > 0){
                detailsPair = new GameDetailsPair();
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(mIgdbGame.getReleaseDate()));
                detailsPair.put("Release date", date);
                mGameDetailsPairs.add(detailsPair);
                mViewTypeItems.add(DETAIL);
            }
            //Developers - If there is no dev or pub it won´t show anything
            mCompanies = ((GameDbLauncher)mContext).getCompanies();
            if (mCompanies.size() > 0){
                detailsPair = new GameDetailsPair();
                detailsPair.put("Developer", mCompanies.get(0));
                mGameDetailsPairs.add(detailsPair);
                mViewTypeItems.add(DETAIL);
            }
            //Publisher - if there is no dev or pub it won´t show anything
            if (mCompanies.size() > 0){
                detailsPair = new GameDetailsPair();
                detailsPair.put("Publisher", mCompanies.get(1));
                mGameDetailsPairs.add(detailsPair);
                mViewTypeItems.add(DETAIL);
            }
            //Summary
            if(mIgdbGame.getSummary() != null){
                detailsPair = new GameDetailsPair();
                detailsPair.put("Summary", mIgdbGame.getSummary());
                mGameDetailsPairs.add(detailsPair);
                mViewTypeItems.add(DETAIL);
            }
            //Websites
            if (mIgdbGame.getIgbdWebsites() != null){
                for (int i = 0; i < mIgdbGame.getIgbdWebsites().length; i++){
                    detailsPair = new GameDetailsPair();
                    IgdbWebsite website = mIgdbGame.getIgbdWebsites()[i];
                    //Website object resolves its own category name and gives an url too.
                    detailsPair.put(website.resolveWebsiteCategoryToName(website.getCategory()), website.getUrl());
                    mGameDetailsPairs.add(detailsPair);
                    mViewTypeItems.add(WEBSITE);
                }
            }
        }
        return mGameDetailsPairs;
    }

    public List<Integer> getViewTypeItems() {
        return mViewTypeItems;
    }
}
