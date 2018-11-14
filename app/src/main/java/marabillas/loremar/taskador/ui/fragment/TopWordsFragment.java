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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.WordCountPair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;
import marabillas.loremar.taskador.ui.adapter.ExcludedWordsRecyclerViewAdapter;
import marabillas.loremar.taskador.ui.adapter.TopWordsRecyclerViewAdapter;
import marabillas.loremar.taskador.ui.adapter.WordsRecyclerViewAdapter;

/**
 * {@link Fragment} representing the window for top words. It displays the most frequently used
 * words for tasks ordered starting from the most frequently used, along with each count value which
 * pertains to the number of times the word is used in tasks.
 */
public class TopWordsFragment extends Fragment {
    private RecyclerView recyclerView;
    private WordsRecyclerViewAdapter adapter;
    private ViewState currentViewState;
    private View fetchingDataView;

    public enum ViewState {TOP, EXCLUDED}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_topwords, container, false);

        // Setup the spinner for number of results
        Spinner numResultsSpinner = view.findViewById(R.id.fragment_topwords_numresults_spinner);
        String[] array = getResources().getStringArray(R.array
                .activity_main_topwords_numresults_selection);

        if (mainInAppActivity != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mainInAppActivity, R.layout
                    .fragment_topwords_numresultsspinner_listitem, R.id
                    .fragment_topwords_numresultsspinner_listitem_textview,
                    array);
            numResultsSpinner.setAdapter(arrayAdapter);
            numResultsSpinner.setOnItemSelectedListener(mainInAppActivity
                    .getTopWordsNumResultsSpinnerItemSelectedListener());

            // Set this fragment to display list of top words by default
            adapter = new TopWordsRecyclerViewAdapter(mainInAppActivity);
            currentViewState = ViewState.TOP;

            // Setup recycler view.
            recyclerView = view.findViewById(R.id.fragment_topwords_recyclerview);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mainInAppActivity));
            recyclerView.addOnScrollListener(mainInAppActivity.getRecyclerViewOnScrollListener());

            Button viewButton = view.findViewById(R.id.fragment_topwords_viewbutton);
            viewButton.setOnClickListener(mainInAppActivity.getOnClickListener());
        }

        fetchingDataView = view.findViewById(R.id.fragment_topwords_fetchingdata);

        return view;
    }

    public void bindTopWordsList(final List<WordCountPair> topWords) {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (adapter instanceof TopWordsRecyclerViewAdapter) {
            if (mainInAppActivity != null) {
                mainInAppActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TopWordsRecyclerViewAdapter) adapter).bindList(topWords);
                    }
                });
            }
        }
    }

    public void removeTopWord(final int position) {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (mainInAppActivity != null) {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter instanceof TopWordsRecyclerViewAdapter) {
                        ((TopWordsRecyclerViewAdapter) adapter).removeItem(position);
                    }
                }
            });
        }
    }

    public void bindExcludedWordsList(final List<String> excludedWords) {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (adapter instanceof ExcludedWordsRecyclerViewAdapter) {
            if (mainInAppActivity != null) {
                mainInAppActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ExcludedWordsRecyclerViewAdapter) adapter).bindList(excludedWords);
                    }
                });
            }
        }
    }

    public void removeExcludedWord(final int position) {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (mainInAppActivity != null) {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter instanceof ExcludedWordsRecyclerViewAdapter) {
                        ((ExcludedWordsRecyclerViewAdapter) adapter).removeItem(position);
                    }
                }
            });
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void showFetchingData() {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (mainInAppActivity != null) {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setVisibility(View.GONE);
                    fetchingDataView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void showRecyclerView() {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (mainInAppActivity != null) {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fetchingDataView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    /**
     * Change the list being displayed from list of top words to list of excluded words or vice
     * versa.
     *
     * @return the {@link ViewState} indicating the new current list being displayed
     */
    public ViewState switchViewState() {
        MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();
        if (mainInAppActivity != null) {
            mainInAppActivity.runOnUiThread(new SwitchViewStateRunnable());
        }

        return currentViewState;
    }

    /**
     * Get the value of the spinner representing the number of results for top words.
     *
     * @return number of results for top words.
     */
    public int getNumResults() {
        View view = getView();
        Spinner spinner;
        if (view != null) {
            spinner = getView().findViewById(R.id.fragment_topwords_numresults_spinner);
            String item = spinner.getSelectedItem().toString();
            return Integer.parseInt(item);
        } else {
            throw new IllegalStateException("View containing the spinner does not exist.");
        }
    }

    private class SwitchViewStateRunnable implements Runnable {
        @Override
        public void run() {
            TextView header = null;
            View numResultsSection = null;
            View columnLabelSection = null;
            Button viewButton = null;

            if (getView() != null) {
                header = getView().findViewById(R.id.fragment_topwords_header);
                numResultsSection = getView().findViewById(R.id.fragment_topwords_numresults_section);
                columnLabelSection = getView().findViewById(R.id.fragment_topwords_columnlabel_section);
                viewButton = getView().findViewById(R.id.fragment_topwords_viewbutton);
            }

            MainInAppActivity mainInAppActivity = (MainInAppActivity) getActivity();

            switch (currentViewState) {
                case TOP:
                    currentViewState = ViewState.EXCLUDED;

                    // Change header text to 'Excluded Words', hide number-of-results spinner,
                    // hide column labels, and change button text to 'VIEW TOP'.
                    if (header != null) {
                        header.setText(R.string.fragment_topwords_excludedwords_header);
                    }
                    if (numResultsSection != null) {
                        numResultsSection.setVisibility(View.GONE);
                    }
                    if (columnLabelSection != null) {
                        columnLabelSection.setVisibility(View.GONE);
                    }
                    if (viewButton != null) {
                        viewButton.setText(R.string.fragment_topwords_viewtop_buttonlabel);
                    }

                    adapter = new ExcludedWordsRecyclerViewAdapter(mainInAppActivity);
                    recyclerView.setAdapter(adapter);
                    break;

                case EXCLUDED:
                    currentViewState = ViewState.TOP;

                    // Change header text to 'Top Words', show number-of-results spinner, show
                    // column labels, and change button text to 'VIEW EXCLUDED'.
                    if (header != null) {
                        header.setText(R.string.fragment_topwords_header);
                    }
                    if (numResultsSection != null) {
                        numResultsSection.setVisibility(View.VISIBLE);
                    }
                    if (columnLabelSection != null) {
                        columnLabelSection.setVisibility(View.VISIBLE);
                    }
                    if (viewButton != null) {
                        viewButton.setText(R.string.fragment_topwords_viewexcluded_buttonlabel);
                    }

                    adapter = new TopWordsRecyclerViewAdapter(mainInAppActivity);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    }

    public ViewState getCurrentViewState() {
        return currentViewState;
    }
}
