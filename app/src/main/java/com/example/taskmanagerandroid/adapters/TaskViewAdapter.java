package com.example.taskmanagerandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.Task;

import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {

    private final FragmentActivity mActivity;
    private List<Task> mTasks;

    public TaskViewAdapter(FragmentActivity activity) {
        this.mActivity = activity;
    }

    public void setTasks(List<Task> mTasks) {
        this.mTasks = mTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.task_card, parent, false);
        return new TaskViewAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        if (task.getDescription() == null)
            holder.description.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "TaskViewHolder";

        public final TextView title;
        public final TextView description;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);

            itemView.setOnClickListener(view -> {
                // TODO: add Task activity
            });
        }
    }
}
