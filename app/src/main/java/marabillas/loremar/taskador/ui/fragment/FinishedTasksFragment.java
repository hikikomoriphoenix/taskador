package marabillas.loremar.taskador.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.adapter.FinishedTasksRecyclerViewAdapter;

/**
 * Fragment representing the window for finished tasks. It displays a list of tasks set by the user
 * as finished during the current week, along with each corresponding date the task was finished.
 */
public class FinishedTasksFragment extends Fragment {
    private MainInAppActivity mainInAppActivity;
    private FinishedTasksRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInAppActivity = (MainInAppActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finishedtasks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.fragment_finishedtasks_recyclerview);
        adapter = new FinishedTasksRecyclerViewAdapter(mainInAppActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void updateList(final List<TaskDatePair> tasks) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.update(tasks);
            }
        });
    }
}
