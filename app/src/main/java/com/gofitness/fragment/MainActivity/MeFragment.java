package com.gofitness.fragment.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.gofitness.R;
import com.gofitness.activity.BodyStatsDetailsActivity;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.repository.BodyStatsRepository;
import com.gofitness.view.BodyStatsInputDialog;
import com.gofitness.view.LineItemView;

public class MeFragment extends BaseFragment {

    private LineItemView mHeightView;
    private LineItemView mWeightView;
    private LineItemView mBodyFatView;
    private LineItemView mChestView;
    private LineItemView mWaistView;
    private LineItemView mHipsView;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);
        // height
        mHeightView = rootView.findViewById(R.id.info_height);
        mHeightView.setOnClickListener(v -> BodyStatsInputDialog.newInstance(
                getString(R.string.info_height), getString(R.string.unit_cm),false)
                .show(getFragmentManager(), getString(R.string.info_height)));
        // weight
        mWeightView = rootView.findViewById(R.id.info_weight);
        mWeightView.setOnClickListener(v -> BodyStatsDetailsActivity.startActivity(
                getContext(), getString(R.string.info_weight), getString(R.string.unit_kg)));
        // body fat
        mBodyFatView = rootView.findViewById(R.id.info_body_fat);
        mBodyFatView.setOnClickListener(v -> BodyStatsDetailsActivity.startActivity(
                getContext(), getString(R.string.info_body_fat), getString(R.string.unit_percentage)));
        // chest
        mChestView = rootView.findViewById(R.id.info_chest);
        mChestView.setOnClickListener(v -> BodyStatsDetailsActivity.startActivity(
                getContext(), getString(R.string.info_chest), getString(R.string.unit_cm)));
        // waist
        mWaistView = rootView.findViewById(R.id.info_waist);
        mWaistView.setOnClickListener(v -> BodyStatsDetailsActivity.startActivity(
                getContext(), getString(R.string.info_waist), getString(R.string.unit_cm)));
        // hips
        mHipsView = rootView.findViewById(R.id.info_hips);
        mHipsView.setOnClickListener(v -> BodyStatsDetailsActivity.startActivity(
                getContext(), getString(R.string.info_hips), getString(R.string.unit_cm)));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // height
        BodyStatsRepository.getInstance(getContext())
                .loadBodyStatsForKey(getString(R.string.info_height))
                .observe(getViewLifecycleOwner(), height ->
                        mHeightView.setRightText(height == null ? getString(R.string.unit_null_cm) :
                                String.format("%s %s", height.getValue(), height.getUnit())));
        // weight
        BodyStatsRepository.getInstance(getContext())
                .loadBodyStatsForKey(getString(R.string.info_weight))
                .observe(getViewLifecycleOwner(), weight ->
                        mWeightView.setRightText(weight == null ? getString(R.string.unit_null_kg) :
                                String.format("%s %s", weight.getValue(), weight.getUnit())));
        // body fat
        BodyStatsRepository.getInstance(getContext())
                .loadBodyStatsForKey(getString(R.string.info_body_fat))
                .observe(getViewLifecycleOwner(), bodyFat ->
                        mBodyFatView.setRightText(bodyFat == null ? getString(R.string.unit_null_percentage) :
                                String.format("%s %s", bodyFat.getValue(), bodyFat.getUnit())));
        // chest
        BodyStatsRepository.getInstance(getContext())
                .loadBodyStatsForKey(getString(R.string.info_chest))
                .observe(getViewLifecycleOwner(), chest ->
                        mChestView.setRightText(chest == null ? getString(R.string.unit_null_cm) :
                                String.format("%s %s", chest.getValue(), chest.getUnit())));
        // waist
        BodyStatsRepository.getInstance(getContext())
                .loadBodyStatsForKey(getString(R.string.info_waist))
                .observe(getViewLifecycleOwner(), waist ->
                        mWaistView.setRightText(waist == null ? getString(R.string.unit_null_cm) :
                                String.format("%s %s", waist.getValue(), waist.getUnit())));
        // hips
        BodyStatsRepository.getInstance(getContext())
                .loadBodyStatsForKey(getString(R.string.info_hips))
                .observe(getViewLifecycleOwner(), hips ->
                        mHipsView.setRightText(hips == null ? getString(R.string.unit_null_cm) :
                                String.format("%s %s", hips.getValue(), hips.getUnit())));
    }

}