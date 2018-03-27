package com.jvillalba.hashr.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jvillalba.hashr.Fragments.fragmentHashFromFile;
import com.jvillalba.hashr.R;

public class PagerAdapter extends FragmentStatePagerAdapter{

    private int numberOfTabs;


    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return  createFromFileFragmentInstance(position);

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    private Fragment createFromFileFragmentInstance(int job) {
        Fragment newFragment = new fragmentHashFromFile();
        Bundle args = new Bundle();
        args.putInt("job", job+1);
        newFragment.setArguments(args);

        return newFragment;
    }
}
