package ca.uwaterloo.newsapp.ui.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new NewsFragment());
        fragments.add(new SearchFragment());
        fragments.add(new FavoriteFragment());
        fragments.add(new AccountFragment());
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }

}
