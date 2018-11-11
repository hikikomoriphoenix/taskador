/*
 *    Copyright 2018 Loremar Marabillas
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.adapter.TodoTasksRecyclerViewAdapter;

/**
 * {@link Fragment} representing the window for to-do tasks. It allows the user to add tasks that
 * the user needs to finish.
 */
public class ToDoTasksFragment extends Fragment {
    private TodoTasksRecyclerViewAdapter adapter;
    private EditText addTaskBox;
    private Button addTaskButton;
    private MainInAppActivity mainInAppActivity;
    private RecyclerView recyclerView;
    private View fetchingDataView;

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
        recyclerView = view.findViewById(R.id.fragment_todotasks_recyclerview);
        adapter = new TodoTasksRecyclerViewAdapter(mainInAppActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainInAppActivity));

        addTaskBox = view.findViewById(R.id.fragment_todotasks_addtask_box);
        addTaskButton = view.findViewById(R.id.fragment_todotasks_addtask_button);
        fetchingDataView = view.findViewById(R.id.fragment_todotasks_fetchingdata);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addTaskBox.setOnEditorActionListener(mainInAppActivity.getAddTaskOnEditorActionListener());
        addTaskBox.addTextChangedListener(mainInAppActivity.getAddTaskBoxTextWatcher());

        addTaskButton.setOnClickListener(mainInAppActivity.getOnClickListener());
    }

    /**
     * Bind the list for to-do tasks to the adapter and update the view to display the values in
     * the list.
     *
     * @param tasks list of to-do tasks
     */
    public void bindList(final List<IdTaskPair> tasks) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.bindList(tasks);
            }
        });
    }

    /**
     * Notify the recycler view's adapter to update its view for the newly added task in the list.
     *
     * @param position position of the new task in the list
     */
    public void notifyTaskAdded(final int position) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemInserted(position);
            }
        });
    }

    public void removeTask(final int position) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.removeItem(position);
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

    /**
     * Clear the text in {@link ToDoTasksFragment}'s {@link EditText} for adding new tasks.
     */
    public void clearAddTaskBox() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addTaskBox.setText("");
            }
        });
    }

    /**
     * Get the text in {@link ToDoTasksFragment}'s {@link EditText} for adding new tasks.
     *
     * @return a string
     */
    public String getAddTaskBoxTextInput() {
        return String.valueOf(addTaskBox.getText());
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Display indeterminate horizontal progress bar to indicate that the list of to-do tasks is
     * being fetched from the back-end server.
     */
    public void showFetchingData() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                fetchingDataView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Hide fetching-data view and display the recycler view to show the list of to-do tasks.
     */
    public void showRecyclerView() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fetchingDataView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}
