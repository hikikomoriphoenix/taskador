package marabillas.loremar.taskador.ui.adapter;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.entries.TaskDatePair;
import marabillas.loremar.taskador.ui.activity.MainInAppActivity;

/**
 * Adapter for {@link marabillas.loremar.taskador.ui.fragment.FinishedTasksFragment}'s
 * {@link RecyclerView} to display a list of finished tasks.
 */
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

        // Set text for the Date Finished component prefixed with a bolded label
        String label = "Date Finished: ";
        String text = label + entry.dateFinished;
        SpannableString spannableString = new SpannableString(text);
        StyleSpan bold = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(bold, 0, label.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        dateFinishedView.setText(spannableString);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void bindList(List<TaskDatePair> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class FinishedTasksViewHolder extends RecyclerView.ViewHolder {
        FinishedTasksViewHolder(View itemView) {
            super(itemView);
        }
    }
}
