package marabillas.loremar.taskador.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment;
import marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment;
import marabillas.loremar.taskador.ui.fragment.TopWordsFragment;

/**
 * Adapter for putting fragments into the ViewPager in in-app screen. Each fragment represents a
 * window for displaying each feature for the in-app screen.
 */
public class MainInappViewPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment todoTasksFragment;
    private Fragment finishedTasksFragment;
    private Fragment topWordsFragment;

    public MainInappViewPagerAdapter(FragmentManager fm) {
        super(fm);
        todoTasksFragment = new ToDoTasksFragment();
        finishedTasksFragment = new FinishedTasksFragment();
        topWordsFragment = new TopWordsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return todoTasksFragment;
            case 1:
                return finishedTasksFragment;
            case 2:
                return topWordsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


}
