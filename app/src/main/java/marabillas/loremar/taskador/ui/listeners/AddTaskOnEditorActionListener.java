package marabillas.loremar.taskador.ui.listeners;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Listen's for editor action events while user is using the soft keyboard to input new task.
 */
public class AddTaskOnEditorActionListener implements TextView.OnEditorActionListener {
    private MainInAppActivity mainInAppActivity;

    public AddTaskOnEditorActionListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mainInAppActivity.onAddTaskUserInput();
        }

        return false;
    }
}
