package com.summertaker.guide48;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.summertaker.guide48.data.Member;

import java.util.ArrayList;

public class MainAdapter extends FragmentPagerAdapter {

    ArrayList<Member> mMembers = new ArrayList<>();

    public MainAdapter(FragmentManager fm, ArrayList<Member> members) {
        super(fm);
        this.mMembers = members;
    }
    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(position);
    }

    /*
    @Override
    public int getItemPosition(Object object) {
        MainFragment fragment = (MainFragment) object;
        if (fragment != null) {
            fragment.update();
        }
        return super.getItemPosition(object);
    }
    */

    @Override
    public int getCount() {
        return mMembers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMembers.get(position).getName();
    }
}
