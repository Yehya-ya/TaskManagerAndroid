package com.example.taskmanagerandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.models.Project;
import com.example.taskmanagerandroid.projects.ProjectActivity;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private static final String TAG = "ProjectAdapter";

    private final Context context;
    private List<Project> projects;

    public ProjectAdapter(Context context) {
        this.context = context;
        this.projects = null;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.project_card, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.title.setText(project.getTitle());
        holder.description.setText(project.getDescription());
        holder.project_id = project.getId();
    }

    @Override
    public int getItemCount() {
        return projects == null ? 0 : projects.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        int project_id = -1;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.project_title);
            description = itemView.findViewById(R.id.project_description);

            Context context = itemView.getContext();

            itemView.setOnClickListener(view -> {
                if (project_id < 0) {
                    Log.e(TAG, "unknown project id" + project_id);
                    return;
                }
                Intent intent = new Intent(context, ProjectActivity.class);
                intent.putExtra("project_id", project_id);

                context.startActivity(intent);
            });
        }
    }
}