package gamedb.abelsantos.com.gamedb.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Abel Cruz dos Santos on 21.01.2017.
 */

public class SplashScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        //Database initialisation
        Realm.init(getApplicationContext());
        //Attention: the old constructor RealmConfiguration.Builder(this) no longer exists
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Intent intent = new Intent(this, GameDbLauncher.class);
        startActivity(intent);
        finish();
    }
}
