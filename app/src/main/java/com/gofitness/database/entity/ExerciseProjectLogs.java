package com.gofitness.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ExerciseProjectLogs {

    @Embedded
    public ExerciseLogsEntity mLogsEntity;

    @Relation(
            parentColumn = "projectID",
            entityColumn = "projectID"
    )

    public ExerciseProjectEntity ExerciseProjectEntity;

    public ExerciseLogsEntity getLogsEntity() {
        return mLogsEntity;
    }

    public void setLogsEntity(ExerciseLogsEntity logsEntity) {
        mLogsEntity = logsEntity;
    }


    public ExerciseProjectEntity getExerciseProjectEntity() {
        return ExerciseProjectEntity;
    }

    public void setExerciseProjectEntity(ExerciseProjectEntity exerciseProjectEntity) {
        ExerciseProjectEntity = exerciseProjectEntity;
    }

    @Override
    public String toString() {
        return "ExerciseProjectLogs{" +
                "mLogsEntity=" + mLogsEntity +
                ", ExerciseProjectEntity=" + ExerciseProjectEntity +
                '}';
    }

}
