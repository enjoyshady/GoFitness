package com.gofitness.fragment.ExerciseDetails;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gofitness.R;
import com.gofitness.activity.EditExerciseActivity;
import com.gofitness.activity.EditLogActivity;
import com.gofitness.activity.ExerciseDetailsActivity;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.repository.ExerciseProjectRepository;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseDetailsFragment extends BaseFragment {

    private TextView mExerciseNameTextView;
    private TextView mMuscleGroupTextView;

    private MagicIndicator mIndicator;
    private ViewPager mViewPager;
    private Fragment[] fragments;

    public static ExerciseDetailsFragment newInstance() {
        return new ExerciseDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_details, container, false);
        mIndicator = rootView.findViewById(R.id.magic_indicator);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mExerciseNameTextView = rootView.findViewById(R.id.exerciseName);
        mMuscleGroupTextView = rootView.findViewById(R.id.muscleGroup);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化ViewPag
        fragments = new Fragment[]{
                ExerciseDetailsImageFragment.newInstance(),
                ExerciseDetailsMuscleFragment.newInstance(),
                ExerciseDetailsDescriptionFragment.newInstance()
        };
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
        ViewPagerHelper.bind(mIndicator, mViewPager);
        // 初始化数据
        long projectID = getActivity().getIntent().getLongExtra(ExerciseDetailsActivity.EXERCISE_PROJECT_ID, 0);
        ExerciseProjectRepository.getInstance(getContext()).observeProjectListForID(projectID)
                .observe(getViewLifecycleOwner(), exerciseProject -> {
                    mExerciseNameTextView.setText(exerciseProject.getExerciseName());
                    mMuscleGroupTextView.setText(exerciseProject.getMuscleGroup());
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof ExerciseDetailsImageFragment) {
                            ((ExerciseDetailsImageFragment) fragment).setImagePicture(exerciseProject.getImageURL());
                        }
                        if (fragment instanceof ExerciseDetailsMuscleFragment) {
                            ((ExerciseDetailsMuscleFragment) fragment).setExerciseMuscle(
                                    exerciseProject.getPrimaryMuscle(), exerciseProject.getSecondaryMuscle());
                        }
                        if (fragment instanceof ExerciseDetailsDescriptionFragment) {
                            ((ExerciseDetailsDescriptionFragment) fragment).setDescriptionText(exerciseProject.getDescription());
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_exercise_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit) {
            new Thread(() -> {
                long projectID = getActivity().getIntent().getLongExtra(ExerciseDetailsActivity.EXERCISE_PROJECT_ID, 0);
                EditExerciseActivity.startActivityForEdit(getContext(), projectID);
            }).start();
        } else if (item.getItemId() == R.id.menu_add_logs) {
            long projectID = getActivity().getIntent().getLongExtra(ExerciseDetailsActivity.EXERCISE_PROJECT_ID, 0);
            EditLogActivity.startActivity(getContext(), projectID, mExerciseNameTextView.getText().toString(), getString(R.string.menu_add_log));
        } else return super.onOptionsItemSelected(item);
        return true;
    }
}