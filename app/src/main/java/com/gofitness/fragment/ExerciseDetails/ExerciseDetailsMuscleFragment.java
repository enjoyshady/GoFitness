package com.gofitness.fragment.ExerciseDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.gofitness.R;
import com.gofitness.utils.FileUtils;

public class ExerciseDetailsMuscleFragment extends Fragment {

    private ExerciseDetailsViewModel mViewModel;

    private ImageView mPrimaryMuscleImageView;
    private ImageView mSecondaryMuscleImageView;

    public static ExerciseDetailsMuscleFragment newInstance() {
        return new ExerciseDetailsMuscleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_details_muscle, container, false);
        mPrimaryMuscleImageView = rootView.findViewById(R.id.primaryMuscleImage);
        mSecondaryMuscleImageView = rootView.findViewById(R.id.secondaryMuscleImage);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExerciseDetailsViewModel.class);
        mViewModel.getStringParam1().observe(getViewLifecycleOwner(),
                primaryMuscleImageURL -> Glide.with(this)
                        .load(FileUtils.getMuscleImageAssetsURL(primaryMuscleImageURL))
                        .into(mPrimaryMuscleImageView));
        mViewModel.getStringParam2().observe(getViewLifecycleOwner(),
                secondaryMuscleImageURL -> Glide.with(this)
                        .load(FileUtils.getMuscleImageAssetsURL(secondaryMuscleImageURL))
                        .into(mSecondaryMuscleImageView));
    }

    /**
     * 设置图片源，利用 ViewModel 保存
     */
    public void setExerciseMuscle(String primaryMuscleImageURL, String secondaryMuscleImageURL) {
        mViewModel.setStringParam1(primaryMuscleImageURL);
        mViewModel.setStringParam2(secondaryMuscleImageURL);
    }

}