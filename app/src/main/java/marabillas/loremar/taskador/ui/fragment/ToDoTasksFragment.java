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
import marabillas.loremar.taskador.ui.adapter.TodoTasksRecyclerViewAdapter;

/**
 * Fragment representing the window for to-do tasks. It allows the user to add tasks that the
 * user needs to finish.
 */
public class ToDoTasksFragment extends Fragment {
    private TodoTasksRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todotasks, container, false);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.fragment_todotasks_recyclerview);
        adapter = new TodoTasksRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void updateList(final List<String> tasks) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.update(tasks);
                }
            });
        }
    }
}
