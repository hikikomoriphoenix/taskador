package marabillas.loremar.taskador.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

public class FinishedTasksRecyclerViewAdapter extends RecyclerView.Adapter {
    private MainInAppActivity mainInAppActivity;
    private List<TaskDatePair> tasks;

    public FinishedTasksRecyclerViewAdapter(MainInAppActivity mainInAppActivity) {
        this.mainInAppActivity = mainInAppActivity;
        tasks = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainInAppActivity);
        View view = inflater.inflate(R.layout.fragment_finishedtasks_listitem, parent, false);
        return new FinishedTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView finishedTaskView = holder.itemView.findViewById(R.id
                .fragment_finishedtasks_textview);
        TextView dateFinishedView = holder.itemView.findViewById(R.id
                .fragment_finishedtasks_listitem_datefinished);

        TaskDatePair entry = tasks.get(position);
        finishedTaskView.setText(entry.finishedTask);
        dateFinishedView.setText(entry.dateFinished);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void update(List<TaskDatePair> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class FinishedTasksViewHolder extends RecyclerView.ViewHolder {
        FinishedTasksViewHolder(View itemView) {
            super(itemView);
        }
    }
}
