package com.example.taskmanagerandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.Task;
import com.example.taskmanagerandroid.utils.ActionListener;

import java.util.LinkedList;
import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TASK_LAYOUT = 101;
    private static final int BUTTON_LAYOUT = 102;

    private final FragmentActivity mActivity;
    private final ActionListener mListener;
    private List<Task> mTasks;

    public TaskViewAdapter(FragmentActivity activity, ActionListener listener) {
        this.mActivity = activity;
        this.mListener = listener;
        this.mTasks = new LinkedList<>();
    }

    public void setTasks(List<Task> mTasks) {
        this.mTasks = mTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);

        if (viewType == TASK_LAYOUT) {
            View view = inflater.inflate(R.layout.task_card, parent, false);
            return new TaskViewHolder(view);
        }

        View view = inflater.inflate(R.layout.new_task_card, parent, false);
        return new NewTaskButtonViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < mTasks.size()) {
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            Task task = mTasks.get(position);
            taskViewHolder.title.setText(task.getTitle());
            taskViewHolder.description.setText(task.getDescription());
            if (task.getDescription() == null)
                taskViewHolder.description.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mTasks.size()) ? TASK_LAYOUT : BUTTON_LAYOUT;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

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

    public static class NewTaskButtonViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "NewTaskButtonViewHolder";

        public NewTaskButtonViewHolder(@NonNull View itemView, ActionListener listener) {
            super(itemView);
            Button mButton = itemView.findViewById(R.id.create_new_task);

            mButton.setOnClickListener(view -> {
                listener.action(true);
            });
        }
    }
}
