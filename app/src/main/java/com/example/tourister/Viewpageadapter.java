package com.example.tourister;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Viewpageadapter extends FragmentPagerAdapter {
    public final List<Fragment> fragment1= new ArrayList<>();
    public final List<String> titie1= new ArrayList<>();

    public Viewpageadapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragment1.get(i);
    }

    @Override
    public int getCount() {
        return titie1.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titie1.get(position);
    }
    public void addfragment(Fragment fragment,String title)
    {
        fragment1.add(fragment);
        titie1.add(title);
    }
}