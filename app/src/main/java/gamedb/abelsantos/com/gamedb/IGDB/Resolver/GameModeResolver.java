package gamedb.abelsantos.com.gamedb.IGDB.Resolver;

/**
 * Created by Abel Cruz dos Santos on 21.08.2017.
 */

public class GameModeResolver {
    private int mId;
    private String mName;

    public GameModeResolver() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String resolveGameMode(int mId) {

        switch (mId) {
            default:
                mName =  "Single player";
                break;
            case 2:
                mName =  "Multiplayer";
            break;
            case 3:
                mName =  "Co-operative";
            break;
            case 4:
                mName =  "Split screen";
            break;
            case 5:
                mName =  "Massive Multiplayer Online (MMO)";
            break;
        }
        return mName;
    }
}
