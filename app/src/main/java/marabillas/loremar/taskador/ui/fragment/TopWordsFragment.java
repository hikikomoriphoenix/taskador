package marabillas.loremar.taskador.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Fragment representing the window for top words. It displays the most frequently used words for
 * tasks ordered starting from the most frequently used, along with each count value which
 * pertains to the number of times the word is used in tasks.
 */
public class TopWordsFragment extends Fragment {
    private MainInAppActivity mainInAppActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInAppActivity = (MainInAppActivity) getActivity();
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

        return view;
    }
}
