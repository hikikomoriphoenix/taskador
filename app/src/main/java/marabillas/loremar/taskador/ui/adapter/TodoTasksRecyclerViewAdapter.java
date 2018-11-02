package marabillas.loremar.taskador.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.IdTaskPair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Adapter for {@link marabillas.loremar.taskador.ui.fragment.ToDoTasksFragment}'s
 * {@link RecyclerView} to display a list of to-do tasks.
 */
public class TodoTasksRecyclerViewAdapter extends RecyclerView.Adapter<TodoTasksRecyclerViewAdapter.TodoTasksViewHolder> {
    private MainInAppActivity mainInAppActivity;
    private List<IdTaskPair> tasks;

    public TodoTasksRecyclerViewAdapter(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
        tasks = new ArrayList<>();
    }

    @NonNull
    @Override
    public TodoTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainInAppActivity);
        View view = inflater.inflate(R.layout.fragment_todotasks_listitem, parent, false);
        return new TodoTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoTasksViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id
                .fragment_todotasks_listitem_textview);
        IdTaskPair task = tasks.get(position);
        String text = task.task;
        textView.setText(text);

        // Clear any changes to translation. The item view could have been moved previously and
        // it may also be no longer bound to the same item in the list, therefore, the item view
        // should be restored to its original position.
        holder.itemView.setTranslationX(0);

        // Update to the new item view bound to the selected task.
        if (mainInAppActivity.getSelectedItemPosition() == position) {
            mainInAppActivity.setSelectedItemView(holder.itemView);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void bindList(List<IdTaskPair> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    class TodoTasksViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        TodoTasksViewHolder(View itemView) {
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
