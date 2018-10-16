package marabillas.loremar.taskador.ui.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Listens to events in {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * {@link android.widget.EditText} which is for inputting new tasks.
 */
public class AddTaskBoxTextWatcher implements TextWatcher {
    private MainInAppActivity mainInAppActivity;

    public AddTaskBoxTextWatcher(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mainInAppActivity.onAddTaskBoxTextChanged(s);
    }
}
