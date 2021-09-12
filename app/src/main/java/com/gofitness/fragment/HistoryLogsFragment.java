package com.gofitness.fragment;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gofitness.R;
import com.gofitness.adapter.HistoryLogsAdapter;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.entity.ExerciseLogsCount;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.repository.ExerciseLogsRepository;
import com.gofitness.view.LogsCalendarPainter;
import com.necer.calendar.MonthCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class HistoryLogsFragment extends BaseFragment implements HistoryLogsAdapter.OnClickListener {

    private HistoryLogsViewModel mViewModel;

    private MonthCalendar mMonthCalendar;
    private LogsCalendarPainter calendarPainter; // 日历绘制类

    private RecyclerView mRecyclerView;
    private HistoryLogsAdapter mAdapter;

    private TextView mShowMonthTextView;

    public static HistoryLogsFragment newInstance() {
        return new HistoryLogsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_logs, container, false);
        mMonthCalendar = rootView.findViewById(R.id.MonthCalendar);  // RecyclerView
        mRecyclerView = rootView.findViewById(R.id.RecyclerView);
        mShowMonthTextView = rootView.findViewById(R.id.show_month_date);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryLogsViewModel.class);
        // 初始化日历
        LogsCalendarPainter calendarPainter = new LogsCalendarPainter(getContext(), mMonthCalendar);
        mMonthCalendar.setCalendarPainter(calendarPainter);
        // 获取日期统计： 更新日历
        ExerciseLogsRepository.getInstance(getContext())
                .loadExerciseProjectLogsCount().observe(getViewLifecycleOwner(),
                list -> {
                    Map<String, Integer> map = new ArrayMap<>();
                    for (ExerciseLogsCount logsCount : list) {
                        map.put(logsCount.getDate(), logsCount.getCount());
                    }
                    calendarPainter.setLogsMap(map);
                });
        // 点击日历
        mMonthCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            // 更新列表数据
            ExerciseLogsRepository.getInstance(getContext())
                    .loadExerciseProjectLogsForDate(localDate.toDate().getTime()).observe(getViewLifecycleOwner(),
                    mProjectLogsList -> mViewModel.setExerciseLogsList(mProjectLogsList));
            // 更新上方显示
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            mShowMonthTextView.setText(dateFormat.format(localDate.toDate()));
        });
        // 初始化RecyclerView
        mAdapter = new HistoryLogsAdapter(mViewModel.getExerciseLogsList().getValue());
        mAdapter.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        // 监听数据
        mViewModel.getExerciseLogsList().observe(getViewLifecycleOwner(),
                mProjectLogsList -> {
                    // 通知有变更
                    getView().findViewById(R.id.text_no_data)   // 没有数据提示语
                            .setVisibility(mProjectLogsList.size() == 0 ? View.VISIBLE : View.GONE);
                    mAdapter.notifyDataSetChanged();
                });
        // 上个月 ， 下个月 ， 当月显示
        getView().findViewById(R.id.last_month).setOnClickListener(v -> {
            mMonthCalendar.toLastPager();
        });
        getView().findViewById(R.id.next_month).setOnClickListener(v -> {
            mMonthCalendar.toNextPager();
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