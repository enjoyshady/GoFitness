package com.gofitness.database.entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Exercise_Project")
public class ExerciseProjectEntity {

    @Nullable
    @PrimaryKey(autoGenerate = true)
    private long projectID;

    @Nullable
    private String exerciseName;

    @Nullable
    private String muscleGroup;

    @Nullable
    private String imageURL;

    @Nullable
    private String primaryMuscle;

    @Nullable
    private String secondaryMuscle;

    @Nullable
    private String description;

    public ExerciseProjectEntity() {

    }

    @Ignore
    public ExerciseProjectEntity(@Nullable String exerciseName, @Nullable String muscleGroup, @Nullable String imageURL, @Nullable String primaryMuscle, @Nullable String secondaryMuscle, @Nullable String description) {
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
        this.imageURL = imageURL;
        this.primaryMuscle = primaryMuscle;
        this.secondaryMuscle = secondaryMuscle;
        this.description = description;
    }

    /**
     * string 转 list （ json格式化 ）
     */
    @Ignore
    public static List<ExerciseProjectEntity> getListForJsonString(String json) {
        List<ExerciseProjectEntity> beans = null;
        try {
            Type type = new TypeToken<ArrayList<ExerciseProjectEntity>>() {
            }.getType();
            beans = new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans == null ? new ArrayList<>() : beans;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    @Nullable
    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(@Nullable String exerciseName) {
        this.exerciseName = exerciseName;
    }

    @Nullable
    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(@Nullable String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    @Nullable
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(@Nullable String imageURL) {
        this.imageURL = imageURL;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(@Nullable String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    @Nullable
    public String getSecondaryMuscle() {
        return secondaryMuscle;
    }

    public void setSecondaryMuscle(@Nullable String secondaryMuscle) {
        this.secondaryMuscle = secondaryMuscle;
    }

    @Override
    public String toString() {
        return "ExerciseProjectEntity{" +
                "projectID=" + projectID +
                ", exerciseName='" + exerciseName + '\'' +
                ", muscleGroup='" + muscleGroup + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", primaryMuscle='" + primaryMuscle + '\'' +
                ", secondaryMuscle='" + secondaryMuscle + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
