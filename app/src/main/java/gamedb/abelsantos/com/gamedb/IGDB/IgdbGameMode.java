package gamedb.abelsantos.com.gamedb.IGDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abel Cruz dos Santos on 18.07.2017.
 */

public class IgdbGameMode {

    private int mId;
    private String mModeName;
    private List<String> mGameModes;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getModeName() {
        return mModeName;
    }

    public void setModeName(String modeName) {
        mModeName = modeName;
    }

    public List<String> gameModeResolver(int[] ids){
        mGameModes = new ArrayList<>();
        for (int id : ids) {
            switch (id){
                case 1:
                    mGameModes.add("Single player");
                    break;
                case 2:
                    mGameModes.add("Multiplayer");
                    break;
                case 3:
                    mGameModes.add("Co-op");
                    break;
                case 4:
                    mGameModes.add("Split screen");
                    break;
                case 5:
                    mGameModes.add("Massively Multiplayer Online (MMO)");
                    break;
                default:
                    break;
            }
        }
        return mGameModes;
    }
}
