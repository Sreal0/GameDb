package gamedb.abelsantos.com.gamedb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Abel Cruz dos Santos on 21.01.2017.
 */

public class SplashScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        Intent intent = new Intent(this, GameDbLauncher.class);
        startActivity(intent);
        finish();
    }
}
