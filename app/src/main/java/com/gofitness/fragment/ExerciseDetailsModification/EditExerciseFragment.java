package com.gofitness.fragment.ExerciseDetailsModification;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gofitness.R;
import com.gofitness.activity.EditExerciseActivity;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.FixedData;
import com.gofitness.database.entity.ExerciseProjectEntity;
import com.gofitness.database.repository.ExerciseProjectRepository;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EditExerciseFragment extends BaseFragment {

    private EditText mExerciseNameTextView;
    private TextView mMuscleGroupTextView;

    private MagicIndicator mIndicator;
    private ViewPager mViewPager;
    private Fragment[] fragments;

    private EditExerciseViewModel mViewModel;

    public static EditExerciseFragment newInstance() {
        return new EditExerciseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_exercise, container, false);
        mIndicator = rootView.findViewById(R.id.magic_indicator);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mExerciseNameTextView = rootView.findViewById(R.id.exerciseName);
        mMuscleGroupTextView = rootView.findViewById(R.id.muscleGroup);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditExerciseViewModel.class);
        // 初始化 Fragment
        fragments = new Fragment[]{
                EditExerciseImageFragment.newInstance(),
                EditExerciseMuscleFragment.newInstance(),
                EditExerciseDescriptionFragment.newInstance()
        };
        // 初始化 view pager
        initViewPager();
        // 选择 MuscleGroup
        mMuscleGroupTextView.setOnClickListener(v -> {
            closeKeyboard(); // 关闭键盘
            List<String> arrayList = Arrays.asList(FixedData.MuscleGroup);
            OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getContext(),
                    (options1, options2, options3, v12) -> {
                        String muscleGroup = arrayList.get(options1);
                        mMuscleGroupTextView.setText(muscleGroup);
                        mViewModel.getProjectEntityLiveData().getValue().setMuscleGroup(muscleGroup);
                    }).build();
            optionsPickerView.setNPicker(arrayList, null, null);
            optionsPickerView.show();
        });
        // 初始化数据
        if (getActivity().getIntent().getBooleanExtra(EditExerciseActivity.EXERCISE_IS_ADD, false)) {
            mViewModel.setProjectEntityLiveData(new ExerciseProjectEntity());  // add
        } else {   // edit
            new Thread(() -> {
                long projectID = getActivity().getIntent().getLongExtra(EditExerciseActivity.EXERCISE_PROJECT_ID, 0);
                ExerciseProjectEntity projectEntity = ExerciseProjectRepository.getInstance(getContext()).loadProjectListForID(projectID);
                getActivity().runOnUiThread(() -> {
                    mViewModel.setProjectEntityLiveData(projectEntity);
                });
            }).start();
        }
        // 监听数据恢复
        mViewModel.getProjectEntityLiveData().observe(getViewLifecycleOwner(),
                entity -> {
                    if (entity.getProjectID() != 0) { // 恢复数据
                        mExerciseNameTextView.setText(entity.getExerciseName());
                        mMuscleGroupTextView.setText(entity.getMuscleGroup());
                        if (fragments[0] instanceof EditExerciseImageFragment) {
                            ((EditExerciseImageFragment) fragments[0]).setImagePicture(entity.getImageURL());
                        }
                        if (fragments[2] instanceof EditExerciseDescriptionFragment) {
                            ((EditExerciseDescriptionFragment) fragments[2]).setDescriptionText(entity.getDescription());
                        }
                    }
                });
    }

    /**
     * 设置图片地址
     */
    public void setExerciseImageURL(String imageURL) {
        ExerciseProjectEntity entity = mViewModel.getProjectEntityLiveData().getValue();
        entity.setImageURL(imageURL);
    }

    /**
     * 设置 Primary Muscle
     */
    public void setExercisePrimaryMuscle(String primaryMuscle) {
        ExerciseProjectEntity entity = mViewModel.getProjectEntityLiveData().getValue();
        entity.setPrimaryMuscle(primaryMuscle);
    }

    /**
     * 设置 Primary Muscle
     */
    public void setExerciseSecondaryMuscle(String secondaryMuscle) {
        ExerciseProjectEntity entity = mViewModel.getProjectEntityLiveData().getValue();
        entity.setSecondaryMuscle(secondaryMuscle);
    }

    /**
     * 初始化 view pager
     */
    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), 2) {
            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        // view page 指示器
        List<String> mTitleDataList = new ArrayList<>();
        Collections.addAll(mTitleDataList, getString(R.string.indicator_image)
                , getString(R.string.indicator_muscle), getString(R.string.indicator_description));
        CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context) {
                    @Override
                    public int getContentLeft() {
                        return getLeft() + getPaddingLeft();
                    }

                    @Override
                    public int getContentRight() {
                        return getLeft() + getWidth() - getPaddingRight();
                    }
                };
                titleView.setNormalColor(getContext().getColor(R.color.black_translucence_eighty));
                titleView.setSelectedColor(getContext().getColor(R.color.white));
                titleView.setText(mTitleDataList.get(index));
                titleView.setTextSize(14f);
                titleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(getContext().getColor(R.color.black_translucence));
                indicator.setRoundRadius(getResources().getDimension(R.dimen.dp6));
                return indicator;
            }

        };
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(commonNavigatorAdapter);
        commonNavigator.setAdjustMode(true);
        mIndicator.setNavigator(commonNavigator);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING || state == ViewPager.SCROLL_STATE_SETTLING) {
                    closeKeyboard();
                }
                mIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_exercise, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save) { // 保存
            // 首要要做数据验证
            ExerciseProjectEntity entity = mViewModel.getProjectEntityLiveData().getValue();
            // 验证name
            String exerciseName = mExerciseNameTextView.getText().toString();
            if (TextUtils.isEmpty(exerciseName)) {
                mExerciseNameTextView.requestFocus();
                showToast(getText(R.string.prompt_enter_exercise_name));
                return false;
            } else entity.setExerciseName(exerciseName);
            // 验证肌群
            String muscleGroup = mMuscleGroupTextView.getText().toString();
            if (TextUtils.isEmpty(muscleGroup)) {
                mMuscleGroupTextView.requestFocus();
                showToast(getText(R.string.prompt_choose_a_muscle_group));
                return false;
            }
            // 验证图片
            if (TextUtils.isEmpty(entity.getImageURL())) {
                mViewPager.setCurrentItem(0);
                showToast(getText(R.string.prompt_select_a_picture));
                return false;
            }
            // 验证肌肉选择 ， 允许用户不选辅肌群
            String primaryMuscle = entity.getPrimaryMuscle();
            if (TextUtils.isEmpty(primaryMuscle)) {
                mViewPager.setCurrentItem(1);
                showToast(getText(R.string.prompt_select_primary_muscle));
                return false;
            }
            // 如果没有secondaryMuscle ， 则自动填充
            if (TextUtils.isEmpty(entity.getSecondaryMuscle())) {
                entity.setSecondaryMuscle(primaryMuscle);
            }
            // 验证描述
            String description = null;
            if (fragments[2] instanceof EditExerciseDescriptionFragment) {
                description = ((EditExerciseDescriptionFragment) fragments[2]).getDescriptionText();
            }
            if (TextUtils.isEmpty(description)) {
                mViewPager.setCurrentItem(2);
                showToast(getText(R.string.prompt_enter_description));
                return false;
            } else entity.setDescription(description);
            // 验证成功，插入数据库
            new Thread(() -> {
                boolean isSuccessfully = ExerciseProjectRepository.getInstance(getContext()).insertProjectData(entity);
                String toastTest = isSuccessfully ? "successfully added." : "addition failed.";
                getActivity().runOnUiThread(() -> { // 成功关闭页面
                    showToast(toastTest);
                    if (isSuccessfully) getActivity().finish();
                });
            }).start();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

}