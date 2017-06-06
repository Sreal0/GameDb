package gamedb.abelsantos.com.gamedb.Database;

import io.realm.RealmObject;

/**
 * Created by Abel Cruz dos Santos on 06.06.2017.
 */

public class RealmInteger extends RealmObject {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
