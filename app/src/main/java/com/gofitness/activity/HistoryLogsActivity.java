package com.gofitness.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gofitness.R;
import com.gofitness.base.BaseActivity;
import com.gofitness.base.BaseFragment;
import com.gofitness.database.Constants;
import com.gofitness.fragment.HistoryLogsFragment;

public class HistoryLogsActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return HistoryLogsFragment.newInstance();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HistoryLogsActivity.class);
        intent.putExtra(Constants.TITLE,context.getString(R.string.title_history));
        context.startActivity(intent);
    }

}