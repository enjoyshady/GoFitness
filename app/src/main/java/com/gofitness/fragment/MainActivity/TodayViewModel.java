package com.gofitness.fragment.MainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gofitness.database.entity.BodyStatsEntity;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.entity.ExerciseProjectEntity;
import com.gofitness.database.entity.ExerciseProjectLogs;

import java.util.ArrayList;
import java.util.List;

public class TodayViewModel extends ViewModel {

    private MutableLiveData<List<ExerciseProjectLogs>> mExerciseLogsList;

    public TodayViewModel(){
        mExerciseLogsList = new MutableLiveData<>();
        mExerciseLogsList.setValue(new ArrayList<>());
    }

    public LiveData<List<ExerciseProjectLogs>> getExerciseLogsList() {
        return mExerciseLogsList;
    }

    public void setExerciseLogsList(List<ExerciseProjectLogs> mProjectLogsList) {
        // 添加title信息
        List<ExerciseProjectLogs> tempProjectLogsList = new ArrayList<>();
        ExerciseProjectEntity tempProjectEntity = null;
        for (ExerciseProjectLogs projectLogs : mProjectLogsList) {
            if (tempProjectEntity != projectLogs.getExerciseProjectEntity()) {
                tempProjectLogsList.add(projectLogs);
                tempProjectLogsList.add(projectLogs);
                tempProjectEntity = projectLogs.getExerciseProjectEntity();
            } else {
                tempProjectLogsList.add(projectLogs);
            }
        }
        // 添加数据
        List<ExerciseProjectLogs> listValue = mExerciseLogsList.getValue();
        listValue.clear();
        listValue.addAll(tempProjectLogsList);
        mExerciseLogsList.setValue(listValue);
    }

}