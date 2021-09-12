package com.gofitness.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.entity.ExerciseProjectEntity;
import com.gofitness.database.entity.ExerciseProjectLogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditLogViewModel extends ViewModel {

    // 当天时间
    private MutableLiveData<Date> mDateLiveData;
    // 计时时间
    private MutableLiveData<Integer> mTimeCountLiveData;
    // 是否正在计时
    private MutableLiveData<Boolean> mTimingLiveData;
    // 列表数据
    private MutableLiveData<List<ExerciseLogsEntity>> mExerciseLogsList;

    public EditLogViewModel() {
        mDateLiveData = new MutableLiveData<>();
        mDateLiveData.setValue(new Date());
        mTimeCountLiveData = new MutableLiveData<>();
        mTimingLiveData = new MutableLiveData<>();
        mTimingLiveData.setValue(false);
        mExerciseLogsList = new MutableLiveData<>();
        mExerciseLogsList.setValue(new ArrayList<>());
    }

    public LiveData<Date> getDateLiveData() {
        return mDateLiveData;
    }

    public void setDateLiveData(Date date) {
        mDateLiveData.setValue(date);
    }

    public LiveData<Integer> getTimeCountLiveData() {
        return mTimeCountLiveData;
    }

    public void setTimeCountLiveData(int timeCount) {
        mTimeCountLiveData.setValue(timeCount);
    }

    public MutableLiveData<Boolean> getTimingLiveData() {
        return mTimingLiveData;
    }

    public void setTimingLiveData(boolean timing) {
        mTimingLiveData.setValue(timing);
    }

    public LiveData<List<ExerciseLogsEntity>> getExerciseLogsList() {
        return mExerciseLogsList;
    }

    public void setExerciseLogsList(List<ExerciseLogsEntity> logsEntities) {
        // 添加数据
        List<ExerciseLogsEntity> listValue = mExerciseLogsList.getValue();
        listValue.clear();
        listValue.addAll(logsEntities);
        mExerciseLogsList.setValue(listValue);
    }
}