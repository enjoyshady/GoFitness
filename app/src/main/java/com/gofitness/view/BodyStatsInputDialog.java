package com.gofitness.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gofitness.R;
import com.gofitness.database.entity.BodyStatsEntity;
import com.gofitness.database.repository.BodyStatsRepository;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BodyStatsInputDialog extends DialogFragment {

    private static final String DIALOG_TITLE = "DIALOG_TITLE";
    private static final String DIALOG_UNIT = "DIALOG_UNIT";
    private static final String DIALOG_IS_SHOW_DATE = "DIALOG_IS_SHOW_DATE";

    private Date mDate = new Date();
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
    private String mValue = "0.0";

    OptionsPickerView<String> optionsPickerView;
    TimePickerView timePickerView;
    FrameLayout mFrameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static BodyStatsInputDialog newInstance(String key, String unit, boolean isShowDate) {
        BodyStatsInputDialog bodyStatsInputDialog = new BodyStatsInputDialog();
        bodyStatsInputDialog.setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme);
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TITLE, key);
        bundle.putString(DIALOG_UNIT, unit);
        bundle.putBoolean(DIALOG_IS_SHOW_DATE, isShowDate);
        bodyStatsInputDialog.setArguments(bundle);
        return bodyStatsInputDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_body_stats_input, container, false);
        mFrameLayout = rootView.findViewById(R.id.FrameLayout);
        // title
        TextView mTitleTextView = rootView.findViewById(R.id.dialog_title);
        mTitleTextView.setText(getArguments().getString(DIALOG_TITLE));
        // value
        TextView mValueTextView = rootView.findViewById(R.id.dialog_value);
        mValueTextView.setText(String.format("0.0 %s", getArguments().getString(DIALOG_UNIT)));
        mValueTextView.setOnClickListener(v -> showValuesPickerView(mValueTextView));
        // date
        TextView mDateTextView = rootView.findViewById(R.id.dialog_date);
        if (getArguments().getBoolean(DIALOG_IS_SHOW_DATE)) {
            mDateTextView.setText(dateFormat.format(mDate));
            mDateTextView.setOnClickListener(v -> showTimePickerView(mDateTextView));
        } else {
            mDateTextView.setVisibility(View.GONE);
        }
        // OK
        rootView.findViewById(R.id.dialog_ok).setOnClickListener(v -> {
            submitDatabase();
            dismiss();
        });
        // Cancel
        rootView.findViewById(R.id.dialog_cancel).setOnClickListener(v -> dismiss());
        return rootView;
    }

    /**
     * 提交数据库
     */
    private void submitDatabase() {
        new Thread(() -> {
            String key = getArguments().getString(DIALOG_TITLE);
            String unit = getArguments().getString(DIALOG_UNIT);
            BodyStatsEntity entity = new BodyStatsEntity(key, mValue, unit, mDate.getTime());
            BodyStatsRepository.getInstance(getContext()).insertBodyStats(entity);
        }).start();
    }

    /**
     * 打开 values 选择器
     */
    private void showValuesPickerView(TextView mValueTextView) {
        if (optionsPickerView == null) {
            List<String> integer = new ArrayList<>();
            List<String> decimals = new ArrayList<>();
            for (int i = 0; i <= 500; i++) {
                integer.add(String.valueOf(i));
                if (i < 10) decimals.add("." + i);
            }
            //条件选择器
            optionsPickerView = new OptionsPickerBuilder(getContext(), null)
                    .setOptionsSelectChangeListener((options1, options2, options3) -> {
                        mValue = integer.get(options1) + decimals.get(options2);
                        mValueTextView.setText(String.format("%s %s", mValue, getArguments().getString(DIALOG_UNIT)));
                    }).setDecorView(mFrameLayout).build();
            optionsPickerView.setNPicker(integer, decimals, null);
        }
        if (timePickerView != null)
            timePickerView.dismiss();
        optionsPickerView.show();
    }

    /**
     * 打开时间选择器
     */
    private void showTimePickerView(TextView showView) {
        if (timePickerView == null) {
            timePickerView = new TimePickerBuilder(getContext(), null)
                    .setTimeSelectChangeListener(date ->
                            showView.setText(dateFormat.format(mDate = date))
                    ).setDecorView(mFrameLayout).build();
        }
        if (optionsPickerView != null)
            optionsPickerView.dismiss();
        timePickerView.show();
    }

}
