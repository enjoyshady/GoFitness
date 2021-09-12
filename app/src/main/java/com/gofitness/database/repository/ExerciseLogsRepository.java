package com.gofitness.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.gofitness.database.dao.ExerciseLogsDAO;
import com.gofitness.database.database.GoFitnessDatabase;
import com.gofitness.database.entity.ExerciseLogsCount;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.entity.ExerciseProjectLogs;

import java.util.List;

public class ExerciseLogsRepository {

    private volatile static ExerciseLogsRepository sRepository;

    private ExerciseLogsDAO mExerciseLogsDAO;

    private ExerciseLogsRepository() {
    }

    private ExerciseLogsRepository(Context context) {
        mExerciseLogsDAO = GoFitnessDatabase.getInstance(context).mExerciseLogsDAO();
    }

    public static synchronized ExerciseLogsRepository getInstance(Context context) {
        if (sRepository == null) {
            sRepository = new ExerciseLogsRepository(context);
        }
        return sRepository;
    }


    /**
     * 插入日志数据
     *
     * @param entity 待插入的数据
     * @return 插入成功返回id
     */
    public long insertExerciseLogs(ExerciseLogsEntity entity) {
        return mExerciseLogsDAO.insertExerciseLogs(entity);
    }


    /**
     * 查询当天日志
     */
    public LiveData<List<ExerciseProjectLogs>> loadExerciseProjectLogs() {
        return mExerciseLogsDAO.loadExerciseProjectLogs();
    }

    /**
     * 查询某天日志
     *
     * @param timestamp 当天任意时间时间戳
     */
    public LiveData<List<ExerciseProjectLogs>> loadExerciseProjectLogsForDate(Long timestamp) {
        return mExerciseLogsDAO.loadExerciseProjectLogsForDate(timestamp);
    }

    /**
     * 查询每天有多少个数据
     */
    public LiveData<List<ExerciseLogsCount>> loadExerciseProjectLogsCount() {
        return mExerciseLogsDAO.loadExerciseProjectLogsCount();
    }

    /**
     * 查询当天某项目的数据
     *
     * @param projectID 项目id
     */
    public LiveData<List<ExerciseLogsEntity>> loadExerciseLogsForProjectID(long projectID) {
        return mExerciseLogsDAO.loadExerciseLogsForProjectID(projectID);
    }

    /**
     * 删除数据
     *
     * @param logsEntity 待删除的数据
     * @return 是否删除成功
     */
    public boolean deleteProjectLogs(ExerciseLogsEntity logsEntity) {
        return mExerciseLogsDAO.deleteProjectLogs(logsEntity) > 0;
    }

}