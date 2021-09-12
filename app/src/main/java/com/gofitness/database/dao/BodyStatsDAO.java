package com.gofitness.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gofitness.database.entity.BodyStatsEntity;

import java.util.List;

@Dao
public interface BodyStatsDAO {

    /**
     * 插入用户数据
     *
     * @param entity 待插入的数据
     * @return 插入成功返回id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBodyStats(BodyStatsEntity entity);

    /**
     * 查询 BodyStats
     *
     * @param key 按照关键字 key 筛选
     * @return 最新的一个数据
     */
    @Query("SELECT * FROM Body_Stats WHERE `key` = :key ORDER BY timestamp DESC")
    LiveData<BodyStatsEntity> loadBodyStatsForKey(String key);

    /**
     * 查询 BodyStats list
     *
     * @param key 按照关键字 key 筛选
     * @return 所有数据
     */
    @Query("SELECT * FROM Body_Stats WHERE `key` = :key ORDER BY timestamp DESC")
    LiveData<List<BodyStatsEntity>> loadBodyStatsListForKey(String key);

    /**
     * 删除数据
     *
     * @param entity 待删除的数据
     * @return 是否删除成功
     */
    @Delete
    int deleteBodyStatsData(BodyStatsEntity entity);

}
