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

/**
 * 不能继承 BaseFragment  , 会导致 toolbar 重复设置
 */
public class ExerciseDetailsImageFragment extends Fragment {

    private ExerciseDetailsViewModel mViewModel;

    private ImageView mImageView;

    public static ExerciseDetailsImageFragment newInstance() {
        return new ExerciseDetailsImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_details_image, container, false);
        mImageView = rootView.findViewById(R.id.details_image);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExerciseDetailsViewModel.class);
        mViewModel.getStringParam1().observe(getViewLifecycleOwner(),
                imageURL -> Glide.with(this).load(imageURL).into(mImageView));
    }

    /**
     * 设置图片源，利用 ViewModel 保存
     */
    public void setImagePicture(String imageURL) {
        mViewModel.setStringParam1(imageURL);
    }

}