package gamedb.abelsantos.com.gamedb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 20.03.2017.
 */

public class MyGamesFragment extends Fragment {

    private static final String TAG = "MyGamesFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Fragment started");
        return inflater.inflate(R.layout.fragment_my_games, container, false);
    }
}
