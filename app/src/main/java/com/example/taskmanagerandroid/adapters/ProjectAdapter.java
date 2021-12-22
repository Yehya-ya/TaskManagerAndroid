package com.example.taskmanagerandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.activities.ProjectActivity;
import com.example.taskmanagerandroid.fragments.EditProjectDialogFragment;
import com.example.taskmanagerandroid.models.Project;
import com.example.taskmanagerandroid.utils.ActionListener;
import com.example.taskmanagerandroid.viewmodels.ProjectCollectionViewModel;

import java.util.LinkedList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ProjectAdapter";
    private static final int EMPTY_LAYOUT = 101;
    private static final int LIST_LAYOUT = 102;

    private final Context mContext;
    ProjectCollectionViewModel mProjectsModel;
    private List<Project> mProjects;

    public ProjectAdapter(Context context) {
        this.mContext = context;
        this.mProjects = new LinkedList<>();
        mProjectsModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ProjectCollectionViewModel.class);

        mProjectsModel.getProjects().observe((LifecycleOwner) context, projects -> {
            setProjects(projects);
        });

    }

    public void setProjects(List<Project> projects) {
        this.mProjects = projects;
        notifyDataSetChanged();
    }

    public void addProject(String title, String description, ActionListener listener) {
        mProjectsModel.createProject(title, description, listener);
    }

    public void editProject(int position, String title, String description, ActionListener listener) {
        mProjectsModel.editProject(position, title, description, listener);
    }

    public void deleteProject(int position, ActionListener listener) {
        mProjectsModel.deleteProject(position, listener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == EMPTY_LAYOUT) {
            View view = inflater.inflate(R.layout.empty_porject_list_card, parent, false);
            return new EmptyProjectViewHolder(view);
        }

        View view = inflater.inflate(R.layout.project_card, parent, false);
        return new ProjectViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProjectViewHolder) {
            Project project = mProjects.get(position);
            ((ProjectViewHolder) holder).title.setText(project.getTitle());
            ((ProjectViewHolder) holder).description.setText(project.getDescription());
            if (project.getDescription() == null) {
                ((ProjectViewHolder) holder).description.setVisibility(View.GONE);
            } else {
                ((ProjectViewHolder) holder).description.setVisibility(View.VISIBLE);
            }
            ((ProjectViewHolder) holder).project = project;
            ((ProjectViewHolder) holder).menu.setOnClickListener(view -> {
                showMenu(view, position);
            });
        }
    }

    @SuppressLint("RestrictedApi")
    public void showMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.project_card_menu, popupMenu.getMenu());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }

        popupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_edit:
                    EditProjectDialogFragment fragment = new EditProjectDialogFragment(mProjects.get(position));
                    fragment.setActionListener(success -> {
                        if (success) {
                            editProject(position, fragment.getTitle(), fragment.getDescription(), success1 -> {
                                Toast.makeText(mContext, "Project has been updated successfully.", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                    fragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "edit fragment" + position);
                    break;
                case R.id.menu_delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Alert!!!")
                            .setMessage("Are you sure you want to delete this project?")
                            .setPositiveButton("yes", (dialogInterface, i) -> {
                                deleteProject(position, success -> {
                                    Toast.makeText(mContext, "Project has been deleted successfully.", Toast.LENGTH_SHORT).show();
                                });
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            })
                            .setIcon(R.drawable.ic_round_warning_24);
                    builder.show();
                    break;
            }

            return false;
        });
        popupMenu.setOnDismissListener(menu -> {

        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mProjects.size() == 0 ? 1 : mProjects.size();
    }

    @Override
    public long getItemId(int position) {
        return mProjects.size() == 0 ? super.getItemId(position) : mProjects.get(position).hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        if (mProjects.size() == 0) {
            return EMPTY_LAYOUT;
        }
        return LIST_LAYOUT;
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        CardView card;
        TextView title;
        TextView description;
        ImageView menu;
        Project project;

        public ProjectViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.project_title);
            description = itemView.findViewById(R.id.project_description);
            menu = itemView.findViewById(R.id.menu);
            card = itemView.findViewById(R.id.project_card);
            mContext = context;

            card.setOnClickListener(view -> {
                if (project == null) {
                    Log.e(TAG, "unknown project");
                    return;
                }
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("project_id", project.getId());

                mContext.startActivity(intent);
            });
        }
    }

    static class EmptyProjectViewHolder extends RecyclerView.ViewHolder {

        public EmptyProjectViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}