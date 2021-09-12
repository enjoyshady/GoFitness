package com.gofitness.fragment.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.gofitness.R;
import com.gofitness.activity.ProjectListActivity;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.Constants;
import com.gofitness.database.FixedData;
import com.gofitness.view.LineItemView;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroupFragment extends BaseFragment {

    private MuscleGroupViewModel mViewModel;

    private List<LineItemView> mItemViews;

    public static MuscleGroupFragment newInstance() {
        return new MuscleGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_muscle_group, container, false);
        LinearLayout parentLayout = rootView.findViewById(R.id.parent_layout);
        // 创建子布局
        mItemViews = new ArrayList<>();
        mItemViews.add(new LineItemView(getContext())
                .setLeftText(Constants.AllExercises)
                .setItemImage(R.drawable.ic_muscle_group_all_exercises)
                .setViewTag(Constants.AllExercises));
        for (String groupName : FixedData.MuscleGroup) {
            String imageSrc = "ic_muscle_group_" + groupName.replace(" ", "_").toLowerCase();
            mItemViews.add(new LineItemView(getContext())
                    .setLeftText(groupName)
                    .setItemImageForDrawableRes(imageSrc)
                    .setViewTag(groupName));
        }
        // 添加子布局
        mItemViews.get(0).isTop(true);
        mItemViews.get(mItemViews.size() - 1).isBottom(true);
        for (LineItemView itemView : mItemViews) {
            parentLayout.addView(itemView.setLeftTextSize(R.dimen.sp20)
                    .setLeftTextColor(R.color.black_translucence_eighty)
                    .setViewLayoutHeight(R.dimen.dp60));
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MuscleGroupViewModel.class);
        for (LineItemView itemView : mItemViews) {
            itemView.setOnClickListener(view -> {
                ProjectListActivity.startActivityForMuscleGroup(getContext(), view.getTag().toString());
            });
        }
    }

}