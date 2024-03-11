package cz.vision.machinevision2017.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import cz.vision.machinevision2017.fragments.CameraFragment;
import cz.vision.machinevision2017.fragments.ContentFragment;

/**
 * Created by Michal on 16.12.2017.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new CameraFragment());
        fragmentList.add(new ContentFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
