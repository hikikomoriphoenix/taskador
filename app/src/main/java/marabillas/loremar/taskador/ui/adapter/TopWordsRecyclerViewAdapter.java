package marabillas.loremar.taskador.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.WordCountPair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class TopWordsRecyclerViewAdapter extends WordsRecyclerViewAdapter<TopWordsRecyclerViewAdapter.TopWordsViewHolder> {
    private MainInAppActivity mainInAppActivity;
    private List<WordCountPair> topWords;

    public TopWordsRecyclerViewAdapter(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
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

        WordCountPair topWord = this.topWords.get(position);
        String wordViewtext = (position + 1) + "." + topWord.word;

        wordView.setText(wordViewtext);
        countView.setText(topWord.count);
    }

    @Override
    public int getItemCount() {
        return topWords.size();
    }

    @Override
    public void update(List<WordCountPair> words) {
        topWords = words;
        notifyDataSetChanged();
    }

    class TopWordsViewHolder extends RecyclerView.ViewHolder {
        TopWordsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
