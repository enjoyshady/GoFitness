package com.gofitness.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Exercise_Logs")
public class ExerciseLogsEntity {

    @Nullable
    @PrimaryKey(autoGenerate = true)
    private long logsID;

    // foreign key
    private long projectID;

    // e.g : 30' x 1
    @Nullable
    private String description;

    // e.g : 2021-7-28 (Long)
    @NonNull
    private Long timestamp;

    @Ignore
    public ExerciseLogsEntity(){

    }

    public ExerciseLogsEntity(long projectID, @Nullable String description, @NonNull Long timestamp) {
        this.projectID = projectID;
        this.description = description;
        this.timestamp = timestamp;
    }

    public long getLogsID() {
        return logsID;
    }

    public void setLogsID(long logsID) {
        this.logsID = logsID;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @NonNull
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ExerciseLogsEntity{" +
                "logsID=" + logsID +
                ", projectID=" + projectID +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
