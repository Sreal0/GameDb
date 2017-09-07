package gamedb.abelsantos.com.gamedb.Fragments.ViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Fragments.MyGameGeneralInfoFragment;
import gamedb.abelsantos.com.gamedb.Fragments.MyGamesPersonalDetailsFragment;

/**
 * Created by Abel Cruz dos Santos on 16.08.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }

    public void addFrag(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        /*switch (position) {
            case 0:
                return new MyGameGeneralInfoFragment();
            case 1:
                return new MyGamesPersonalDetailsFragment();
            default:
                return null; // Problem occurs at this condition!
        }*/
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }

    public List<String> getFragmentTitleList() {
        return mFragmentTitleList;
    }
}
