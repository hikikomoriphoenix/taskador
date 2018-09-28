package marabillas.loremar.taskador.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import marabillas.loremar.taskador.R;

/**
 * Fragment representing the window for top words. It displays the most frequently used words for
 * tasks ordered starting from the most frequently used, along with each count value which
 * pertains to the number of times the word is used in tasks.
 */
public class TopWordsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finishedtasks, container, false);
        return view;
    }
}
