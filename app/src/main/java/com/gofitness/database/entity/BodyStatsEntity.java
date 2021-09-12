package com.gofitness.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Body_Stats")
public class BodyStatsEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long bodyStatsID;

    // e.g : height
    @NonNull
    private String key;

    // e.g : 50
    @NonNull
    private String value;

    // e.g : kg
    @NonNull
    private String unit;

    // e.g : 2021-7-28 12:00:00 (Long)
    @NonNull
    private Long timestamp;

    @Ignore
    public BodyStatsEntity() {
    }

    public BodyStatsEntity(@NonNull String key, @NonNull String value, @NonNull String unit, @NonNull Long timestamp) {
        this.key = key;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    public long getBodyStatsID() {
        return bodyStatsID;
    }

    public void setBodyStatsID(long bodyStatsID) {
        this.bodyStatsID = bodyStatsID;
    }

    @NonNull
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + bodyStatsID + '\'' +
                ", " + key + "='" + value + unit + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
