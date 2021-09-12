package com.gofitness.fragment.ExerciseDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gofitness.database.entity.ExerciseProjectEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * ExerciseDetails 公共 ViewModel ： 只能存两个String属性
 */
public class ExerciseDetailsViewModel extends ViewModel {

    private MutableLiveData<String> mStringParam1;
    private MutableLiveData<String> mStringParam2;

    public ExerciseDetailsViewModel(){
        mStringParam1 = new MutableLiveData<>();
        mStringParam2 = new MutableLiveData<>();
    }

    public LiveData<String> getStringParam1() {
        return mStringParam1;
    }

    public void setStringParam1(String param) {
        mStringParam1.setValue(param);
    }

    public LiveData<String> getStringParam2() {
        return mStringParam2;
    }

    public void setStringParam2(String param) {
        mStringParam2.setValue(param);
    }
}