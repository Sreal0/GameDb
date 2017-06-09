package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 09.05.2017.
 */

public class WishlistFragment extends Fragment {
    public static final String TAG = "WishlistFragment";

    public static WishlistFragment newInstance() {
        WishlistFragment fragment = new WishlistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Fragment started");
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

}
