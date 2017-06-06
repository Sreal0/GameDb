package gamedb.abelsantos.com.gamedb.Database;

import io.realm.RealmObject;

/**
 * Created by Abel Cruz dos Santos on 06.06.2017.
 */

public class RealmString extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
