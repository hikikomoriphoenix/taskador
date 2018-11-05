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
    private MainInAppActivity mainInAppActivity;
    private RecyclerView recyclerView;
    private WordsRecyclerViewAdapter adapter;
    private ViewState currentViewState;
    private View fetchingDataView;

    public enum ViewState {TOP, EXCLUDED}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInAppActivity = (MainInAppActivity) getActivity();

        // Set this fragment to display list of top words by default
        adapter = new TopWordsRecyclerViewAdapter(mainInAppActivity);
        currentViewState = ViewState.TOP;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topwords, container, false);

        // Setup the spinner for number of results
        Spinner numResultsSpinner = view.findViewById(R.id.fragment_topwords_numresults_spinner);
        String[] array = getResources().getStringArray(R.array
                .activity_main_topwords_numresults_selection);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mainInAppActivity, R.layout
                .fragment_topwords_numresultsspinner_listitem, R.id
                .fragment_topwords_numresultsspinner_listitem_textview,
                array);
        numResultsSpinner.setAdapter(arrayAdapter);
        numResultsSpinner.setOnItemSelectedListener(mainInAppActivity
                .getTopWordsNumResultsSpinnerItemSelectedListener());

        // Setup recycler view.
        recyclerView = view.findViewById(R.id.fragment_topwords_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainInAppActivity));

        Button viewButton = view.findViewById(R.id.fragment_topwords_viewbutton);
        viewButton.setOnClickListener(mainInAppActivity.getOnClickListener());

        fetchingDataView = view.findViewById(R.id.fragment_topwords_fetchingdata);

        return view;
    }

    public void bindTopWordsList(final List<WordCountPair> topWords) {
        if (adapter instanceof TopWordsRecyclerViewAdapter) {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TopWordsRecyclerViewAdapter) adapter).bindList(topWords);
                }
            });
        }
    }

    public void updateExcludedWordsList(final List<String> excludedWords) {
        if (adapter instanceof ExcludedWordsRecyclerViewAdapter) {
            mainInAppActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ExcludedWordsRecyclerViewAdapter) adapter).update(excludedWords);
                }
            });
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void showFetchingData() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                fetchingDataView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showRecyclerView() {
        mainInAppActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fetchingDataView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Change the list being displayed from list of top words to list of excluded words or vice
     * versa.
     *
     * @return the {@link ViewState} indicating the new current list being displayed
     */
    public ViewState switchViewState() {
        mainInAppActivity.runOnUiThread(new SwitchViewStateRunnable());

        return currentViewState;
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
}
