package com.gofitness.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gofitness.R;
import com.gofitness.base.BaseActivity;
import com.gofitness.database.Constants;
import com.gofitness.fragment.EditLogFragment;

public class EditLogActivity extends BaseActivity {

    public static final String EXERCISE_PROJECT_ID = "EXERCISE_PROJECT_ID";
    public static final String EXERCISE_PROJECT_NAME = "EXERCISE_PROJECT_NAME";

    @Override
    protected Fragment createFragment() {
        return EditLogFragment.newInstance();
    }

    public static void startActivity(Context context, long projectID,String exerciseName,String title) {
        Intent intent = new Intent(context, EditLogActivity.class);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(EXERCISE_PROJECT_ID, projectID);
        intent.putExtra(EXERCISE_PROJECT_NAME, exerciseName);
        context.startActivity(intent);
    }

}