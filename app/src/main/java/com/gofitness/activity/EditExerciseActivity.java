package com.gofitness.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.gofitness.R;
import com.gofitness.base.BaseActivity;
import com.gofitness.database.Constants;
import com.gofitness.fragment.ExerciseDetailsModification.EditExerciseFragment;

public class EditExerciseActivity extends BaseActivity {

    public static final String EXERCISE_PROJECT_ID = "EXERCISE_PROJECT_ID";
    public static final String EXERCISE_IS_ADD = "EXERCISE_IS_ADD";

    @Override
    protected Fragment createFragment() {
        return EditExerciseFragment.newInstance();
    }

    public static void startActivityForEdit(Context context, long projectID) {
        Intent intent = new Intent(context, EditExerciseActivity.class);
        intent.putExtra(Constants.TITLE, context.getString(R.string.title_edit_exercise));
        intent.putExtra(EXERCISE_PROJECT_ID, projectID);
        context.startActivity(intent);
    }

    public static void startActivityForAdd(Context context) {
        Intent intent = new Intent(context, EditExerciseActivity.class);
        intent.putExtra(Constants.TITLE, context.getString(R.string.title_add_exercise));
        intent.putExtra(EXERCISE_IS_ADD, true);
        context.startActivity(intent);
    }

}