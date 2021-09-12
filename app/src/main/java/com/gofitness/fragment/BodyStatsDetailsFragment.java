package com.gofitness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gofitness.R;
import com.gofitness.activity.BodyStatsDetailsActivity;
import com.gofitness.adapter.BodyStatsDetailsAdapter;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.entity.BodyStatsEntity;
import com.gofitness.database.repository.BodyStatsRepository;
import com.gofitness.view.BodyStatsInputDialog;

public class BodyStatsDetailsFragment extends BaseFragment implements BodyStatsDetailsAdapter.OnClickListener {

    private BodyStatsDetailsViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private BodyStatsDetailsAdapter mAdapter;

    public static BodyStatsDetailsFragment newInstance() {
        return new BodyStatsDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mRecyclerView = rootView.findViewById(R.id.RecyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BodyStatsDetailsViewModel.class);
        // 初始化RecyclerView
        mAdapter = new BodyStatsDetailsAdapter(mViewModel.getBodyStatsList().getValue());
        mAdapter.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        // 初始化数据
        String key = getActivity().getIntent().getStringExtra(BodyStatsDetailsActivity.BODY_STATS_KEY);
        BodyStatsRepository.getInstance(getContext()).loadBodyStatsListForKey(key)
                .observe(getViewLifecycleOwner(), list -> {
                    getView().findViewById(R.id.text_no_data)   // 没有数据提示语
                            .setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                    mViewModel.setBodyStatsList(list);
                    mAdapter.notifyDataSetChanged();
                });
    }

    /**
     * item click Listener
     */
    @Override
    public void onClickItemListener(BodyStatsEntity entity) {

    }

    /**
     * delete click Listener
     */
    @Override
    public void onClickDeleteListener(BodyStatsEntity entity) {
        new Thread(() -> {
            boolean isSuccessful = BodyStatsRepository.getInstance(getContext()).deleteBodyStatsData(entity);
            getActivity().runOnUiThread(() -> {
                CharSequence showToast = isSuccessful ? getText(R.string.delete_successfully) : getText(R.string.delete_failed);
                Toast.makeText(getContext(), showToast, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            String key = getActivity().getIntent().getStringExtra(BodyStatsDetailsActivity.BODY_STATS_KEY);
            String unit = getActivity().getIntent().getStringExtra(BodyStatsDetailsActivity.BODY_STATS_UNIT);
            BodyStatsInputDialog.newInstance(key, unit, true)
                    .show(getFragmentManager(), key);
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}