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

import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.adapter.FinishedTasksRecyclerViewAdapter;

/**
 * {@link Fragment} representing the window for finished tasks. It displays a list of tasks set by the
 * user as finished during the current week, along with each corresponding date the task was
 * finished.
 */
public class FinishedTasksFragment extends Fragment {
    private MainInAppActivity mainInAppActivity;
    private FinishedTasksRecyclerViewAdapter adapter;
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
        View view = inflater.inflate(R.layout.fragment_finishedtasks, container, false);

        recyclerView = view.findViewById(R.id.fragment_finishedtasks_recyclerview);
        adapter = new FinishedTasksRecyclerViewAdapter(mainInAppActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(mainInAppActivity.getRecyclerViewOnScrollListener());

        fetchingDataView = view.findViewById(R.id.fragment_finishedtasks_fetchingdata);

        return view;
    }

    public void bindList(final List<TaskDatePair> tasks) {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.bindList(tasks);
            }
        });
    }

    /**
     * Display indeterminate horizontal progress bar to indicate that the list of finished tasks is
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
     * Hide fetching-data view and display the recycler view to show the list of finished tasks.
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
