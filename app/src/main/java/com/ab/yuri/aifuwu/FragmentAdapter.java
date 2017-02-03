package com.ab.yuri.aifuwu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Yuri on 2017/2/3.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    public Fragment getItem(int fragment) {
        return fragments.get(fragment);
    }

    public int getCount() {
        return fragments.size();
    }

}
