package com.gofitness.fragment.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gofitness.R;
import com.gofitness.base.BaseFragment;

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

public class ExerciseFragment extends BaseFragment {

    private MagicIndicator mIndicator;
    private ViewPager mViewPager;
    private Fragment[] fragments;

    public static ExerciseFragment newInstance() {
        return new ExerciseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
        mIndicator = rootView.findViewById(R.id.magic_indicator);
        mViewPager = rootView.findViewById(R.id.view_pager);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化ViewPag
        fragments = new Fragment[]{
                MuscleGroupFragment.newInstance(),
                MuscleFragment.newInstance()
        };
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
        Collections.addAll(mTitleDataList, getString(R.string.indicator_muscle_group), getString(R.string.indicator_muscle));
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
                titleView.setNormalColor(getContext().getColor(R.color.colorPrimary));
                titleView.setSelectedColor(getContext().getColor(R.color.white));
                titleView.setText(mTitleDataList.get(index));
                titleView.setTextSize(15f);
                titleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(getContext().getColor(R.color.colorPrimary));
                indicator.setRoundRadius(getResources().getDimension(R.dimen.dp6));
                return indicator;
            }

        };
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(commonNavigatorAdapter);
        commonNavigator.setAdjustMode(true);
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

}
