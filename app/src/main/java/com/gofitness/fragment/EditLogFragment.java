
package com.gofitness.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gofitness.R;
import com.gofitness.activity.EditLogActivity;
import com.gofitness.adapter.EditLogsAdapter;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.Constants;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.repository.ExerciseLogsRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 这个功能太多了，写的很差，TODO 应该拆分
 */
public class EditLogFragment extends BaseFragment implements EditLogsAdapter.OnClickListener {

    private EditLogViewModel mViewModel;

    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    // name
    private TextView mExerciseNameTextView;

    // by repetitions
    private TextView mWeightTextView;
    private TextView mRepetitionsTextView;
    private View mByRepetitionsAddLogButton;

    // by time
    private View mTimeStartButton;
    private TextView mTimeStartTextView;
    private TextView mTimeShowTextView;
    private View mByTimeAddLogButton;
    private Disposable mTimeDisposable = null;

    // timer
    private View mTimerButton;
    private TextView mTimerTextView;
    private ImageView mTimerImageView;
    private Disposable mTimerDisposable = null;

    // countdown
    private View mCountdownButton;
    private TextView mCountdownTextView;
    private ImageView mCountdownImageView;
    private Disposable mCountdownDisposable = null;

    // date
    private TextView mDateTextView;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private EditLogsAdapter mAdapter;

    public static EditLogFragment newInstance() {
        return new EditLogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_log, container, false);
        mExerciseNameTextView = rootView.findViewById(R.id.exerciseName);
        mWeightTextView = rootView.findViewById(R.id.weight);
        mRepetitionsTextView = rootView.findViewById(R.id.repetitions);
        mByRepetitionsAddLogButton = rootView.findViewById(R.id.btn_by_repetitions_add_log);
        mTimeStartButton = rootView.findViewById(R.id.btn_time_start);
        mTimeStartTextView = rootView.findViewById(R.id.text_time_start);
        mTimeShowTextView = rootView.findViewById(R.id.btn_time_show);
        mByTimeAddLogButton = rootView.findViewById(R.id.btn_by_time_add_log);
        mTimerButton = rootView.findViewById(R.id.btn_timer);
        mTimerTextView = rootView.findViewById(R.id.text_timer);
        mTimerImageView = rootView.findViewById(R.id.image_timer);
        mCountdownButton = rootView.findViewById(R.id.btn_countdown);
        mCountdownTextView = rootView.findViewById(R.id.text_countdown);
        mCountdownImageView = rootView.findViewById(R.id.image_countdown);
        mDateTextView = rootView.findViewById(R.id.text_date);
        mRecyclerView = rootView.findViewById(R.id.RecyclerView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditLogViewModel.class);
        // name
        String exerciseName = getActivity().getIntent().getStringExtra(EditLogActivity.EXERCISE_PROJECT_NAME);
        mExerciseNameTextView.setText(exerciseName);
        // ********* date *********
        // 日期有变化
        mViewModel.getDateLiveData().observe(getViewLifecycleOwner(), date -> {
            String formatDate = mSimpleDateFormat.format(date);
            mDateTextView.setText(formatDate);
        });
        // 点击日期选择框
        mDateTextView.setOnClickListener(v -> {
            closeKeyboard();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mViewModel.getDateLiveData().getValue());
            new TimePickerBuilder(getContext(), (date, v1) -> mViewModel.setDateLiveData(date))
                    .setDate(calendar).build().show();
        });
        // ********* repetitions *********
        // 点击 repetitions 添加日志框
        mByRepetitionsAddLogButton.setOnClickListener(v -> {
            String repetitions = mRepetitionsTextView.getText().toString();
            if (TextUtils.isEmpty(repetitions)) {
                showToast(getText(R.string.prompt_repetitions));
                mRepetitionsTextView.requestFocus();
            } else {
                String weight = mWeightTextView.getText().toString();
                addLogForRepetitions(repetitions, weight);
            }
        });
        // ********* time *********
        // 打开计时选择
        mTimeShowTextView.setOnClickListener(v -> {
            // 先判断有没有在倒计时 , 有则不能选时间
            if (mViewModel.getTimingLiveData().getValue()) return;
            closeKeyboard();
            List<String> arrayList = new ArrayList<>(Constants.getExercisesTime().keySet());
            OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getContext(),
                    (options1, options2, options3, v12) -> {
                        String timeKey = arrayList.get(options1);
                        Integer timeCount = Constants.getExercisesTime().get(timeKey);
                        mViewModel.setTimeCountLiveData(timeCount);
                    }).build();
            optionsPickerView.setNPicker(arrayList, null, null);
            optionsPickerView.show();
        });
        // 记录的时间值有变化
        mViewModel.getTimeCountLiveData().observe(getViewLifecycleOwner(), timeCount -> {
            int minutes = timeCount / 60;
            int seconds = timeCount % 60;
            mTimeShowTextView.setText(String.format(getString(R.string.text_format_time_minute), minutes, seconds));
        });
        // 点击开始计时
        mTimeStartButton.setOnClickListener(v -> {
            // 首先判断有没有选时间
            if (mViewModel.getTimeCountLiveData().getValue() == null) {
                showToast(getText(R.string.prompt_time));
            } else  // 没有在倒计时：开始倒计时 , 否则暂停。 刚好状态取反
                mViewModel.setTimingLiveData(!mViewModel.getTimingLiveData().getValue());
        });
        // 倒计时状态改变
        mViewModel.getTimingLiveData().observe(getViewLifecycleOwner(),
                timing -> {
                    if (timing) { // 开始倒计时
                        int timeCount = mViewModel.getTimeCountLiveData().getValue();
                        mTimeDisposable = Observable
                                .interval(0, 1, TimeUnit.SECONDS) //延迟0，间隔1s，单位秒
                                .take(timeCount + 1)    // 计时次数
                                .map(aLong -> timeCount - (int) ((long) aLong))   // 转换成 int
                                .observeOn(AndroidSchedulers.mainThread())  // 主线程
                                .doOnSubscribe(subscription -> {    // 准备工作
                                    mTimeStartTextView.setText(getText(R.string.text_stop));
                                }).subscribe(count -> { // 倒计时中
                                    int minutes = count / 60;
                                    int seconds = count % 60;
                                    mTimeShowTextView.setText(String.format(getString(R.string.text_format_time_minute), minutes, seconds));
                                }, throwable -> { // 出错
                                    throwable.printStackTrace();
                                    showToast("error : " + throwable.getMessage());
                                    // 重新设置一遍，会刷新显示
                                    mViewModel.setTimeCountLiveData(mViewModel.getTimeCountLiveData().getValue());
                                }, () -> {  // 倒计时完成
                                    // 添加日志
                                    addLogForTime(mViewModel.getTimeCountLiveData().getValue());
                                    // 改变运行记录状态
                                    mViewModel.setTimingLiveData(false);
                                });
                        mDisposables.add(mTimeDisposable); // 添加销毁列表
                    } else { // 暂停
                        // 先暂停倒计时
                        if (mTimeDisposable != null && !mTimeDisposable.isDisposed()) {
                            mTimeDisposable.dispose();
                            mTimeDisposable = null;
                        }
                        // 更新时间显示
                        mTimeStartTextView.setText(getText(R.string.text_start));
                        // 重新设置一遍，会刷新显示
                        Integer timeCount = mViewModel.getTimeCountLiveData().getValue();
                        if (timeCount != null) mViewModel.setTimeCountLiveData(timeCount);
                    }
                });
        // 点击 time 添加日志框
        mByTimeAddLogButton.setOnClickListener(v -> {
            // 首先判断有没有选时间
            if (mViewModel.getTimeCountLiveData().getValue() == null) {
                showToast(getText(R.string.prompt_time));
            } else {    // 添加日志
                addLogForTime(mViewModel.getTimeCountLiveData().getValue());
            }
        });
        // ********* timer *********
        // 点击开始正计时
        mTimerButton.setOnClickListener(v -> {
            // 首先判断是否正在计时中
            if (mTimerDisposable == null) {
                // 没有在正计时，开始计时
                mTimerDisposable = Observable
                        .interval(0, 1, TimeUnit.SECONDS) //延迟0，间隔1s，单位秒
                        .map(aLong -> (int) ((long) aLong))   // 转换成 int
                        .observeOn(AndroidSchedulers.mainThread())  // 主线程
                        .doOnSubscribe(subscription -> { // 准备工作
                            mTimerImageView.setVisibility(View.INVISIBLE);
                            mTimerTextView.setVisibility(View.VISIBLE);
                            mTimerTextView.setText(String.format(getString(R.string.text_format_time_hour), 0, 0, 0));
                        }).subscribe(count -> { // 计时中
                            int seconds = count % 60;
                            int minutes = count / 60 % 60;
                            int hours = minutes / 60;
                            mTimerTextView.setText(String.format(getString(R.string.text_format_time_hour), hours, minutes, seconds));
                        });
                mDisposables.add(mTimeDisposable); // 添加销毁列表
            } else { // 暂停
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.dialog_title_stop_timer)
                        .setPositiveButton(getText(R.string.text_ok), (dialog, which) -> {
                            if (!mTimerDisposable.isDisposed())
                                mTimerDisposable.dispose();
                            mTimerDisposable = null;
                            mTimerImageView.setVisibility(View.VISIBLE);
                            mTimerTextView.setVisibility(View.INVISIBLE);
                        }).setNegativeButton(getText(R.string.text_cancel), null)
                        .show();
            }
        });
        // ********* RecyclerView *********
        mAdapter = new EditLogsAdapter(mViewModel.getExerciseLogsList().getValue());
        mAdapter.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        // 监听数据
        mViewModel.getExerciseLogsList().observe(getViewLifecycleOwner(),
                mLogsList -> {
                    // 通知有变更
                    getView().findViewById(R.id.text_no_data)   // 没有数据提示语
                            .setVisibility(mLogsList.size() == 0 ? View.VISIBLE : View.GONE);
                    mAdapter.notifyDataSetChanged();
                });
        // 加载数据库
        ExerciseLogsRepository.getInstance(getContext())
                .loadExerciseLogsForProjectID(getActivity().getIntent().getLongExtra(EditLogActivity.EXERCISE_PROJECT_ID, 0))
                .observe(getViewLifecycleOwner(), list -> mViewModel.setExerciseLogsList(list));
        // ********* countdown *********
        // 倒计时点击
        mCountdownButton.setOnClickListener(v -> {
            // 首先判断是否正在计时中
            if (mCountdownDisposable == null) {
                // 没有在计时，打开时间选择
                List<String> arrayList = new ArrayList<>(Constants.getExercisesTime().keySet());
                OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getContext(),
                        (options1, options2, options3, v12) -> {
                            Integer timeCount = Constants.getExercisesTime().get(arrayList.get(options1));
                            // 开始倒计时
                            mCountdownDisposable = Observable
                                    .interval(0, 1, TimeUnit.SECONDS) //延迟0，间隔1s，单位秒
                                    .take(timeCount + 1)    // 计时次数
                                    .map(aLong -> timeCount - (int) ((long) aLong))   // 转换成 int
                                    .observeOn(AndroidSchedulers.mainThread())  // 主线程
                                    .doOnSubscribe(subscription -> { // 准备工作
                                        mCountdownImageView.setVisibility(View.INVISIBLE);
                                        mCountdownTextView.setVisibility(View.VISIBLE);
                                        mCountdownTextView.setText(String.format(getString(R.string.text_format_time_minute), 0, 0));
                                    }).subscribe(count -> { // 倒计时中
                                        int minutes = count / 60;
                                        int seconds = count % 60;
                                        mCountdownTextView.setText(String.format(getString(R.string.text_format_time_minute), minutes, seconds));
                                    }, throwable -> { // 出错
                                        throwable.printStackTrace();
                                        showToast("error : " + throwable.getMessage());
                                        // 重新设置一遍，会刷新显示
                                        mViewModel.setTimeCountLiveData(mViewModel.getTimeCountLiveData().getValue());
                                    }, () -> {  // 倒计时完成
                                        mCountdownImageView.setVisibility(View.VISIBLE);
                                        mCountdownTextView.setVisibility(View.INVISIBLE);
                                    });
                            mDisposables.add(mCountdownDisposable); // 添加销毁列表
                        }).build();
                optionsPickerView.setNPicker(arrayList, null, null);
                optionsPickerView.show();
            } else { // 暂停
                if (!mCountdownDisposable.isDisposed())
                    mCountdownDisposable.dispose();
                mCountdownDisposable = null;
                mCountdownImageView.setVisibility(View.VISIBLE);
                mCountdownTextView.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 添加日志 ： 按次数
     */
    private void addLogForRepetitions(String repetitions, String weight) {
        closeKeyboard();
        new Thread(() -> {
            String description = TextUtils.isEmpty(weight) ? repetitions + " reps." : weight + " kg x " + repetitions + " resp.";
            long exerciseID = getActivity().getIntent().getLongExtra(EditLogActivity.EXERCISE_PROJECT_ID, 0);
            ExerciseLogsEntity entity = new ExerciseLogsEntity(exerciseID, description, mViewModel.getDateLiveData().getValue().getTime());
            ExerciseLogsRepository.getInstance(getContext()).insertExerciseLogs(entity);
        }).start();
    }

    /**
     * 添加日志 ： 按时间
     */
    private void addLogForTime(int time) {
        closeKeyboard();
        new Thread(() -> {
            int seconds = time % 60;
            int minutes = time / 60;
            String description = minutes == 0 ? "" : minutes + " min ";
            description += seconds == 0 ? "" : seconds + " sec ";
            long exerciseID = getActivity().getIntent().getLongExtra(EditLogActivity.EXERCISE_PROJECT_ID, 0);
            ExerciseLogsEntity entity = new ExerciseLogsEntity(exerciseID, description, mViewModel.getDateLiveData().getValue().getTime());
            ExerciseLogsRepository.getInstance(getContext()).insertExerciseLogs(entity);
        }).start();
    }

    /**
     * 点击 log item
     */
    @Override
    public void onClickItemListener(ExerciseLogsEntity entity) {

    }

    /**
     * 删除 log item
     */
    @Override
    public void onClickDeleteListener(ExerciseLogsEntity entity) {
        new Thread(() -> {
            boolean isSuccessful = ExerciseLogsRepository.getInstance(getContext()).deleteProjectLogs(entity);
            getActivity().runOnUiThread(() -> {
                CharSequence showToast = isSuccessful ? getText(R.string.delete_successfully) : getText(R.string.delete_failed);
                showToast(showToast);
            });
        }).start();
    }

}