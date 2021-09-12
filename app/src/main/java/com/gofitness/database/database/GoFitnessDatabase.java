package com.gofitness.database.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gofitness.database.dao.ExerciseLogsDAO;
import com.gofitness.database.dao.ExerciseProjectDAO;
import com.gofitness.database.dao.BodyStatsDAO;
import com.gofitness.database.entity.BodyStatsEntity;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.entity.ExerciseProjectEntity;


@Database(entities = {BodyStatsEntity.class, ExerciseProjectEntity.class, ExerciseLogsEntity.class}, version = 1, exportSchema = false)
public abstract class GoFitnessDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "Go_Fitness_db";

    private volatile static GoFitnessDatabase databaseInstance;

    public static synchronized GoFitnessDatabase getInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), GoFitnessDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration() // 数据库更新时删除数据重新创建，只能测试时打开
                    .build();
        }
        return databaseInstance;
    }

    public abstract BodyStatsDAO mBodyStatsDAO();

    public abstract ExerciseProjectDAO mExerciseProjectDAO();

    public abstract ExerciseLogsDAO mExerciseLogsDAO();


}