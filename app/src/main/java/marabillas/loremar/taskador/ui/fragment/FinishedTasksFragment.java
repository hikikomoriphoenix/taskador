package marabillas.loremar.taskador.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import marabillas.loremar.taskador.R;

/**
 * Fragment representing the window for finished tasks. It displays a list of tasks set by the user
 * as finished during the current week, along with each corresponding date the task was finished.
 */
public class FinishedTasksFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finishedtasks, container, false);
        return view;
    }
}
