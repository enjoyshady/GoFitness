package com.gofitness.fragment.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gofitness.R;
import com.gofitness.activity.HistoryLogsActivity;
import com.gofitness.adapter.HistoryLogsAdapter;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.repository.ExerciseLogsRepository;

public class TodayFragment extends BaseFragment implements HistoryLogsAdapter.OnClickListener {

    private TodayViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private HistoryLogsAdapter mAdapter;

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        // history
        rootView.findViewById(R.id.history).setOnClickListener(
                v -> HistoryLogsActivity.startActivity(getContext()));
        // RecyclerView
        mRecyclerView = rootView.findViewById(R.id.RecyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        // 初始化RecyclerView
        mAdapter = new HistoryLogsAdapter(mViewModel.getExerciseLogsList().getValue());
        mAdapter.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        // 监听数据
        ExerciseLogsRepository.getInstance(getContext())
                .loadExerciseProjectLogs().observe(getViewLifecycleOwner(),
                mProjectLogsList -> mViewModel.setExerciseLogsList(mProjectLogsList));
        mViewModel.getExerciseLogsList().observe(getViewLifecycleOwner(),
                mProjectLogsList -> {
                    // 通知有变更
                    getView().findViewById(R.id.text_no_data)   // 没有数据提示语
                            .setVisibility(mProjectLogsList.size() == 0 ? View.VISIBLE : View.GONE);
                    mAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onClickItemListener(ExerciseLogsEntity entity) {

    }

    /**
     * 删除 Exercise Logs
     */
    @Override
    public void onClickDeleteListener(ExerciseLogsEntity entity) {
        new Thread(() -> {
            boolean isSuccessful = ExerciseLogsRepository.getInstance(getContext()).deleteProjectLogs(entity);
            getActivity().runOnUiThread(() -> {
                CharSequence showToast = isSuccessful ? getText(R.string.delete_successfully) : getText(R.string.delete_failed);
                Toast.makeText(getContext(), showToast, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

}