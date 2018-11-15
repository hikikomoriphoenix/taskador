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

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Base class for {@link TopWordsRecyclerViewAdapter} and
 * {@link ExcludedWordsRecyclerViewAdapter}. It allows touch input from the user.
 *
 * @param <VH> a {@link WordsViewHolder} subclass
 */
public abstract class WordsRecyclerViewAdapter<VH extends WordsRecyclerViewAdapter.WordsViewHolder> extends
        RecyclerView.Adapter<VH> {
    private MainInAppActivity mainInAppActivity;

    WordsRecyclerViewAdapter(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
    }

    abstract class WordsViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        WordsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mainInAppActivity.getMainInApp().onListItemTouch(v, event, getAdapterPosition());

            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }

            return true;
        }
    }
}
