package com.gofitness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gofitness.R;
import com.gofitness.activity.BodyStatsDetailsActivity;
import com.gofitness.activity.EditExerciseActivity;
import com.gofitness.activity.ExerciseDetailsActivity;
import com.gofitness.activity.ProjectListActivity;
import com.gofitness.adapter.ProjectListAdapter;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.entity.ExerciseProjectEntity;
import com.gofitness.database.repository.ExerciseProjectRepository;
import com.gofitness.view.BodyStatsInputDialog;

import java.util.List;

public class ProjectListFragment extends BaseFragment implements ProjectListAdapter.OnClickListener {

    private ProjectListViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private ProjectListAdapter mAdapter;

    public static ProjectListFragment newInstance() {
        return new ProjectListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mRecyclerView = rootView.findViewById(R.id.RecyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProjectListViewModel.class);
        // 初始化RecyclerView
        mAdapter = new ProjectListAdapter(mViewModel.getProjectList().getValue());
        mAdapter.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        // 初始化数据
        ExerciseProjectRepository projectRepository = ExerciseProjectRepository.getInstance(getContext());
        int projectType = getActivity().getIntent().getIntExtra(ProjectListActivity.PROJECT_TYPE, 0);
        LiveData<List<ExerciseProjectEntity>> listLiveData = null;
        if (projectType == ProjectListActivity.FOR_MUSCLE_GROUP) {
            String muscleGroup = getActivity().getIntent().getStringExtra(ProjectListActivity.PROJECT_MUSCLE_GROUP);
            listLiveData = projectRepository.observeProjectListForMuscleGroup(muscleGroup);
        } else if (projectType == ProjectListActivity.FOR_MUSCLE) {
            String muscle = getActivity().getIntent().getStringExtra(ProjectListActivity.PROJECT_MUSCLE);
            listLiveData = projectRepository.observeProjectListForMuscle(muscle);
        }
        listLiveData.observe(getViewLifecycleOwner(), list -> {
            getView().findViewById(R.id.text_no_data)   // 没有数据提示语
                    .setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            mViewModel.setProjectList(list);
            mAdapter.notifyDataSetChanged();
        });
    }

    /**
     * item click Listener
     */
    @Override
    public void onClickItemListener(ExerciseProjectEntity entity) {
        ExerciseDetailsActivity.startActivity(getContext(), entity.getProjectID());
    }

    /**
     * delete click Listener
     */
    @Override
    public void onClickDeleteListener(ExerciseProjectEntity entity) {
        new Thread(() -> {
            boolean isSuccessful = ExerciseProjectRepository.getInstance(getContext()).deleteProjectData(entity);
            getActivity().runOnUiThread(() -> {
                CharSequence showToast = isSuccessful ? getText(R.string.delete_successfully) : getText(R.string.delete_failed);
                Toast.makeText(getContext(), showToast, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            EditExerciseActivity.startActivityForAdd(getContext());
            return true;
        } else return super.onOptionsItemSelected(item);
    }

}