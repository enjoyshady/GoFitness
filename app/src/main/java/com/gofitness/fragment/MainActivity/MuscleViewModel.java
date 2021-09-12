package com.gofitness.fragment.MainActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class MuscleViewModel extends ViewModel {

    /**
     * 是否是正面
     */
    private MutableLiveData<Boolean> isFrontData;

    public MuscleViewModel() {
        isFrontData = new MutableLiveData<>();
    }

    public void isFrontDataObserve(@NonNull LifecycleOwner owner, @NonNull Observer<Boolean> observer) {
        isFrontData.observe(owner, observer);
    }

    public void setIsFrontData(Boolean isFrontData) {
        this.isFrontData.setValue(isFrontData);
    }

    /**
     * 是不是正面
     */
    public boolean isFrontData() {
        if (isFrontData.getValue() == null)
            return true;
        else return isFrontData.getValue();
    }

}