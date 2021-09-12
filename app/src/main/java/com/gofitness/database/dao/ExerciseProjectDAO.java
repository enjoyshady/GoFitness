package com.gofitness.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gofitness.database.entity.ExerciseProjectEntity;

import java.util.List;

@Dao
public interface ExerciseProjectDAO {

    /**
     * 插入数据库
     *
     * @param entity 待插入数据
     * @return 插入成功返回 id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProjectData(ExerciseProjectEntity entity);

    /**
     * 查询数据库 ： 按照 muscle 查询
     *
     * @param muscle 查询关键字
     * @return 查询结果 （ 监听形式 ）
     */
    @Query("SELECT * FROM Exercise_Project WHERE primaryMuscle = :muscle OR secondaryMuscle = :muscle")
    LiveData<List<ExerciseProjectEntity>> observeProjectListForMuscle(String muscle);

    /**
     * 查询数据库 ： 按照 MuscleGroup 查询
     *
     * @param muscleGroup 查询关键字
     * @return 查询结果 （ 监听形式 ）
     */
    @Query("SELECT * FROM Exercise_Project WHERE muscleGroup = :muscleGroup")
    LiveData<List<ExerciseProjectEntity>> observeProjectListForMuscleGroup(String muscleGroup);

    /**
     * 查询数据库 ： 按照 id 查询
     *
     * @param projectID 查询关键字
     * @return 查询结果 （ 监听形式 ）
     */
    @Query("SELECT * FROM Exercise_Project WHERE projectID = :projectID")
    LiveData<ExerciseProjectEntity> observeProjectListForID(long projectID);

   /**
     * 查询数据库 ： 按照 id 查询
     *
     * @param projectID 查询关键字
     * @return 查询结果
     */
    @Query("SELECT * FROM Exercise_Project WHERE projectID = :projectID")
    ExerciseProjectEntity loadProjectListForID(long projectID);

    /**
     * 查询数据库 ： 按照 MuscleGroup 查询
     *
     * @return 查询结果 （ 监听形式 ）
     */
    @Query("SELECT * FROM Exercise_Project ")
    LiveData<List<ExerciseProjectEntity>> observeAllProjectList();

    /**
     * 删除数据
     *
     * @return 删除了几行
     */
    @Delete
    int deleteProjectData(ExerciseProjectEntity entity);

}
