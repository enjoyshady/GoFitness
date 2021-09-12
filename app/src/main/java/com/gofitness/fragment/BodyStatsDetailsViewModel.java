package com.gofitness.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gofitness.database.entity.BodyStatsEntity;
import com.gofitness.database.entity.ExerciseProjectEntity;

import java.util.ArrayList;
import java.util.List;

public class BodyStatsDetailsViewModel extends ViewModel {

    private MutableLiveData<List<BodyStatsEntity>> mBodyStatsList;

    public BodyStatsDetailsViewModel(){
        mBodyStatsList = new MutableLiveData<>();
        mBodyStatsList.setValue(new ArrayList<>());
    }

    public LiveData<List<BodyStatsEntity>> getBodyStatsList() {
        return mBodyStatsList;
    }

    public void setBodyStatsList(List<BodyStatsEntity> bodyStatsList) {
        List<BodyStatsEntity> listValue = mBodyStatsList.getValue();
        listValue.clear();
        listValue.addAll(bodyStatsList);
        mBodyStatsList.setValue(listValue);
    }
}