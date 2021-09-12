package com.gofitness.fragment.ExerciseDetailsModification;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gofitness.R;
import com.gofitness.fragment.ExerciseDetails.ExerciseDetailsViewModel;

public class EditExerciseDescriptionFragment extends Fragment {

    private ExerciseDetailsViewModel mViewModel;

    private EditText mDescriptionEditText;

    public static EditExerciseDescriptionFragment newInstance() {
        return new EditExerciseDescriptionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_exercise_description, container, false);
        mDescriptionEditText = rootView.findViewById(R.id.description);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExerciseDetailsViewModel.class);
        mViewModel.getStringParam1().observe(getViewLifecycleOwner(),
                description -> mDescriptionEditText.setText(description));
    }

    /**
     * 设置 Description 文字，利用 ViewModel 保存
     */
    public void setDescriptionText(String text) {
        mViewModel.setStringParam1(text);
    }

    /**
     * 获取 Description 文字，利用 ViewModel 保存
     */
    public String getDescriptionText() {
        String text = mDescriptionEditText.getText().toString();
        if (TextUtils.isEmpty(text)) // 如果被清除掉了，那就得从ViewModel获取
            text = mViewModel.getStringParam1().getValue();
        return text;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 销毁时保存数据
        String text = mDescriptionEditText.getText().toString();
        mViewModel.setStringParam1(text);
    }
}