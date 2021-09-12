package com.gofitness.fragment.ExerciseDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gofitness.R;

public class ExerciseDetailsDescriptionFragment extends Fragment {

    private ExerciseDetailsViewModel mViewModel;

    private TextView mDescriptionTextView;

    public static ExerciseDetailsDescriptionFragment newInstance() {
        return new ExerciseDetailsDescriptionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_details_description, container, false);
        mDescriptionTextView = rootView.findViewById(R.id.description);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExerciseDetailsViewModel.class);
        mViewModel.getStringParam1().observe(getViewLifecycleOwner(),
                description -> mDescriptionTextView.setText(description));
    }

    /**
     * 设置 Description 文字，利用 ViewModel 保存
     */
    public void setDescriptionText(String text) {
        mViewModel.setStringParam1(text);
    }

}