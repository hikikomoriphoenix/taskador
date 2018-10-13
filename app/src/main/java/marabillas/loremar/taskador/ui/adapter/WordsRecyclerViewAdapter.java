package marabillas.loremar.taskador.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

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
            mainInAppActivity.onListItemTouch(v, event, getAdapterPosition());

            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }

            return true;
        }
    }
}
