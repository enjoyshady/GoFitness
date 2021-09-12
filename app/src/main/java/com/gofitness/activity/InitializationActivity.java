package com.gofitness.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.gofitness.base.BaseActivity;
import com.gofitness.fragment.InitializationFragment;

public class InitializationActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return InitializationFragment.newInstance();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, InitializationActivity.class);
        context.startActivity(intent);
    }

}