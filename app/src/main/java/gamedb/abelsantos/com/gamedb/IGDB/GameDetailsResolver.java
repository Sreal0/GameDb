package gamedb.abelsantos.com.gamedb.IGDB;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Activities.GameDbLauncher;

/**
 * Created by Abel Cruz dos Santos on 19.07.2017.
 */

public class GameDetailsResolver {
    private static final int TOP = 0;
    private static final int DETAIL = 2;
    private static final int WEBSITE = 4;
    private final String NOT_AVALIABLE = "N/A";

    private IgdbGame mIgdbGame;
    private HashMap<String, String> gameDetails = new HashMap<>();
    private List<Integer> mViewTypeItems = new ArrayList<>();
    private Context mContext;
    private List<String> mCompanies = new ArrayList<>();


    public GameDetailsResolver(IgdbGame game, Context context){
        mIgdbGame = game;
        mContext = context;
    }

    public IgdbGame getIgdbGame() {
        return mIgdbGame;
    }

    public void setIgdbGame(IgdbGame igdbGame) {
        mIgdbGame = igdbGame;
    }

    public HashMap<String, String> getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(HashMap<String, String> gameDetails) {
        this.gameDetails = gameDetails;
    }

    public void resolveGameDetails(){
        if (mIgdbGame != null){
            //Top header
            gameDetails.put("TOP", "Top header");
            //Genre -> using mContext to get a access to GameDbLauncher Instance
            gameDetails.put("Genre", ((GameDbLauncher)mContext).resolveGenreNames(mIgdbGame.getGenre()));
            //GameMode
            List<String>  igdbGameModes = mIgdbGame.getIgdbGameModes();
            String modes = igdbGameModes.toString();
            String cleanedUp = modes.replace("[", "");
            cleanedUp = cleanedUp.replace("]", "");
            gameDetails.put("Game modes", cleanedUp);
            //Release date
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(mIgdbGame.getReleaseDate()));
            gameDetails.put("Release date", date);
            //Developers
            mCompanies = ((GameDbLauncher)mContext).getCompanies();
            if (mCompanies.size() > 0){
                gameDetails.put("Developer", mCompanies.get(0));
            }else{
                gameDetails.put("Developer", NOT_AVALIABLE);
            }
            //Publisher
            if (mCompanies.size() > 0){
                gameDetails.put("Publisher", mCompanies.get(1));
            }else{
                gameDetails.put("Publisher", NOT_AVALIABLE);
            }
            //Summary
            gameDetails.put("Summary", mIgdbGame.getSummary());
            //Websites
            if (mIgdbGame.getIgbdWebsites() != null){
                for (int i = 0; i < mIgdbGame.getIgbdWebsites().length; i++){
                    IgdbWebsite website = mIgdbGame.getIgbdWebsites()[i];
                    //Website object resolves its own category name and gives an url too.
                    gameDetails.put(website.resolveWebsiteCategoryToName(website.getCategory()), website.getUrl());
                }
            }

        }
    }

}
