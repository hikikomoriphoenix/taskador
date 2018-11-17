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

        // Clear any changes to translation. The item view could have been moved previously and
        // it may also be no longer bound to the same item in the list, therefore, the item view
        // should be restored to its original position.
        holder.itemView.setTranslationX(0);

        // Update to the new item view bound to the selected word.
        if (mainInAppActivity.getMainInApp().getSelectedItemPosition() == position) {
            mainInAppActivity.getMainInApp().setSelectedItemView(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return topWords.size();
    }

    public void bindList(List<WordCountPair> words) {
        topWords = words;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < topWords.size()) {
            topWords.remove(position);
            notifyItemRemoved(position);
        }
    }

    class TopWordsViewHolder extends WordsRecyclerViewAdapter.WordsViewHolder {
        TopWordsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
