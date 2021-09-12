package com.gofitness.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.gofitness.base.BaseActivity;
import com.gofitness.database.Constants;
import com.gofitness.fragment.BodyStatsDetailsFragment;

public class BodyStatsDetailsActivity extends BaseActivity {

    public static final String BODY_STATS_KEY = "BODY_STATS_KEY";
    public static final String BODY_STATS_UNIT = "BODY_STATS_UNIT";

    @Override
    protected Fragment createFragment() {
        return BodyStatsDetailsFragment.newInstance();
    }

    public static void startActivity(Context context, String key,String unit) {
        Intent intent = new Intent(context, BodyStatsDetailsActivity.class);
        intent.putExtra(Constants.TITLE, key);
        intent.putExtra(BODY_STATS_KEY, key);
        intent.putExtra(BODY_STATS_UNIT, unit);
        context.startActivity(intent);
    }

}