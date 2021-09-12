package com.gofitness.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.gofitness.database.Constants;
import com.gofitness.database.dao.ExerciseProjectDAO;
import com.gofitness.database.database.GoFitnessDatabase;
import com.gofitness.database.entity.ExerciseProjectEntity;

import java.util.List;

public class ExerciseProjectRepository {

    private volatile static ExerciseProjectRepository sRepository;

    private ExerciseProjectDAO mExerciseProjectDAO;

    private ExerciseProjectRepository() {
    }

    private ExerciseProjectRepository(Context context) {
        mExerciseProjectDAO = GoFitnessDatabase.getInstance(context).mExerciseProjectDAO();
    }

    public static synchronized ExerciseProjectRepository getInstance(Context context) {
        if (sRepository == null) {
            sRepository = new ExerciseProjectRepository(context);
        }
        return sRepository;
    }

    /**
     * 插入多条数据
     *
     * @param entities 不定项列表，可以是多条
     * @return 插入是否成功
     */
    public boolean insertProjectData(List<ExerciseProjectEntity> entities) {
        boolean flag = entities != null && entities.size() != 0;
        for (ExerciseProjectEntity entity : entities) {
            flag &= mExerciseProjectDAO.insertProjectData(entity) != -1;
        }
        return flag;
    }

    /**
     * 插入一数据
     *
     * @param entity 待插入数据
     * @return 插入是否成功
     */
    public boolean insertProjectData(ExerciseProjectEntity entity) {
        return mExerciseProjectDAO.insertProjectData(entity) != -1;
    }

    /**
     * 查询数据库 ： 按照 MuscleGroup 查询
     *
     * @param muscleGroup 查询关键字
     * @return 查询结果 （ 监听形式 ）
     */
    public LiveData<List<ExerciseProjectEntity>> observeProjectListForMuscleGroup(String muscleGroup) {
        if (Constants.AllExercises.equals(muscleGroup))
            return mExerciseProjectDAO.observeAllProjectList();
        return mExerciseProjectDAO.observeProjectListForMuscleGroup(muscleGroup);
    }

    /**
     * 查询数据库 ： 按照 Muscle 查询
     *
     * @param muscle 查询关键字
     * @return 查询结果 （ 监听形式 ）
     */
    public LiveData<List<ExerciseProjectEntity>> observeProjectListForMuscle(String muscle) {
        return mExerciseProjectDAO.observeProjectListForMuscle(muscle);
    }

    /**
     * 查询数据库 ： 按照 id 查询
     *
     * @param projectID 查询关键字
     * @return 查询结果 （ 监听形式 ）
     */
    public LiveData<ExerciseProjectEntity> observeProjectListForID(long projectID) {
        return mExerciseProjectDAO.observeProjectListForID(projectID);
    }

    /**
     * 查询数据库 ： 按照 id 查询
     *
     * @param projectID 查询关键字
     * @return 查询结果
     */
    public ExerciseProjectEntity loadProjectListForID(long projectID) {
        return mExerciseProjectDAO.loadProjectListForID(projectID);
    }

    /**
     * 删除数据
     *
     * @return 删除是否成功
     */
    public boolean deleteProjectData(ExerciseProjectEntity entity) {
        return mExerciseProjectDAO.deleteProjectData(entity) > 0;
    }

}