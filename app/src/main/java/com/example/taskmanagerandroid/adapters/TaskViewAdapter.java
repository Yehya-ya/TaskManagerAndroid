package com.example.taskmanagerandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.activities.ProjectActivity;
import com.example.taskmanagerandroid.activities.TaskActivity;
import com.example.taskmanagerandroid.fragments.CategoryFragment;
import com.example.taskmanagerandroid.models.Task;
import com.example.taskmanagerandroid.utils.ActionListener;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TASK_LAYOUT = 101;
    private static final int BUTTON_LAYOUT = 102;

    private final CategoryFragment mCategoryFragment;
    private final ActionListener mListener;
    private final boolean mHasPrevious;
    private final boolean mHasNext;
    private List<Task> mTasks;
    private NewTaskButtonViewHolder mNewTaskButtonViewHolder;

    public TaskViewAdapter(CategoryFragment activity, ActionListener listener, boolean hasPrevious, boolean hasNext) {
        this.mCategoryFragment = activity;
        this.mListener = listener;
        this.mTasks = new LinkedList<>();
        this.mHasPrevious = hasPrevious;
        this.mHasNext = hasNext;
    }

    public void setTasks(List<Task> mTasks) {
        this.mTasks = mTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCategoryFragment.getActivity());

        if (viewType == TASK_LAYOUT) {
            View view = inflater.inflate(R.layout.task_card, parent, false);
            return new TaskViewHolder(view);
        }

        if (mNewTaskButtonViewHolder != null) {
            return mNewTaskButtonViewHolder;
        }

        View view = inflater.inflate(R.layout.new_task_card, parent, false);
        mNewTaskButtonViewHolder = new NewTaskButtonViewHolder(view, mListener);
        return mNewTaskButtonViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < mTasks.size()) {
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            Task task = mTasks.get(position);
            taskViewHolder.title.setText(task.getTitle());
            if (task.getDescription() == null)
                taskViewHolder.description.setVisibility(View.GONE);
            if (task.getEndAt() == null) {
                taskViewHolder.endAt.setVisibility(View.GONE);
            } else {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
                taskViewHolder.endAt.setText(" " + formatter.format(task.getEndAt()));
            }
            if (mHasNext) {
                taskViewHolder.next.setOnClickListener(view -> {
                    mCategoryFragment.moveTask(position, true);
                });
            } else {
                taskViewHolder.next.setClickable(false);
                taskViewHolder.next.setFocusable(false);
                taskViewHolder.next.setImageTintList(ColorStateList.valueOf(mCategoryFragment.getContext().getColor(R.color.disabled)));
            }
            if (mHasPrevious) {
                taskViewHolder.previous.setOnClickListener(view -> {
                    mCategoryFragment.moveTask(position, false);
                });
            } else {
                taskViewHolder.previous.setClickable(false);
                taskViewHolder.previous.setFocusable(false);
                taskViewHolder.previous.setImageTintList(ColorStateList.valueOf(mCategoryFragment.getContext().getColor(R.color.disabled)));
            }
            ((TaskViewHolder) holder).taskCard.setOnClickListener(view -> {
                Intent intent = new Intent(mCategoryFragment.getContext(), TaskActivity.class);
                intent.putExtra("projectId", task.getProjectId());
                intent.putExtra("taskId", task.getId());
                ((ProjectActivity) mCategoryFragment.requireActivity()).getLaunchSomeActivity().launch(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        if (position < mTasks.size()) {
            Task task = mTasks.get(position);
            return task.hashCode() + task.getCategoryId();
        }
        return mNewTaskButtonViewHolder.hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mTasks.size()) ? TASK_LAYOUT : BUTTON_LAYOUT;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        public final TextView title;
        public final ImageView description;
        public final TextView endAt;
        public final ImageView next;
        public final ImageView previous;
        public final View taskCard;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            endAt = itemView.findViewById(R.id.task_end_at);
            next = itemView.findViewById(R.id.next);
            previous = itemView.findViewById(R.id.previous);
            taskCard = itemView.findViewById(R.id.task_card);

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
