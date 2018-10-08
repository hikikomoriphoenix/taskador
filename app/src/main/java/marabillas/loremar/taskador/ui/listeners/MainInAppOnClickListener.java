package marabillas.loremar.taskador.ui.listeners;

import android.view.View;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class MainInAppOnClickListener implements View.OnClickListener {
    private MainInAppActivity mainInAppActivity;

    public MainInAppOnClickListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_todotasks_addtask_button) {
            mainInAppActivity.onAddTaskButtonClicked();
        }
    }
}
