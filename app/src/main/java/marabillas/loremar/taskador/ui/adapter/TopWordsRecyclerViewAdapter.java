package marabillas.loremar.taskador.ui.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.WordCountPair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Adapter for {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.support.v7.widget.RecyclerView} to display the list of top words.
 */
public class TopWordsRecyclerViewAdapter extends
        WordsRecyclerViewAdapter<TopWordsRecyclerViewAdapter.TopWordsViewHolder> {
    private MainInAppActivity mainInAppActivity;
    private List<WordCountPair> topWords;

    public TopWordsRecyclerViewAdapter(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity);
        this.mainInAppActivity = mainInAppActivity;
        topWords = new ArrayList<>();
    }

    @NonNull
    @Override
    public TopWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainInAppActivity);
        View view = inflater.inflate(R.layout.fragment_topwords_listitem, parent, false);
        return new TopWordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopWordsViewHolder holder, int position) {
        TextView wordView = holder.itemView.findViewById(R.id.fragment_topwords_listitem_word);
        TextView countView = holder.itemView.findViewById(R.id.fragment_topwords_listitem_count);

        // Prefix the text with a number corresponding to the word's position in the list.
        WordCountPair topWord = this.topWords.get(position);
        String wordViewtext = (position + 1) + ". " + topWord.word;

        wordView.setText(wordViewtext);
        countView.setText(topWord.count);
    }

    @Override
    public int getItemCount() {
        return topWords.size();
    }

    public void bindList(List<WordCountPair> words) {
        topWords = words;
        notifyDataSetChanged();
    }

    class TopWordsViewHolder extends WordsRecyclerViewAdapter.WordsViewHolder {
        TopWordsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
