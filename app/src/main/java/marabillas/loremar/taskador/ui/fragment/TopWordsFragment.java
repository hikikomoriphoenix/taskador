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
 * Fragment representing the window for top words. It displays the most frequently used words for
 * tasks ordered starting from the most frequently used, along with each count value which
 * pertains to the number of times the word is used in tasks.
 */
public class TopWordsFragment extends Fragment {
    private MainInAppActivity mainInAppActivity;
    private RecyclerView recyclerView;
    private WordsRecyclerViewAdapter adapter;
    private ViewState currentViewState;

    public enum ViewState {TOP, EXCLUDED}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInAppActivity = (MainInAppActivity) getActivity();
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

        return view;
    }

    public void updateTopWordsList(List<WordCountPair> topWords) {
        if (adapter instanceof TopWordsRecyclerViewAdapter) {
            ((TopWordsRecyclerViewAdapter) adapter).update(topWords);
        }
    }

    public void updateExcludedWordsList(List<String> excludedWords) {
        if (adapter instanceof ExcludedWordsRecyclerViewAdapter) {
            ((ExcludedWordsRecyclerViewAdapter) adapter).update(excludedWords);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ViewState switchViewState() {
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

        return currentViewState;
    }
}
