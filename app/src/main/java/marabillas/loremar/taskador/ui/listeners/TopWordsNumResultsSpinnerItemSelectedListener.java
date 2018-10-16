package marabillas.loremar.taskador.ui.listeners;

import android.view.View;
import android.widget.AdapterView;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Listens to events in {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.widget.Spinner} which is used for selecting number of results for top words list.
 */
public class TopWordsNumResultsSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private MainInAppActivity mainInAppActivity;

    public TopWordsNumResultsSpinnerItemSelectedListener(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mainInAppActivity.onChangeTopWordsNumResults(10);
                break;
            case 1:
                mainInAppActivity.onChangeTopWordsNumResults(20);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
