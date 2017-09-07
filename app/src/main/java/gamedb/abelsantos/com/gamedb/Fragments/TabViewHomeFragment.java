package gamedb.abelsantos.com.gamedb.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gamedb.abelsantos.com.gamedb.Fragments.ViewPagerAdapter.ViewPagerAdapter;
import gamedb.abelsantos.com.gamedb.R;

/**
 * Created by Abel Cruz dos Santos on 21.08.2017.
 */

public class TabViewHomeFragment extends Fragment {
    public static final String TAG = "TabViewHomeFragment";
    private static final String KEY = "id";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int mId;


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
        /*//General info Fragment
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        Fragment gamesGeneralInfo = supportFragmentManager.findFragmentByTag(MyGameGeneralInfoFragment.TAG);
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_base_my_games_fragment, MyGameGeneralInfoFragment.newInstance(),
                        MyGameGeneralInfoFragment.TAG)
                .addToBackStack(MyGameGeneralInfoFragment.TAG)
                .commit();
        //Personal info Fragment
        Fragment personalInfor = supportFragmentManager.findFragmentByTag(MyGamesPersonalDetailsFragment.TAG);
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_base_my_games_fragment, MyGamesPersonalDetailsFragment.newInstance(),
                        MyGamesPersonalDetailsFragment.TAG)
                .addToBackStack(MyGamesPersonalDetailsFragment.TAG)
                .commit();*/
        MyGameGeneralInfoFragment gameGeneralInfoFragment = new MyGameGeneralInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, mId);
        gameGeneralInfoFragment.setArguments(bundle);
        adapter.addFrag(gameGeneralInfoFragment, "General");

        MyGamesPersonalDetailsFragment gamesPersonalDetailsFragment = new MyGamesPersonalDetailsFragment();
        gamesPersonalDetailsFragment.setArguments(bundle);
        adapter.addFrag(new MyGamesPersonalDetailsFragment(), "Personal");
        viewPager.setAdapter(adapter);
    }

    public static TabViewHomeFragment newInstance(int id) {
        TabViewHomeFragment fragment = new TabViewHomeFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, id);
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
