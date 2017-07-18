package gamedb.abelsantos.com.gamedb.IGDB;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abel Cruz dos Santos on 18.07.2017.
 */

public class IgdbGameMode {

    private int mId;
    private String mModeName;
    private List<String> mGameModes;

    public IgdbGameMode() {
    }

    public int getId() {
        return mId;
    }
    @JsonSetter("id")
    public void setId(int id) {
        mId = id;
    }

    public String getModeName() {
        return mModeName;
    }

    public void setModeName(String modeName) {
        mModeName = modeName;
    }

    public String gameModeResolver(int ids){
        String mode = "";
            switch (ids){
                case 1:
                    mode = "Single player";
                    break;
                case 2:
                    mode = "Multiplayer";
                    break;
                case 3:
                    mode = "Co-op";
                    break;
                case 4:
                    mode = "Split screen";
                    break;
                case 5:
                    mode =  "Massively Multiplayer Online (MMO)";
                    break;
                default:
                    break;

            }
            return mode;
    }
}
