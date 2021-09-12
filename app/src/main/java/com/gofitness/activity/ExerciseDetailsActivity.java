package com.gofitness.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.gofitness.R;
import com.gofitness.base.BaseActivity;
import com.gofitness.database.Constants;
import com.gofitness.fragment.ExerciseDetails.ExerciseDetailsFragment;

public class ExerciseDetailsActivity extends BaseActivity {

    public static final String EXERCISE_PROJECT_ID = "EXERCISE_PROJECT_ID";

    @Override
    protected Fragment createFragment() {
        return ExerciseDetailsFragment.newInstance();
    }

    public static void startActivity(Context context, long projectID) {
        Intent intent = new Intent(context, ExerciseDetailsActivity.class);
        intent.putExtra(Constants.TITLE, context.getString(R.string.title_exercise_details));
        intent.putExtra(EXERCISE_PROJECT_ID, projectID);
        context.startActivity(intent);
    }

}