package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gamedb.abelsantos.com.gamedb.Fragments.ViewPagerAdapter.ViewPagerAdapter;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 21.08.2017.
 */

public class BaseFragmentMyGames extends Fragment {
    public static final String TAG = "BaseFragmentMyGames";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_my_games, container, false);

        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        setupViewAdapter(mViewPager);

        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewAdapter(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        Fragment gamesGeneralInfo = supportFragmentManager.findFragmentByTag(MyGameGeneralInfoFragment.TAG);

        adapter.addFrag(new MyGameGeneralInfoFragment(), "General");
        adapter.addFrag(new MyGamesPersonalDetailsFragment(), "Personal");
        viewPager.setAdapter(adapter);
    }

    public static BaseFragmentMyGames newInstance() {
        BaseFragmentMyGames fragment = new BaseFragmentMyGames();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
        }
    }
}
