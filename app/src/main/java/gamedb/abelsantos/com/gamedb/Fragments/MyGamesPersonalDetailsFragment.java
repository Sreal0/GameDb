package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 21.08.2017.
 */

public class MyGamesPersonalDetailsFragment extends Fragment {
    public static final String TAG = "MyGamesPersonalDetailsFragment";
    private static final String KEY = "id";
    private int mId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_view_private_info, container, false);

        return view;
    }


    public static MyGamesPersonalDetailsFragment newInstance() {
        MyGamesPersonalDetailsFragment fragment = new MyGamesPersonalDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            mId = bundle.getInt(KEY);
        }
    }
}
