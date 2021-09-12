package com.gofitness.fragment.ExerciseDetailsModification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gofitness.database.entity.ExerciseProjectEntity;

public class EditExerciseViewModel extends ViewModel {

    private MutableLiveData<ExerciseProjectEntity> mProjectEntityLiveData;

    public EditExerciseViewModel() {
        mProjectEntityLiveData = new MutableLiveData<>();
    }

    public LiveData<ExerciseProjectEntity> getProjectEntityLiveData() {
        return mProjectEntityLiveData;
    }

    public void setProjectEntityLiveData(ExerciseProjectEntity projectEntityLiveData) {
        mProjectEntityLiveData.setValue(projectEntityLiveData);
    }

}