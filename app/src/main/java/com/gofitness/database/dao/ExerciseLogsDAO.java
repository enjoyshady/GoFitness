package com.gofitness.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.gofitness.database.entity.ExerciseLogsCount;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.entity.ExerciseProjectLogs;

import java.util.List;

@Dao
public interface ExerciseLogsDAO {

    /**
     * 插入日志数据
     *
     * @param entity 待插入的数据
     * @return 插入成功返回id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertExerciseLogs(ExerciseLogsEntity entity);


    /**
     * 查询当天的数据
     */
    @Transaction
    @Query("SELECT * FROM Exercise_Logs " +
            "WHERE strftime('%Y-%m-%d',datetime(timestamp/1000,'unixepoch', 'localtime')) = date() " +
            "ORDER BY projectID, timestamp DESC")
    LiveData<List<ExerciseProjectLogs>> loadExerciseProjectLogs();

    /**
     * 查询某天日志
     *
     * @param timestamp 当天任意时间时间戳
     */
    @Transaction
    @Query("SELECT * FROM Exercise_Logs " +
            "WHERE strftime('%Y-%m-%d',datetime(timestamp/1000,'unixepoch', 'localtime')) = strftime('%Y-%m-%d',datetime(:timestamp/1000,'unixepoch', 'localtime')) " +
            "ORDER BY projectID, timestamp DESC")
    LiveData<List<ExerciseProjectLogs>> loadExerciseProjectLogsForDate(Long timestamp);

    /**
     * 查询每天有多少个数据
     */
    @Query("SELECT timestamp,strftime('%Y-%m-%d',datetime(timestamp/1000,'unixepoch', 'localtime')) as 'date',Count(*) as 'count' " +
            "FROM Exercise_Logs " +
            "GROUP BY strftime('%Y-%m-%d',datetime(timestamp/1000,'unixepoch', 'localtime'))")
    LiveData<List<ExerciseLogsCount>> loadExerciseProjectLogsCount();

    /**
     * 查询当天某项目的数据
     *
     * @param projectID 项目id
     */
    @Query("SELECT * FROM Exercise_Logs " +
            "WHERE strftime('%Y-%m-%d',datetime(timestamp/1000,'unixepoch', 'localtime')) = date() AND projectID = :projectID  " +
            "ORDER BY timestamp DESC")
    LiveData<List<ExerciseLogsEntity>> loadExerciseLogsForProjectID(long projectID);

    /**
     * 删除数据
     *
     * @param logsEntity 待删除的数据
     * @return 操作的行数
     */
    @Delete
    int deleteProjectLogs(ExerciseLogsEntity logsEntity);

}
