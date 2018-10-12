package marabillas.loremar.taskador.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import marabillas.loremar.taskador.entries.WordCountPair;

public abstract class WordsRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {
    public abstract void update(List<WordCountPair> words);
}
