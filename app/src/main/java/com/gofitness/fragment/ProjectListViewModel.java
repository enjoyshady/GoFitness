package com.gofitness.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gofitness.database.entity.ExerciseProjectEntity;

import java.util.ArrayList;
import java.util.List;

public class ProjectListViewModel extends ViewModel {

    private MutableLiveData<List<ExerciseProjectEntity>> mProjectList;

    public ProjectListViewModel(){
        mProjectList = new MutableLiveData<>();
        mProjectList.setValue(new ArrayList<>());
    }

    public LiveData<List<ExerciseProjectEntity>> getProjectList() {
        return mProjectList;
    }

    public void setProjectList(List<ExerciseProjectEntity> projectList) {
        List<ExerciseProjectEntity> listValue = mProjectList.getValue();
        listValue.clear();
        listValue.addAll(projectList);
        mProjectList.setValue(listValue);
    }

}