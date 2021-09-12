package com.gofitness.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.gofitness.base.BaseActivity;
import com.gofitness.database.Constants;
import com.gofitness.fragment.ProjectListFragment;

public class ProjectListActivity extends BaseActivity {

    public static final String PROJECT_TYPE = "PROJECT_TYPE";
    public static final int FOR_MUSCLE_GROUP = 0x0001;
    public static final int FOR_MUSCLE = 0x0002;

    public static final String PROJECT_MUSCLE_GROUP = "PROJECT_MUSCLE_GROUP";
    public static final String PROJECT_MUSCLE = "PROJECT_MUSCLE";

    @Override
    protected Fragment createFragment() {
        return ProjectListFragment.newInstance();
    }

    public static void startActivityForMuscleGroup(Context context, String muscleGroup) {
        Intent intent = new Intent(context, ProjectListActivity.class);
        intent.putExtra(Constants.TITLE, muscleGroup);
        intent.putExtra(PROJECT_MUSCLE_GROUP, muscleGroup);
        intent.putExtra(PROJECT_TYPE, FOR_MUSCLE_GROUP);
        context.startActivity(intent);
    }

    public static void startActivityForMuscle(Context context, String muscle) {
        Intent intent = new Intent(context, ProjectListActivity.class);
        intent.putExtra(Constants.TITLE, muscle);
        intent.putExtra(PROJECT_MUSCLE, muscle);
        intent.putExtra(PROJECT_TYPE, FOR_MUSCLE);
        context.startActivity(intent);
    }

}