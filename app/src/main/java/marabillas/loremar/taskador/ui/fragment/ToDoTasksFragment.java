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
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.adapter.TodoTasksRecyclerViewAdapter;

/**
 * Fragment representing the window for to-do tasks. It allows the user to add tasks that the
 * user needs to finish.
 */
public class ToDoTasksFragment extends Fragment {
    private TodoTasksRecyclerViewAdapter adapter;
    private EditText addTaskBox;
    private ImageButton addTaskButton;
    private MainInAppActivity mainInAppActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInAppActivity = (MainInAppActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todotasks, container, false);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.fragment_todotasks_recyclerview);
        adapter = new TodoTasksRecyclerViewAdapter(mainInAppActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainInAppActivity));

        addTaskBox = view.findViewById(R.id.fragment_todotasks_addtask_box);
        addTaskButton = view.findViewById(R.id.fragment_todotasks_addtask_button);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addTaskBox.setOnEditorActionListener(mainInAppActivity.getAddTaskOnEditorActionListener());
        addTaskBox.addTextChangedListener(mainInAppActivity.getAddTaskBoxTextWatcher());

        addTaskButton.setOnClickListener(mainInAppActivity.getOnClickListener());
    }

    public void updateList(final List<String> tasks) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.update(tasks);
            }
        });
    }

    public void showAddTaskButton() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addTaskButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideAddTaskButton() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addTaskButton.setVisibility(View.GONE);
            }
        });
    }

    public void clearAddTaskBox() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addTaskBox.setText("");
            }
        });
    }

    public String getAddTaskBoxTextInput() {
        return String.valueOf(addTaskBox.getText());
    }
}
