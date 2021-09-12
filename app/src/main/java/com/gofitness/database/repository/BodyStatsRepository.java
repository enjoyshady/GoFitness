package com.gofitness.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.gofitness.database.dao.BodyStatsDAO;
import com.gofitness.database.database.GoFitnessDatabase;
import com.gofitness.database.entity.BodyStatsEntity;

import java.util.List;

public class BodyStatsRepository {

    private volatile static BodyStatsRepository sRepository;

    private BodyStatsDAO mBodyStatsDAO;

    private BodyStatsRepository() {
    }

    private BodyStatsRepository(Context context) {
        mBodyStatsDAO = GoFitnessDatabase.getInstance(context).mBodyStatsDAO();
    }

    public static synchronized BodyStatsRepository getInstance(Context context) {
        if (sRepository == null) {
            sRepository = new BodyStatsRepository(context);
        }
        return sRepository;
    }

    /**
     * 插入用户数据
     *
     * @param entity 待插入的数据
     * @return 插入成功返回id
     */
    public long insertBodyStats(BodyStatsEntity entity) {
        return mBodyStatsDAO.insertBodyStats(entity);
    }

    /**
     * 查询 BodyStats
     *
     * @param key 按照关键字 key 筛选
     * @return 最新的一个数据
     */
    public LiveData<BodyStatsEntity> loadBodyStatsForKey(String key) {
        return mBodyStatsDAO.loadBodyStatsForKey(key);
    }

    /**
     * 查询 BodyStats list
     *
     * @param key 按照关键字 key 筛选
     * @return 所有数据
     */
    public LiveData<List<BodyStatsEntity>> loadBodyStatsListForKey(String key) {
        return mBodyStatsDAO.loadBodyStatsListForKey(key);
    }

    /**
     * 删除数据
     *
     * @param entity 待删除的数据
     * @return 是否删除成功
     */
    public boolean deleteBodyStatsData(BodyStatsEntity entity) {
        return mBodyStatsDAO.deleteBodyStatsData(entity) > 0;
    }
}