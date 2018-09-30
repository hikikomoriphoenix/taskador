package marabillas.loremar.taskador.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Adapter for putting fragments into the ViewPager in in-app screen. Each fragment represents a
 * window for displaying each feature for the in-app screen.
 */
public class MainInappViewPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;

    public MainInappViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }


}
