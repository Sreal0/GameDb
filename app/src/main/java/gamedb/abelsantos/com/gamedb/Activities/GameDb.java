package gamedb.abelsantos.com.gamedb.Activities;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Abel Cruz dos Santos on 21.01.2017.
 */

public class GameDb extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        //Db initialisation
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
