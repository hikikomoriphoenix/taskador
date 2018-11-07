package marabillas.loremar.taskador.ui.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Adapter for {@link marabillas.loremar.taskador.ui.fragment.TopWordsFragment}'s
 * {@link android.support.v7.widget.RecyclerView} to display the list of words excluded from top
 * words list.
 */
public class ExcludedWordsRecyclerViewAdapter extends
        WordsRecyclerViewAdapter<ExcludedWordsRecyclerViewAdapter.ExcludedWordsViewHolder> {
    private MainInAppActivity mainInAppActivity;
    private List<String> excludedWords;

    public ExcludedWordsRecyclerViewAdapter(MainInAppActivity mainInAppActivity) {
        super(mainInAppActivity);
        this.mainInAppActivity = mainInAppActivity;
        excludedWords = new ArrayList<>();
    }

    @NonNull
    @Override
    public ExcludedWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainInAppActivity);
        View view = inflater.inflate(R.layout.fragment_topwords_excludedwords_listitem, parent,
                false);
        return new ExcludedWordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcludedWordsViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id
                .fragment_topwords_excludedwords_listitem_textview);
        textView.setText(excludedWords.get(position));

        // Clear any changes to translation. The item view could have been moved previously and
        // it may also be no longer bound to the same item in the list, therefore, the item view
        // should be restored to its original position.
        holder.itemView.setTranslationX(0);

        // Update to the new item view bound to the selected word.
        if (mainInAppActivity.getSelectedItemPosition() == position) {
            mainInAppActivity.setSelectedItemView(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return excludedWords.size();
    }

    public void bindList(List<String> words) {
        excludedWords = words;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        excludedWords.remove(position);
        notifyItemRemoved(position);
    }

    class ExcludedWordsViewHolder extends WordsRecyclerViewAdapter.WordsViewHolder {
        ExcludedWordsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
