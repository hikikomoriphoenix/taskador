package marabillas.loremar.taskador.ui.listeners;

import android.view.View;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Listens to click events in in-app screen. Some components in the screen may have their own
 * listeners.
 */
public class MainInAppOnClickListener implements View.OnClickListener {
    private MainInAppActivity mainInAppActivity;

    public MainInAppOnClickListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_todotasks_addtask_button:
                mainInAppActivity.onAddTaskButtonClicked();
                break;
            case R.id.fragment_topwords_viewbutton:
                mainInAppActivity.onTopWordsViewButtonClicked();
                break;
            case R.id.activtiy_maininapp_reloadtool:
                mainInAppActivity.onReloadClicked();
                break;
            case R.id.activity_maininapp_logouttool:
                mainInAppActivity.logout();
                break;
        }
    }
}
