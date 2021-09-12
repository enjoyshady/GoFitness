package com.gofitness.fragment.ExerciseDetailsModification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gofitness.R;
import com.gofitness.fragment.MainActivity.MuscleViewModel;
import com.gofitness.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class EditExerciseMuscleFragment extends Fragment {

    private MuscleViewModel mViewModel;

    private View mFrontView;
    private ImageView mFrontDisplayImage;
    private List<Button> mFrontButtons;

    private View mBackView;
    private ImageView mBackDisplayImage;
    private List<Button> mBackButtons;

    public static EditExerciseMuscleFragment newInstance() {
        return new EditExerciseMuscleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_exercise_muscle, container, false);
        // front
        mFrontView = rootView.findViewById(R.id.front_view);
        mFrontDisplayImage = rootView.findViewById(R.id.front_display_image);
        mFrontDisplayImage.setImageBitmap(FileUtils.getMuscleImage(getContext(), "front.png"));
        mFrontButtons = new ArrayList<>();
        mFrontButtons.add(rootView.findViewById(R.id.btn_deltoid));
        mFrontButtons.add(rootView.findViewById(R.id.btn_biceps_brachii));
        mFrontButtons.add(rootView.findViewById(R.id.btn_pectoralis_major));
        mFrontButtons.add(rootView.findViewById(R.id.btn_brachioradialis));
        mFrontButtons.add(rootView.findViewById(R.id.btn_flexor_carpi_ulnaris));
        mFrontButtons.add(rootView.findViewById(R.id.btn_rectus_abdominis));
        mFrontButtons.add(rootView.findViewById(R.id.btn_external_oblique));
        mFrontButtons.add(rootView.findViewById(R.id.btn_adductor));
        mFrontButtons.add(rootView.findViewById(R.id.btn_quadriceps_femoris));
        mFrontButtons.add(rootView.findViewById(R.id.btn_tibialis_anterior));
        for (Button frontButton : mFrontButtons) {
            frontButton.setOnClickListener(v -> onButtonClickListener(frontButton));
        }
        // back
        mBackView = rootView.findViewById(R.id.back_view);
        mBackDisplayImage = rootView.findViewById(R.id.back_display_image);
        mBackDisplayImage.setImageBitmap(FileUtils.getMuscleImage(getContext(), "back.png"));
        mBackButtons = new ArrayList<>();
        mBackButtons.add(rootView.findViewById(R.id.btn_trapezius));
        mBackButtons.add(rootView.findViewById(R.id.btn_infraspinatus));
        mBackButtons.add(rootView.findViewById(R.id.btn_triceps_brachii));
        mBackButtons.add(rootView.findViewById(R.id.btn_latissimus_dorsi));
        mBackButtons.add(rootView.findViewById(R.id.btn_gluteus_maximus));
        mBackButtons.add(rootView.findViewById(R.id.btn_biceps_femoris));
        mBackButtons.add(rootView.findViewById(R.id.btn_gastrocnemius));
        for (Button backButton : mBackButtons) {
            backButton.setOnClickListener(v -> onButtonClickListener(backButton));
        }
        // 切换前后
        rootView.findViewById(R.id.front_rolling_over).setOnClickListener(v -> mViewModel.setIsFrontData(false));
        rootView.findViewById(R.id.back_rolling_over).setOnClickListener(v -> mViewModel.setIsFrontData(true));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MuscleViewModel.class);
        mViewModel.isFrontDataObserve(getViewLifecycleOwner(), isFront -> {
            View outView;
            View inView;
            if (isFront) { // 翻转后是正面
                outView = mBackView; // 背面出去
                inView = mFrontView; // 正面进来
            } else { // 翻转后是背面
                outView = mFrontView; // 正面出去
                inView = mBackView; // 背面进来
            }
            // 下面只需要控制，出去和进入的动画，就行了
            ObjectAnimator animatorOut = ObjectAnimator.ofFloat(outView, "rotationY", 0, 90);
            ObjectAnimator animatorIn = ObjectAnimator.ofFloat(inView, "rotationY", -90, 0);
            animatorOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatorIn.start();
                    inView.setVisibility(View.VISIBLE);
                    outView.setVisibility(View.GONE);
                }
            });
            animatorIn.setDuration(200);
            animatorOut.setDuration(200);
            animatorOut.start();
        });
    }

    /**
     * 一堆按钮的点击事件
     *
     * @param clickView 点击的view
     */
    private void onButtonClickListener(Button clickView) {
        boolean isFront = mViewModel.isFrontData();
        // 判断这个按钮是不是已经选中过了。
        if (getText(R.string.text_Select).equals(clickView.getText().toString())) {
            new AlertDialog.Builder(getContext())
                    .setItems(new String[]{getString(R.string.text_primary_muscle), getString(R.string.text_secondary_muscle)},
                            (dialog, which) -> {
                                EditExerciseFragment fragment = getParentFragment() instanceof EditExerciseFragment ?
                                        (EditExerciseFragment) getParentFragment() : null;
                                if (which == 0 && fragment != null) {
                                    fragment.setExercisePrimaryMuscle(clickView.getTag().toString());
                                    Toast.makeText(getContext(), getText(R.string.text_setting_data_succeeded), Toast.LENGTH_SHORT).show();
                                } else if (which == 1 && fragment != null) {
                                    fragment.setExerciseSecondaryMuscle(clickView.getTag().toString());
                                    Toast.makeText(getContext(), getText(R.string.text_setting_data_succeeded), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), getText(R.string.text_failed_to_set_data), Toast.LENGTH_SHORT).show();
                                }
                            })
                    .show();
            return;
        } // 没有被选中
        // 切换按钮状态
        for (Button unselectedButton : isFront ? mFrontButtons : mBackButtons) {
            if (unselectedButton == clickView) { // 点击了这个
                clickView.setBackground(getContext().getDrawable(R.drawable.shape_muscle_button_selected));
                clickView.setTextColor(getContext().getColor(R.color.muscle_button_selected_color));
                clickView.setText(getText(R.string.text_Select));
            } else { // 其他改成未选择
                unselectedButton.setBackground(getContext().getDrawable(R.drawable.shape_muscle_button_unselected));
                unselectedButton.setTextColor(getContext().getColor(R.color.muscle_button_unselected_color));
                unselectedButton.setText(unselectedButton.getTag().toString());
            }
        }
        // 切换图片显示
        if (isFront) {
            mFrontDisplayImage.setImageBitmap(FileUtils.getMuscleImage
                    (getContext(), clickView.getTag().toString() + ".png"));
        } else {
            mBackDisplayImage.setImageBitmap(FileUtils.getMuscleImage
                    (getContext(), clickView.getTag().toString() + ".png"));
        }
    }

}