package marabillas.loremar.taskador.ui.adapter;

import android.app.Activity;
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
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class TodoTasksRecyclerViewAdapter extends RecyclerView.Adapter<TodoTasksRecyclerViewAdapter.TodoTasksViewHolder> {
    private MainInAppActivity activity;
    private List<String> tasks;

    public TodoTasksRecyclerViewAdapter(Activity activity) {
        this.activity = (MainInAppActivity) activity;
        tasks = new ArrayList<>();
    }

    @NonNull
    @Override
    public TodoTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.fragment_todotasks_listitem, parent, false);
        return new TodoTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoTasksViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id
                .fragment_todotasks_listitem_textview);
        String text = tasks.get(position);
        textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void update(List<String> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TodoTasksViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        TodoTasksViewHolder(View itemView) {
            super(itemView);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            activity.onListItemTouch(v, event, getAdapterPosition());

            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }

            return true;
        }
    }
}
